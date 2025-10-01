package com.expenshare.repository;

import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.SettlementEntity;
import com.expenshare.model.enums.SettlementStatus;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface GroupRepository {

    GroupEntity createGroup(@NotBlank String name);

    GroupEntity getGroupByID(long id);

    boolean usersExist(List<Long> userIds);

    List<SettlementEntity> getSettlementForGroup(Long groupID, SettlementStatus status, Long fromUserId, Long toUserId,
                                                 int page, int size);

}
