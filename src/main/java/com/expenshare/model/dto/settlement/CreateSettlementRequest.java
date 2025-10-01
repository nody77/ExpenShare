package com.expenshare.model.dto.settlement;

import com.expenshare.model.enums.SettlementMethod;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Introspected
@Serdeable
public class CreateSettlementRequest {

    @NotNull
    private long groupId;

    @NotNull
    private long fromUserId;

    @NotNull
    private long toUserId;

    @NotNull
    @Positive
    private BigDecimal amount;

    private SettlementMethod method;

    private String note;

    private String reference;

    private boolean enforceOwedLimit;

    public CreateSettlementRequest(long groupId, long fromUserId, long toUserId, BigDecimal amount) {
        this.groupId = groupId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isEnforceOwedLimit() {
        return enforceOwedLimit;
    }

    public void setEnforceOwedLimit(boolean enforceOwedLimit) {
        this.enforceOwedLimit = enforceOwedLimit;
    }
}
