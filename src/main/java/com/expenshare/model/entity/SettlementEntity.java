package com.expenshare.model.entity;


import com.expenshare.model.enums.SettlementMethod;
import com.expenshare.model.enums.SettlementStatus;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "settlements")
public class SettlementEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private UserEntity fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private UserEntity toUser;

    @Column(name = "amount" , nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING) // Or EnumType.ORDINAL
    @Column(name = "method")
    private SettlementMethod method;

    @Column(name = "note")
    private String note;

    @Column(name = "reference", length = 64)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SettlementStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant  createdAt;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();  // UTC timestamp
    }

    public SettlementEntity(GroupEntity group, UserEntity fromUser, UserEntity toUser, BigDecimal  amount, SettlementMethod method, String note, String reference, SettlementStatus status) {
        this.group = group;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
        this.method = method;
        this.note = note;
        this.reference = reference;
        this.status = status;
    }
    public SettlementEntity(){}

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

    public UserEntity getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserEntity fromUser) {
        this.fromUser = fromUser;
    }

    public UserEntity getToUser() {
        return toUser;
    }

    public void setToUser(UserEntity toUser) {
        this.toUser = toUser;
    }

    public BigDecimal  getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal  amount) {
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

    public SettlementStatus getStatus() {
        return status;
    }

    public void setStatus(SettlementStatus status) {
        this.status = status;
    }

    public Instant  getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant  createdAt) {
        this.createdAt = createdAt;
    }

    public Instant  getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Instant  confirmedAt) {
        this.confirmedAt = confirmedAt;
    }


}
