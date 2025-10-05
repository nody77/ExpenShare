package com.expenshare.model.dto.expense;

import com.expenshare.model.enums.ExpenseSplitType;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

@Introspected
@Serdeable
public class CreateExpenseRequest {

    @NotNull
    private long groupId;

    @NotNull
    private long paidBy;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private String description;

    @NotNull
    private ExpenseSplitType splitType;
    private List<ShareDto> split;

    public CreateExpenseRequest(long groupId, long paidBy, BigDecimal amount, String description, ExpenseSplitType splitType) {
        this.groupId = groupId;
        this.paidBy = paidBy;
        this.amount = amount;
        this.description = description;
        this.splitType = splitType;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(long paidBy) {
        this.paidBy = paidBy;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExpenseSplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(ExpenseSplitType splitType) {
        this.splitType = splitType;
    }

    public List<ShareDto> getSplit() {
        return split;
    }

    public void setSplit(List<ShareDto> split) {
        this.split = split;
    }
}
