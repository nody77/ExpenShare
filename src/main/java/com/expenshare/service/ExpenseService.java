package com.expenshare.service;

import com.expenshare.event.KafkaProducer;
import com.expenshare.exception.ValidationException;
import com.expenshare.model.dto.expense.CreateExpenseRequest;
import com.expenshare.model.dto.expense.ExpenseDto;
import com.expenshare.model.dto.expense.ShareDto;
import com.expenshare.model.entity.*;
import com.expenshare.model.enums.ExpenseSplitType;
import com.expenshare.model.mapper.ExpenseMapper;
import com.expenshare.model.mapper.ExpenseShareMapper;
import com.expenshare.repository.facade.ExpenseRepositoryFacade;
import com.expenshare.repository.facade.ExpenseShareRepositoryFacade;
import com.expenshare.repository.facade.GroupRepositoryFacade;
import com.expenshare.repository.facade.UserRepositoryFacade;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton
public class ExpenseService {

    private final ExpenseRepositoryFacade expenseRepositoryFacade;
    private final ExpenseShareRepositoryFacade expenseShareRepositoryFacade;
    private final GroupRepositoryFacade groupRepositoryFacade;
    private final UserRepositoryFacade userRepositoryFacade;
    private final ExpenseMapper expenseMapper;
    private final ExpenseShareMapper expenseShareMapper;
    private final KafkaProducer kafkaProducer;

