package com.expenshare.model.entity;


import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.time.Instant;

import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "group_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"group_id", "user_id"})
})
public class GroupMemberEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "added_at" , nullable = false)
    private Instant addedAt;


    public GroupMemberEntity(GroupEntity group, UserEntity user) {
        this.group = group;
        this.user = user;
        this.addedAt = Instant.now();
    }

    public GroupMemberEntity(){

    }
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

    public Instant  getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Instant  addedAt) {
        this.addedAt = addedAt;
    }


}
