package com.expenshare.model.dto.settlement;

import com.expenshare.model.enums.SettlementMethod;
import com.expenshare.model.enums.SettlementStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;

@Introspected
@Serdeable

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SettlementDto {

    private long settlementId;
    private long groupId;
    private long fromUserId;
    private long toUserId;
    private BigDecimal amount;
    private SettlementMethod method;
    private String note;
    private SettlementStatus status;
    private Instant createdAt;
    private Instant confirmedAt;

    public SettlementDto(){}

    public SettlementDto(long groupId, long fromUserId, long toUserId, BigDecimal amount) {
        this.groupId = groupId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
    }

    public SettlementDto(long settlementId, SettlementStatus status, Instant confirmedAt) {
        this.settlementId = settlementId;
        this.status = status;
        this.confirmedAt = confirmedAt;
    }

    public SettlementDto(long settlementId,long fromUserId, long toUserId, BigDecimal amount, SettlementStatus status){
        this.settlementId = settlementId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.status = status;
    }


    public long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(long settlementId) {
        this.settlementId = settlementId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public SettlementMethod getMethod() {
        return method;
    }

    public void setMethod(SettlementMethod method) {
        this.method = method;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public SettlementStatus getStatus() {
        return status;
    }

    public void setStatus(SettlementStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Instant confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}
