package com.expenshare.model.entity;


import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;


import java.time.Instant;
import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "groups")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at" , nullable = false, length = 100)
    private Instant createdAt;

    //Joining Group Member Entity
    @OneToMany(mappedBy = "group")
    private List<GroupMemberEntity> groupMemberEntity;

    //Joining Expense Entity
    @OneToMany(mappedBy = "group")
    private List<ExpenseEntity> expenseEntities;

    //Joining Settlement Entity
    @OneToMany(mappedBy = "group")
    private List<SettlementEntity> settlement;

    public GroupEntity(String name) {
        this.name = name;
        this.createdAt = Instant.now();
    }
    public GroupEntity(){

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant  getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<GroupMemberEntity> getGroupMemberEntity() {
        return groupMemberEntity;
    }

    public void setGroupMemberEntity(List<GroupMemberEntity> groupMemberEntity) {
        this.groupMemberEntity = groupMemberEntity;
    }

    public List<ExpenseEntity> getExpenseEntities() {
        return expenseEntities;
    }

    public void setExpenseEntities(List<ExpenseEntity> expenseEntities) {
        this.expenseEntities = expenseEntities;
    }

    public List<SettlementEntity> getSettlement() {
        return settlement;
    }

    public void setSettlement(List<SettlementEntity> settlement) {
        this.settlement = settlement;
    }
}
