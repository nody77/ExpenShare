package com.expenshare.model.mapper;

import com.expenshare.model.dto.settlement.CreateSettlementRequest;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.SettlementEntity;
import com.expenshare.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jsr330")
public interface SettlementMapper {

    @Mapping(target = "group", source = "groupId")
    @Mapping(target = "fromUser", source = "fromUserId")
    @Mapping(target = "toUser", source = "toUserId")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "method", expression = "java(req.getMethod() != null ? req.getMethod() : com.expenshare.model.enums.SettlementMethod.OTHER)")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "status", expression = "java(com.expenshare.model.enums.SettlementStatus.PENDING)")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "confirmedAt", ignore = true)
    SettlementEntity toEntity(CreateSettlementRequest req);

    @Mapping(target = "settlementId", source = "e.id")
    @Mapping(target = "groupId", source = "e.group.id")
    @Mapping(target = "fromUserId", source = "e.fromUser.id")
    @Mapping(target = "toUserId", source = "e.toUser.id")
    @Mapping(target = "amount", source = "e.amount")
    @Mapping(target = "method", source = "e.method")
    @Mapping(target = "note", source = "e.note")
    @Mapping(target = "status", source = "e.status")
    @Mapping(target = "createdAt", source = "e.createdAt")
    SettlementDto toDto(SettlementEntity e);

    default GroupEntity mapGroup(long groupId) {
        if (groupId == 0) {
            return null;
        }
        GroupEntity group = new GroupEntity();
        group.setId(groupId);
        return group;
    }

    default UserEntity mapUser(long userId) {
        if (userId == 0) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(userId);
        return user;
    }
}
