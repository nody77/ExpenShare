package com.expenshare.model.entity;

import com.expenshare.model.enums.CountryISO;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;
import java.util.List;


import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "UK_USER_EMAIL", columnNames = "email"))
public class UserEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email" , nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "mobile_number", length = 20)
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
    private String mobileNumber;

    @Column(name = "addr_line1", length = 150)
    private String addressLine1;

    @Column(name = "addr_line2", length = 150)
    private String addressLine2;

    @Column(name = "addr_city", length = 80)
    private String addressCity;

    @Column(name = "addr_state", length = 80)
    private String addressState;

    @Column(name = "addr_postal", length = 20)
    private String addressPostal;

    @Enumerated(EnumType.STRING)
    @Column(name = "addr_country", length = 2)
    private CountryISO addressCountry;

    @Column(name = "created_at" , nullable = false)
    private Instant  createdAt;

    // Joining Group Member Entity
    @OneToMany(mappedBy = "user")
    private List<GroupMemberEntity> groupMemberEntities;

    // Joining Expense Entity
    @OneToMany(mappedBy = "user")
    private List<ExpenseEntity> expenseEntities;

    // Joining Expense Shares Entity
    @OneToMany(mappedBy = "user")
    private List<ExpenseShareEntity> expenseShareEntities;

    // Joining Settlements Entity
    @OneToMany(mappedBy = "fromUser")
    private List<SettlementEntity> settlementMade;

    @OneToMany(mappedBy = "toUser")
    private List<SettlementEntity> settlementReceived;


    public UserEntity(String name, String email, String mobileNumber, String addressLine1, String addressLine2, String addressCity, String addressState, String addressPostal, CountryISO addressCountry) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressCity = addressCity;
        this.addressState = addressState;
        this.addressPostal = addressPostal;
        this.addressCountry = addressCountry;
        this.createdAt = Instant.now();
    }

    public UserEntity(){

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressPostal() {
        return addressPostal;
    }

    public void setAddressPostal(String addressPostal) {
        this.addressPostal = addressPostal;
    }

    public CountryISO getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(CountryISO addressCountry) {
        this.addressCountry = addressCountry;
    }

    public Instant   getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant   createdAt) {
        this.createdAt = createdAt;
    }

    public List<GroupMemberEntity> getGroupMemberEntities() {
        return groupMemberEntities;
    }

    public void setGroupMemberEntities(List<GroupMemberEntity> groupMemberEntities) {
        this.groupMemberEntities = groupMemberEntities;
    }

    public List<ExpenseEntity> getExpenseEntities() {
        return expenseEntities;
    }

    public void setExpenseEntities(List<ExpenseEntity> expenseEntities) {
        this.expenseEntities = expenseEntities;
    }

    public List<ExpenseShareEntity> getExpenseShareEntities() {
        return expenseShareEntities;
    }

    public void setExpenseShareEntities(List<ExpenseShareEntity> expenseShareEntities) {
        this.expenseShareEntities = expenseShareEntities;
    }

    public List<SettlementEntity> getSettlementMade() {
        return settlementMade;
    }

    public void setSettlementMade(List<SettlementEntity> settlementMade) {
        this.settlementMade = settlementMade;
    }

    public List<SettlementEntity> getSettlementReceived() {
        return settlementReceived;
    }

    public void setSettlementReceived(List<SettlementEntity> settlementReceived) {
        this.settlementReceived = settlementReceived;
    }


}
