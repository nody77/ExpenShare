package com.expenshare.service;

import com.expenshare.event.KafkaProducer;
import com.expenshare.model.dto.expense.ShareDto;
import com.expenshare.model.dto.group.AddMembersRequest;
import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.GroupMemberEntity;
import com.expenshare.model.entity.SettlementEntity;
import com.expenshare.model.enums.SettlementStatus;
import com.expenshare.model.mapper.GroupMapper;
import com.expenshare.model.mapper.SettlementMapper;
import com.expenshare.repository.facade.GroupRepositoryFacade;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Singleton
public class GroupService {

    private final GroupRepositoryFacade groupRepositoryFacade;
    private final SettlementMapper settlementMapper;
    private final GroupMapper groupMapper;
    private final KafkaProducer kafkaProducer;

    public GroupService(GroupRepositoryFacade groupRepositoryFacade, SettlementMapper settlementMapper, GroupMapper groupMapper, KafkaProducer kafkaProducer) {
        this.groupRepositoryFacade = groupRepositoryFacade;
        this.settlementMapper = settlementMapper;
        this.groupMapper = groupMapper;
        this.kafkaProducer = kafkaProducer;
    }

    public GroupDto getGroupByID(long groupId) {
        GroupEntity group = groupRepositoryFacade.getGroupByID(groupId);

        List<GroupMemberEntity> groupMembers = groupRepositoryFacade.getGroupMemberByGroupId(group);

        List<Long> memberIds = new ArrayList<>();
        for (GroupMemberEntity groupMember : groupMembers) {
            memberIds.add(groupMember.getUser().getId());
        }
        return groupMapper.toDto(group, memberIds);
    }

    public GroupDto createGroup(CreateGroupRequest groupRequest) {
        GroupEntity group = groupMapper.toEntity(groupRequest);
        GroupEntity newGroup = groupRepositoryFacade.createGroup(group.getName());
        List<Long> memberIds = new ArrayList<>();
        for (Long userId : groupRequest.getMembers()) {
            memberIds.add(userId);
        }
        groupRepositoryFacade.addMembers(newGroup.getId(), memberIds);
        GroupDto newGroupDto = groupMapper.toDto(newGroup, memberIds);
        kafkaProducer.sendGroupCreated(newGroupDto);
        kafkaProducer.sendGroupWelcomeNotification(newGroupDto);
        return newGroupDto;
    }

    @Transactional
    public AddMembersRequest addMembersIntoGroup (Long groupID, List < Long > memberAdded){

        groupRepositoryFacade.addMembers(groupID, memberAdded);
        GroupEntity updatedGroup = groupRepositoryFacade.getGroupByID(groupID);
        List<Long> updatedMemberIds = new ArrayList<>();
        for (GroupMemberEntity groupMember : updatedGroup.getGroupMemberEntity()) {
            updatedMemberIds.add(groupMember.getUser().getId());
        }
        AddMembersRequest newResponse = new AddMembersRequest(updatedMemberIds);
        newResponse.setMembersAdded(memberAdded);
        newResponse.setGroupId(groupID);
        return newResponse;
    }

    @Transactional
    public Map<String, Object> getGroupBalances(long groupID){
        Map<String, Object> response = new LinkedHashMap<>();

        GroupEntity group = groupRepositoryFacade.getGroupByID(groupID);
        response.put("groupId" , group.getId());

        List<ShareDto> totalBalanceGroup = new ArrayList<>();
        for(GroupMemberEntity groupMember:group.getGroupMemberEntity()){

            BigDecimal totalBalancePerUser = groupRepositoryFacade.calculateNetBalanceOfUser(groupMember);
            ShareDto share = new ShareDto(groupMember.getUser().getId(), totalBalancePerUser);
            totalBalanceGroup.add(share);
        }

        response.put("balances" , totalBalanceGroup);
        response.put("calculatedAt", Instant.now());

        return response;
    }

    @Transactional
    public Map<String, Object> getGroupSettlements(Long groupID, SettlementStatus status, Long fromUserId,  Long toUserId,
                                                    int page,  int size){
        Map<String, Object> response = new LinkedHashMap<>();
        GroupEntity group = groupRepositoryFacade.getGroupByID(groupID);
        response.put("groupId", group.getId());
        List<SettlementEntity> settlements = groupRepositoryFacade.getSettlementForGroup(groupID, status, fromUserId, toUserId, page, size);

        List<SettlementDto> settlementsDto = new ArrayList<>();
        for (SettlementEntity settlement: settlements){
            settlementsDto.add(settlementMapper.toDto(settlement));
        }

        response.put("items", settlementsDto);
        response.put("page", page);
        response.put("size", size);
        response.put("total", settlements.size());
        return response;

    }

}
