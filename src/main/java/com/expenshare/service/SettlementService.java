package com.expenshare.service;

import com.expenshare.event.KafkaProducer;
import com.expenshare.exception.ValidationException;
import com.expenshare.model.dto.settlement.CreateSettlementRequest;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.GroupMemberEntity;
import com.expenshare.model.entity.SettlementEntity;
import com.expenshare.model.mapper.SettlementMapper;
import com.expenshare.repository.facade.GroupRepositoryFacade;
import com.expenshare.repository.facade.SettlementRepositoryFacade;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.List;

@Singleton
public class SettlementService {

    private final KafkaProducer kafkaProducer;
    private final SettlementRepositoryFacade settlementRepositoryFacade;
    private final SettlementMapper settlementMapper;

    public SettlementService(KafkaProducer kafkaProducer, SettlementRepositoryFacade settlementRepositoryFacade, SettlementMapper settlementMapper) {
        this.kafkaProducer = kafkaProducer;
        this.settlementRepositoryFacade = settlementRepositoryFacade;
        this.settlementMapper = settlementMapper;
    }

    public SettlementDto createSettlement(CreateSettlementRequest req){
        if(req.isEnforceOwedLimit()){
            validateOwnedAmountSettlement(req);
        }
        SettlementEntity settlement = settlementMapper.toEntity(req);
        SettlementEntity newSettlement = settlementRepositoryFacade.createSettlement(settlement.getGroup(), settlement.getFromUser(),settlement.getToUser(),settlement.getAmount(),settlement.getMethod(),settlement.getNote(),settlement.getReference(),settlement.getStatus());
        return settlementMapper.toDto(newSettlement);
    }

    public SettlementDto settlementConfirm(long settlementId){

        SettlementEntity settlement = settlementRepositoryFacade.getSettlementById(settlementId);
        SettlementEntity settlementConfirmed = settlementRepositoryFacade.createConfirmed(settlement);
        SettlementDto confirmedSettlementDto = settlementMapper.toDto(settlementConfirmed);
        kafkaProducer.sendSettlementConfirmed(confirmedSettlementDto);

        return new SettlementDto(settlement.getId(), confirmedSettlementDto.getStatus(), confirmedSettlementDto.getConfirmedAt());
    }

    public SettlementDto settlementCancel(long settlementId){

        SettlementEntity settlement = settlementRepositoryFacade.getSettlementById(settlementId);
        SettlementEntity settlementCancel = settlementRepositoryFacade.createCancel(settlement);
        SettlementDto response = new SettlementDto();
        response.setSettlementId(settlementCancel.getId());
        response.setStatus(settlementCancel.getStatus());

        return response;
    }

    private void validateOwnedAmountSettlement(CreateSettlementRequest req){
        BigDecimal ownAmount = settlementRepositoryFacade.owedAmount(req.getGroupId(), req.getFromUserId(), req.getToUserId());
        if(ownAmount.compareTo(req.getAmount()) < 0){
            throw new ValidationException("Cannot settle more than owed");
        }
    }
}
