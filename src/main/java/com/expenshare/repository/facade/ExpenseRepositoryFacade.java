package com.expenshare.repository.facade;

import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.model.enums.ExpenseSplitType;
import com.expenshare.repository.ExpenseRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;

@Singleton
public class ExpenseRepositoryFacade implements ExpenseRepository {

    private final EntityManager entityManager;

    public ExpenseRepositoryFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    @Transactional
    public ExpenseEntity createExpense(GroupEntity group, UserEntity userPaid, BigDecimal amount, String description, ExpenseSplitType expenseSplitType) {
        ExpenseEntity newExpense = new ExpenseEntity(group, userPaid, amount, description, expenseSplitType);
        entityManager.persist(newExpense);
        entityManager.flush();
        return newExpense;
    }
}
