package com.expenshare.repository;

import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.SettlementEntity;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.model.enums.SettlementMethod;
import com.expenshare.model.enums.SettlementStatus;

import java.math.BigDecimal;

public interface SettlementRepository {

    BigDecimal owedAmount(Long groupId, Long fromUserId, Long toUserId);
    SettlementEntity createConfirmed(SettlementEntity e);
    SettlementEntity createCancel(SettlementEntity e);
    SettlementEntity getSettlementById(long settlementId);
    SettlementEntity createSettlement(GroupEntity group, UserEntity fromUser, UserEntity toUser, BigDecimal  amount, SettlementMethod method, String note, String reference, SettlementStatus status);
}
