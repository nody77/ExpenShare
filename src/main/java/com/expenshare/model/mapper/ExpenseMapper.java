package com.expenshare.model.mapper;

import com.expenshare.model.dto.expense.CreateExpenseRequest;
import com.expenshare.model.dto.expense.ExpenseDto;
import com.expenshare.model.dto.expense.ShareDto;
import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jsr330")
public interface ExpenseMapper {

    @Mapping(target = "group" , source = "groupId")
    @Mapping(target = "user" , source = "paidBy")
    @Mapping(target = "amount" , source = "amount")
    @Mapping(target = "description" , source = "description")
    @Mapping(target = "expenseSplitType" , source = "splitType")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expenseShareEntities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ExpenseEntity toEntity(CreateExpenseRequest req);

    @Mapping(target = "expenseId" , source = "e.id")
    @Mapping(target = "groupId" , source = "e.group.id")
    @Mapping(target = "paidBy" , source = "e.user.id")
    @Mapping(target = "amount" , source = "e.amount")
    @Mapping(target = "description" , source = "e.description")
    @Mapping(target = "split" , source = "shares")
    @Mapping(target = "createdAt" , source = "e.createdAt")
    ExpenseDto toDto(ExpenseEntity e, List<ShareDto> shares);

    default GroupEntity mapGroup(long groupId) {
        if (groupId == 0) {
            return null;
        }
        GroupEntity group = new GroupEntity();
        group.setId(groupId);
        return group;
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