    public ExpenseService(ExpenseRepositoryFacade expenseRepositoryFacade, ExpenseShareRepositoryFacade expenseShareRepositoryFacade, GroupRepositoryFacade groupRepositoryFacade, UserRepositoryFacade userRepositoryFacade, ExpenseMapper expenseMapper, ExpenseShareMapper expenseShareMapper, KafkaProducer kafkaProducer) {
        this.expenseRepositoryFacade = expenseRepositoryFacade;
        this.expenseShareRepositoryFacade = expenseShareRepositoryFacade;
        this.groupRepositoryFacade = groupRepositoryFacade;
        this.userRepositoryFacade = userRepositoryFacade;
        this.expenseMapper = expenseMapper;
        this.expenseShareMapper = expenseShareMapper;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional
    public ExpenseDto createExpense(CreateExpenseRequest req){

        //Validate that if the split type is EXACT or PERCENT that the split list is not empty
        if(req.getSplitType().equals(ExpenseSplitType.EXACT)||req.getSplitType().equals(ExpenseSplitType.PERCENT)){
            if(req.getSplit().isEmpty()){
                throw new ValidationException("Split array should not be empty");
            }
        }

        //Validate that if split type is EXACT or PERCENT that the split list is sum is correct
        if(req.getSplitType().equals(ExpenseSplitType.EXACT)){
            validateParticipantSumExact(req);
        } else if (req.getSplitType().equals(ExpenseSplitType.PERCENT)){
            validateParticipantSumPercent(req);
        }

        // Validate that the group and user paid are found
        GroupEntity group = groupRepositoryFacade.getGroupByID(req.getGroupId());
        UserEntity userWhoPaid = userRepositoryFacade.getUserById(req.getPaidBy());

        ExpenseEntity expense = expenseMapper.toEntity(req);
        expense.setGroup(group);
        expense.setUser(userWhoPaid);

        ExpenseEntity newExpense = expenseRepositoryFacade.createExpense(expense.getGroup(), expense.getUser(), expense.getAmount(), expense.getDescription(),expense.getExpenseSplitType());

        List<ExpenseShareEntity> participants = new ArrayList<>();
        if(newExpense.getExpenseSplitType().equals(ExpenseSplitType.PERCENT)){
            participants = createExpensePercent(newExpense, req);
        }
        else if(newExpense.getExpenseSplitType().equals(ExpenseSplitType.EXACT)){
            participants = createExpenseExact(newExpense, req);
        }
        else if(newExpense.getExpenseSplitType().equals(ExpenseSplitType.EQUAL)){
            participants = createExpenseEqual(newExpense);
        }

        List<ShareDto> participantsDto = new ArrayList<>();
        for(ExpenseShareEntity participant: participants){
            participantsDto.add(expenseShareMapper.toDto(participant));
        }

        ExpenseDto newExpenseDto = expenseMapper.toDto(newExpense, participantsDto);

        kafkaProducer.sendExpenseCreated(newExpenseDto);

        return newExpenseDto;
    }

    private void validateParticipantSumExact(CreateExpenseRequest req){
        BigDecimal totalAmount = BigDecimal.valueOf(0.0);
        for(ShareDto share:req.getSplit()){
            totalAmount = totalAmount.add(share.getAmount());
        }
        if (!totalAmount.equals(req.getAmount())){
            throw new ValidationException("Split total must equal to the total paid");
        }
    }

    private void validateParticipantSumPercent(CreateExpenseRequest req){
        BigDecimal totalPercent = BigDecimal.valueOf(0.0);
        for(ShareDto share:req.getSplit()){
            totalPercent = totalPercent.add(share.getAmount());
        }
        if (!totalPercent.equals(BigDecimal.valueOf(100.0))){
            throw new ValidationException("Split percentages must total 100");
        }

    }

    private List<ExpenseShareEntity> createExpenseEqual(ExpenseEntity expense){
        List<ExpenseShareEntity> participants = new ArrayList<>();
        BigDecimal shareAmount = expense.getAmount().divide(BigDecimal.valueOf(expense.getGroup().getGroupMemberEntity().size()), 2, RoundingMode.HALF_UP);
        for(GroupMemberEntity groupMember: expense.getGroup().getGroupMemberEntity()){
            if(Objects.equals(groupMember.getUser().getId(), expense.getUser().getId())){
                // user who paid
                ExpenseShareEntity newExpenseShare = expenseShareRepositoryFacade.createExpenseShare(expense, groupMember.getUser(), shareAmount.subtract(expense.getAmount()));
                participants.add(newExpenseShare);
            }
            else{
                ExpenseShareEntity newExpenseShare = expenseShareRepositoryFacade.createExpenseShare(expense, groupMember.getUser(), shareAmount);
                participants.add(newExpenseShare);
            }
        }
        return participants;

    }

    private List<ExpenseShareEntity> createExpenseExact(ExpenseEntity expense, CreateExpenseRequest req){
        List<ExpenseShareEntity> participants = new ArrayList<>();
        for(ShareDto participant:req.getSplit()){
            ExpenseShareEntity expenseShare = expenseShareMapper.toEntity(participant, expense.getId());
            UserEntity user = userRepositoryFacade.getUserById(expenseShare.getUser().getId());

            if(user.getId().equals(expense.getUser().getId())){
                ExpenseShareEntity newExpenseShare = expenseShareRepositoryFacade.createExpenseShare(expense, user, expenseShare.getShareAmount().subtract(expense.getAmount()));
                participants.add(newExpenseShare);
            }
            else{
                ExpenseShareEntity newExpenseShare = expenseShareRepositoryFacade.createExpenseShare(expense, user, expenseShare.getShareAmount());
                participants.add(newExpenseShare);
            }
        }
        return participants;
    }

    private List<ExpenseShareEntity> createExpensePercent(ExpenseEntity expense, CreateExpenseRequest req){
        List<ExpenseShareEntity> participants = new ArrayList<>();
        for(ShareDto participant:req.getSplit()){

            ExpenseShareEntity expenseShare = expenseShareMapper.toEntity(participant, expense.getId());

            BigDecimal shareAmount = expense.getAmount()
                    .multiply(participant.getAmount())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            UserEntity user = userRepositoryFacade.getUserById(expenseShare.getUser().getId());

            if(user.getId().equals(expense.getUser().getId())){
                ExpenseShareEntity newExpenseShare = expenseShareRepositoryFacade.createExpenseShare(expense, user, shareAmount.subtract(expense.getAmount()));
                participants.add(newExpenseShare);
            }
            else{
                ExpenseShareEntity newExpenseShare = expenseShareRepositoryFacade.createExpenseShare(expense, user, shareAmount);
                participants.add(newExpenseShare);
            }
        }

        return participants;
    }
}
