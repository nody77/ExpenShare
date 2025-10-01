package com.expenshare.repository;

import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.model.entity.ExpenseShareEntity;
import com.expenshare.model.entity.UserEntity;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseShareRepository {

    ExpenseShareEntity createExpenseShare(ExpenseEntity expense, UserEntity user, BigDecimal shareAmount);

}
