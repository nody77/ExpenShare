package com.expenshare.model.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "expense_shares")
public class ExpenseShareEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "expense_id")
    private ExpenseEntity expense;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "share_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal shareAmount;

    public ExpenseShareEntity(ExpenseEntity expense, UserEntity user, BigDecimal  shareAmount) {
        this.expense = expense;
        this.user = user;
        this.shareAmount = shareAmount;
    }

    public ExpenseShareEntity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExpenseEntity getExpense() {
        return expense;
    }

    public void setExpense(ExpenseEntity expense) {
        this.expense = expense;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BigDecimal  getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal  shareAmount) {
        this.shareAmount = shareAmount;
    }


}
