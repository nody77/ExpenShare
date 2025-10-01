package com.expenshare.model.mapper;

import com.expenshare.model.dto.expense.ShareDto;
import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.model.entity.ExpenseShareEntity;
import com.expenshare.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "jsr330")
public interface ExpenseShareMapper {

    @Mapping(target = "expense", source = "expenseId")
    @Mapping(target = "user", source = "dto.userId")
    @Mapping(target = "shareAmount", source = "dto.amount")
    @Mapping(target = "id", ignore = true)
    ExpenseShareEntity toEntity(ShareDto dto, Long expenseId);

    @Mapping(target = "userId", source = "e.user.id")
    @Mapping(target = "amount", source = "e.shareAmount")
    ShareDto toDto(ExpenseShareEntity e);

    default ExpenseEntity mapExpense(long expenseId) {
        if (expenseId == 0) {
            return null;
        }
        ExpenseEntity expense = new ExpenseEntity();
        expense.setId(expenseId);
        return expense;
    }

    default UserEntity mapUser(long userId) {
        if (userId == 0) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(userId);
        return user;
    }
}
