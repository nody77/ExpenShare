package com.expenshare.controller;

import com.expenshare.model.dto.expense.ExpenseDto;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.Map;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/api/groups")
@Tag(name = "Groups", description = "Operations related to groups")
public class GroupController {


    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Get("/{groupId}")
    @Operation(
            summary = "Get an Existing Group",
            description = "Get an Existing Group using group's ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Group is found successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GroupDto.class)
            )
    )
    HttpResponse<GroupDto> getGroup(Long groupId){
        return HttpResponse.ok(groupService.getGroupByID(groupId));
    }



    @Get("/{groupId}/balances")
    @Operation(
            summary = "Get Group's Members Balances",
            description = "Retrieves the balances of each member in a group by group ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Group's balances returned successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseDto.class)
            )
    )
    HttpResponse<ExpenseDto> getGroupBalances(Long groupId){
        return HttpResponse.ok(groupService.getGroupBalances(groupId));
    }

    @Get("/{groupId}/settlements")
    @Operation(
            summary = "Get Group's Settlements",
            description = "Retrieves all settlements done in a group by group ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Group's Settlements returned successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GroupDto.class)
            )
    )
    HttpResponse<GroupDto> getGroupSettlements(Long groupId, @QueryValue @Nullable SettlementStatus status,
                                            @QueryValue @Nullable Long fromUserId, @QueryValue @Nullable Long toUserId,
                                            @QueryValue(defaultValue = "0") @Nullable Integer page,
                                            @QueryValue(defaultValue = "20") @Nullable Integer size){
        return HttpResponse.ok(groupService.getGroupSettlements(groupId, status, fromUserId, toUserId, page, size));
    }

    @Post
    @Operation(
            summary = "Create a new Group",
            description = "Creates an Group and returns the created Group object."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Group successfully created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GroupDto.class)
            )
    )
    HttpResponse<GroupDto> createGroup(@Body @Valid CreateGroupRequest groupRequest) {
        GroupDto newGroup = groupService.createGroup(groupRequest);
        return HttpResponse.created(newGroup);
    }


    @Post("/{groupId}/members")
    @Operation(
            summary = "Add Members to an Existing Group",
            description = "Add Members to and Existing Group using Group's ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Members are successfully added",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AddMembersRequest.class)
            )
    )
    HttpResponse<AddMembersRequest> addMembers(Long groupId,@Body @Valid AddMembersRequest addMembersRequest) {
        AddMembersRequest addedMembers = groupService.addMembersIntoGroup(groupId, addMembersRequest.getMembersAdded());
        return HttpResponse.ok(addedMembers);
    }
}
