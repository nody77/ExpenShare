package com.expenshare.controller;

import com.expenshare.model.dto.group.AddMembersRequest;
import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.enums.SettlementStatus;
import com.expenshare.service.GroupService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.validation.Valid;

import java.util.Map;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/api/groups")
public class GroupController {


    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Get("/{groupId}")
    GroupDto getGroup(Long groupId){
        return groupService.getGroupByID(groupId);
    }

    @Get("/{groupId}/balances")
    Map<String, Object> getGroupBalances(Long groupId){
        return groupService.getGroupBalances(groupId);
    }

    @Get("/{groupId}/settlements")
    Map<String, Object> getGroupSettlements(Long groupId, @QueryValue @Nullable SettlementStatus status,
                                            @QueryValue @Nullable Long fromUserId, @QueryValue @Nullable Long toUserId,
                                            @QueryValue(defaultValue = "0") @Nullable Integer page,
                                            @QueryValue(defaultValue = "20") @Nullable Integer size){
        return groupService.getGroupSettlements(groupId, status, fromUserId, toUserId, page, size);
    }

    @Post
    HttpResponse<GroupDto> createUser(@Body @Valid CreateGroupRequest groupRequest) {
        GroupDto newGroup = groupService.createGroup(groupRequest);
        return HttpResponse.created(newGroup);
    }


    @Post("/{groupId}/members")
    HttpResponse<AddMembersRequest> addMembers(Long groupId,@Body @Valid AddMembersRequest addMembersRequest) {
        AddMembersRequest addedMembers = groupService.addMembersIntoGroup(groupId, addMembersRequest.getMembersAdded());
        return HttpResponse.created(addedMembers);
    }
}
