package com.expenshare.model.mapper;

import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.entity.GroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jsr330")
public interface GroupMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groupMemberEntity", ignore = true)
    @Mapping(target = "expenseEntities", ignore = true)
    @Mapping(target = "settlement", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    GroupEntity toEntity(CreateGroupRequest req);

    @Mapping(target = "name", source = "e.name")
    @Mapping(target = "createdAt", source = "e.createdAt")
    @Mapping(target = "members", source = "memberIds")
    GroupDto toDto(GroupEntity e, List<Long> memberIds);




}
