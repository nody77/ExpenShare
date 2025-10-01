package com.expenshare.repository;

import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.GroupMemberEntity;
import com.expenshare.model.entity.UserEntity;

import java.util.List;


public interface GroupMemberRepository {

    void createGroupMember(UserEntity user, GroupEntity group);

    List<GroupMemberEntity> getGroupMemberByGroupId(GroupEntity group);

    void addMembers(Long groupId, List<Long> userIds);
}
