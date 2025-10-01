package com.expenshare.event;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

import java.util.Map;


@KafkaClient
public interface KafkaProducerInterface {

    @Topic("user.created")
    void sendUserCreated(String eventID, String occurredAt , Map<String, String> payload);

    @Topic("notification.welcome")
    void sendWelcomeNotification(String eventID, String occurredAt , Map<String, String> payload);

    @Topic("group.created")
    void sendGroupCreated(String eventID, String occurredAt , Map<String, String> payload);

    @Topic("expense.added")
    void sendExpenseAdded(String eventID, String occurredAt , Map<String, String> payload);

    @Topic("settlement.confirmed")
    void sendSettlementConfirmed(String eventID, String occurredAt , Map<String, String> payload);

}
