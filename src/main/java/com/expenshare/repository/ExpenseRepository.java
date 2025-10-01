package com.expenshare.repository;

import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.model.enums.ExpenseSplitType;

import java.math.BigDecimal;


public interface ExpenseRepository {

    ExpenseEntity createExpense(GroupEntity group, UserEntity userPaid, BigDecimal amount, String description, ExpenseSplitType expenseSplitType);
}
