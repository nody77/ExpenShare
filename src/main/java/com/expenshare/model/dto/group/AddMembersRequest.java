package com.expenshare.model.dto.group;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Introspected
@Serdeable
public class AddMembersRequest {

    private Long groupId;
    private List<Long> membersAdded;
    private int totalMember;

    public AddMembersRequest(List<Long> membersAdded) {
        this.membersAdded = membersAdded;
        this.totalMember = this.membersAdded.size();
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getMembersAdded() {
        return membersAdded;
    }

    public void setMembersAdded(List<Long> membersAdded) {
        this.membersAdded = membersAdded;
    }

    public int getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(int totalMember) {
        this.totalMember = totalMember;
    }
}
