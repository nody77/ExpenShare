package com.expenshare.repository.facade;

import com.expenshare.exception.ConflictException;
import com.expenshare.exception.NotFoundException;
import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.SettlementEntity;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.model.enums.SettlementMethod;
import com.expenshare.model.enums.SettlementStatus;
import com.expenshare.repository.SettlementRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Singleton
public class SettlementRepositoryFacade implements SettlementRepository {

    private final EntityManager entityManager;

    public SettlementRepositoryFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public BigDecimal owedAmount(Long groupId, Long fromUserId, Long toUserId) {
        GroupEntity group = entityManager.find(GroupEntity.class, groupId);
        UserEntity fromUser = entityManager.find(UserEntity.class, fromUserId);
        UserEntity toUser = entityManager.find(UserEntity.class, toUserId);

        if(Objects.equals(fromUserId, toUserId)){
            throw new ConflictException("From User can not be the same to User To");
        }

        String query = "SELECT u FROM SettlementEntity u WHERE u.group =: group AND u.fromUser =: fromUser AND u.toUser =: toUser";
        SettlementEntity settlement = entityManager.createQuery(query, SettlementEntity.class).setParameter("group", group).setParameter("fromUser", fromUser)
                .setParameter("toUser", toUser).getSingleResult();

        return settlement.getAmount();
    }

    @Override
    @Transactional
    public SettlementEntity createConfirmed(SettlementEntity e) {

        if(e.getStatus().equals(SettlementStatus.CONFIRMED)){
            throw new ConflictException("Already confirmed");
        }

        entityManager.createQuery("UPDATE SettlementEntity s SET s.status = :status, s.confirmedAt =: confirmedAt where s.id = :id")
                .setParameter("status", SettlementStatus.CONFIRMED)
                .setParameter("confirmedAt", Instant.now())
                .setParameter("id", e.getId())
                .executeUpdate();

        return getSettlementById(e.getId());
    }

    @Override
    @Transactional
    public SettlementEntity createCancel(SettlementEntity e) {
        if(!e.getStatus().equals(SettlementStatus.PENDING)){
            throw new ConflictException("Only pending settlements can be canceled");
        }

        entityManager.createQuery("UPDATE SettlementEntity s SET s.status = :status where s.id = :id")
                .setParameter("status", SettlementStatus.CANCELED)
                .setParameter("id", e.getId())
                .executeUpdate();

        return getSettlementById(e.getId());
    }

    @Override
    @Transactional
    public SettlementEntity getSettlementById(long settlementId) {
        SettlementEntity settlement = entityManager.find(SettlementEntity.class, settlementId);
        if(settlement == null){
            throw new NotFoundException("Settlement is not found");
        }
        return settlement;
    }

    @Override
    @Transactional
    public SettlementEntity createSettlement(GroupEntity group, UserEntity fromUser, UserEntity toUser, BigDecimal amount, SettlementMethod method, String note, String reference, SettlementStatus status) {
        if(Objects.equals(fromUser, toUser)){
            throw new ConflictException("From User can not be the same to User To");
        }

        SettlementEntity newSettlement = new SettlementEntity(group, fromUser, toUser, amount, method, note, reference, status);
        entityManager.persist(newSettlement);
        entityManager.flush();
        return newSettlement;
    }
}
