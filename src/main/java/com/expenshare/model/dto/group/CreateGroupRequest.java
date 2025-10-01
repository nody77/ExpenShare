package com.expenshare.model.dto.group;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Introspected
@Serdeable
public class CreateGroupRequest {

    @NotBlank
    private String name;

    @NotNull
    private List<Long> members;

    public CreateGroupRequest(String name, List<Long> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getMembers() {
        return members;
    }

    public void setMembers(List<Long> members) {
        this.members = members;
    }
}
