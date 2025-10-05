package com.expenshare.model.dto.expense;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Introspected
@Serdeable
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ExpenseDto {
    private long expenseId;
    private long groupId;
    private long paidBy;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotBlank
    private String description;
    private List<ShareDto> split;
    private Instant createdAt;
    private Instant calculatedAt;

    public ExpenseDto(){}

    public ExpenseDto(long expenseId, long groupId, long paidBy, BigDecimal amount, String description) {
        this.expenseId = expenseId;
        this.groupId = groupId;
        this.paidBy = paidBy;
        this.amount = amount;
        this.description = description;
    }

    public ExpenseDto(long groupId, List<ShareDto> split, Instant calculatedAt) {
        this.groupId = groupId;
        this.split = split;
        this.calculatedAt = calculatedAt;
    }

    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
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

    public List<ShareDto> getSplit() {
        return split;
    }

    public void setSplit(List<ShareDto> split) {
        this.split = split;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(Instant calculatedAt) {
        this.calculatedAt = calculatedAt;
    }


}
