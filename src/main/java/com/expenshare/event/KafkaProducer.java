package com.expenshare.event;


import com.expenshare.model.dto.expense.ExpenseDto;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.model.dto.user.UserDto;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Singleton
public class KafkaProducer {

    public final KafkaProducerInterface kafkaProducer;

    public KafkaProducer(KafkaProducerInterface kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void sendUserCreated(UserDto user){
        Map<String, String> payload = new HashMap<>();
        payload.put("userId" , Long.toString(user.getId()));
        kafkaProducer.sendUserCreated(UUID.randomUUID().toString(), user.getCreatedAt().toString(), payload);
    }


    public void sendUserWelcomeNotification(UserDto user){
        Map<String, String> payload = new HashMap<>();
        payload.put("target type" , "user");
        payload.put("message" , "Welcome");
        kafkaProducer.sendWelcomeNotification(UUID.randomUUID().toString(), user.getCreatedAt().toString(), payload);
    }


    public void sendGroupWelcomeNotification(GroupDto group){
        Map<String, String> payload = new HashMap<>();
        payload.put("target type" , "user");
        payload.put("message" , "Welcome");
        kafkaProducer.sendWelcomeNotification(UUID.randomUUID().toString(), group.getCreatedAt().toString(), payload);
    }


    public void sendGroupCreated(GroupDto group){
        Map<String, String> payload = new HashMap<>();
        payload.put("groupId " , Long.toString(group.getId()));
        kafkaProducer.sendGroupCreated(UUID.randomUUID().toString(), group.getCreatedAt().toString(), payload);
    }

    public void sendExpenseCreated(ExpenseDto expense){
        Map<String, String> payload = new HashMap<>();
        payload.put("expenseId" , Long.toString(expense.getExpenseId()));
        payload.put("groupId" , Long.toString(expense.getGroupId()));
        payload.put("paidBy" , Long.toString(expense.getPaidBy()));
        payload.put("amount" , expense.getAmount().toString());
        payload.put("description " , expense.getDescription());
        kafkaProducer.sendExpenseAdded(UUID.randomUUID().toString(), expense.getCreatedAt().toString(), payload);
    }

    public void sendSettlementConfirmed(SettlementDto settlement){
        Map<String, String> payload = new HashMap<>();
        payload.put("expenseId" , Long.toString(settlement.getSettlementId()));
        payload.put("groupId" , Long.toString(settlement.getGroupId()));
        payload.put("fromUserId" , Long.toString(settlement.getFromUserId()));
        payload.put("toUserId" , Long.toString(settlement.getToUserId()));
        payload.put("amount  " , settlement.getAmount().toString());
        kafkaProducer.sendSettlementConfirmed(UUID.randomUUID().toString(), settlement.getCreatedAt().toString(), payload);

    }
}
