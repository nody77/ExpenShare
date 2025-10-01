package com.expenshare.model.entity;


import com.expenshare.model.enums.ExpenseSplitType;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "expenses")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private UserEntity user;

    @Column(name = "amount" , nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "split_type")
    private ExpenseSplitType expenseSplitType;

    @Column(name = "created_at" , nullable = false)
    private Instant createdAt;

    // Joining Expense Shares Entity
    @OneToMany(mappedBy = "expense" , fetch = FetchType.LAZY)
    private List<ExpenseShareEntity> expenseShareEntities;


    public ExpenseEntity(GroupEntity group, UserEntity user, BigDecimal  amount, String description, ExpenseSplitType expenseSplitType) {
        this.group = group;
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.expenseSplitType = expenseSplitType;
        this.createdAt = Instant.now();
    }
    public ExpenseEntity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BigDecimal  getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal  amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ExpenseSplitType getExpenseSplitType() {
        return expenseSplitType;
    }

    public void setExpenseSplitType(ExpenseSplitType expenseSplitType) {
        this.expenseSplitType = expenseSplitType;
    }

    public List<ExpenseShareEntity> getExpenseShareEntities() {
        return expenseShareEntities;
    }

    public void setExpenseShareEntities(List<ExpenseShareEntity> expenseShareEntities) {
        this.expenseShareEntities = expenseShareEntities;
    }
}
