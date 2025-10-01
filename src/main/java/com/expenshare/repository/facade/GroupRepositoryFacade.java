package com.expenshare.repository.facade;

import com.expenshare.exception.NotFoundException;
import com.expenshare.exception.ValidationException;
import com.expenshare.model.entity.*;
import com.expenshare.model.enums.SettlementStatus;
import com.expenshare.repository.GroupMemberRepository;
import com.expenshare.repository.GroupRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class GroupRepositoryFacade implements GroupRepository, GroupMemberRepository {

    private final EntityManager entityManager;

    public GroupRepositoryFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void createGroupMember(UserEntity user, GroupEntity group) {
        GroupMemberEntity newGroupMember = new GroupMemberEntity(group, user);
        entityManager.persist(newGroupMember);
        entityManager.flush();
    }

    @Override
    @Transactional
    public List<GroupMemberEntity> getGroupMemberByGroupId(GroupEntity group) {
        String query = "SELECT u FROM GroupMemberEntity u WHERE u.group =: group";
        return entityManager.createQuery(query, GroupMemberEntity.class).setParameter("group", group)
                .getResultList();
    }

    @Override
    @Transactional
    public GroupEntity createGroup(@NotBlank String name) {
        GroupEntity newGroup = new GroupEntity(name);
        entityManager.persist(newGroup);
        entityManager.flush();
        return newGroup;
    }

    @Override
    @Transactional
    public GroupEntity getGroupByID(long id) {
        GroupEntity group = entityManager.find(GroupEntity.class, id);
        if(group == null){
            throw new NotFoundException("Group not found");
        }
        return group;
    }

    @Override
    @Transactional
    public void addMembers(Long groupId, List<Long> userIds){

        if(userIds.isEmpty()){
            throw new ValidationException("Members List should not be empty");
        }

        GroupEntity group = getGroupByID(groupId);
        boolean users = usersExist(userIds);

        if(users) {
            for(Long userId: userIds){
                UserEntity user = entityManager.find(UserEntity.class, userId);
                createGroupMember(user, group);
            }
        }
        else{
            throw new NotFoundException("One or more users not found");
        }

    }

    @Transactional
    public boolean usersExist(List<Long> userIds){
        boolean exists = true;
        for(Long userId: userIds){
            UserEntity user = entityManager.find(UserEntity.class, userId);
            if(user == null){
                exists = false;
                return exists;
            }
        }
        return exists;
    }

    @Override
    @Transactional
    public List<SettlementEntity> getSettlementForGroup(Long groupID, SettlementStatus status, Long fromUserId, Long toUserId, int page, int size) {
        StringBuilder jpql = new StringBuilder("SELECT s FROM SettlementEntity s WHERE s.group.id = :groupId");
        Map<String, Object> params = new HashMap<>();

        params.put("groupId", groupID);

        if (fromUserId != null) {
            jpql.append(" AND s.fromUser.id = :fromUserId");
            params.put("fromUserId", fromUserId);
        }

        if (toUserId != null) {
            jpql.append(" AND s.toUser.id = :toUserId");
            params.put("toUserId", toUserId);
        }

        if (status != null) {
            jpql.append(" AND s.status = :status");
            params.put("status", status);
        }

        // Create query
        TypedQuery<SettlementEntity> query = entityManager.createQuery(jpql.toString(), SettlementEntity.class);
        params.forEach(query::setParameter);

        // Pagination
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public BigDecimal calculateNetBalanceOfUser(GroupMemberEntity groupMember){

        // net(user) = Σ share_amount(user)  –  Σ settlementsPaidBy(user) + Σ settlementsReceivedBy(user)

        BigDecimal totalShareAmountPerUser = BigDecimal.valueOf(0.0);
        BigDecimal totalSettlementsPaidByUser = BigDecimal.valueOf(0.0);
        BigDecimal totalSettlementsReceivedByUser = BigDecimal.valueOf(0.0);

        // Σ share_amount(user)
        for(ExpenseShareEntity expenseShare: groupMember.getUser().getExpenseShareEntities()){
            totalShareAmountPerUser = totalShareAmountPerUser.add(expenseShare.getShareAmount());
        }

        // Σ settlementsPaidBy(user)
        for(SettlementEntity settlement: groupMember.getUser().getSettlementMade()){
            totalSettlementsPaidByUser = totalSettlementsPaidByUser.add(settlement.getAmount());
        }

        // Σ settlementsReceivedBy(user)
        for(SettlementEntity settlement: groupMember.getUser().getSettlementReceived()){
            totalSettlementsReceivedByUser = totalSettlementsReceivedByUser.add(settlement.getAmount());
        }

        BigDecimal totalBalancePerUser = totalShareAmountPerUser.subtract(totalSettlementsPaidByUser);
        totalBalancePerUser = totalBalancePerUser.add(totalSettlementsReceivedByUser);

        return totalBalancePerUser;
    }


}
