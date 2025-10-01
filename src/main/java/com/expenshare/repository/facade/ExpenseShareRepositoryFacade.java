package com.expenshare.repository.facade;

import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.model.entity.ExpenseShareEntity;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.repository.ExpenseShareRepository;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;


import java.math.BigDecimal;


@Singleton
public class ExpenseShareRepositoryFacade implements ExpenseShareRepository {

    private final EntityManager entityManager;

    public ExpenseShareRepositoryFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public ExpenseShareEntity createExpenseShare(ExpenseEntity expense, UserEntity user, BigDecimal shareAmount) {
        ExpenseShareEntity newExpenseShare = new ExpenseShareEntity(expense, user, shareAmount);
        entityManager.persist(newExpenseShare);
        entityManager.flush();
        return newExpenseShare;
    }

}
