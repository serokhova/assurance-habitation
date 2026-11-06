package com.example.auditconsumer.messaging;

import com.example.auditconsumer.entity.Event;
import com.example.auditconsumer.repository.EventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event-driven Consumer pattern: automatically reacts to incoming JMS messages
 * via @JmsListener and records each event into messaging.event.
 */
@Component
public class JmsEventConsumer {

    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public JmsEventConsumer(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @JmsListener(destination = "user.created.queue")
    public void onUserCreated(Message message) {
        persistEvent(message, "USER_CREATED");
    }

    @JmsListener(destination = "contract.created.queue")
    public void onContractCreated(Message message) {
        persistEvent(message, "CONTRACT_CREATED");
    }

    @JmsListener(destination = "claim.created.queue")
    public void onClaimCreated(Message message) {
        persistEvent(message, "CLAIM_CREATED");
    }

    private void persistEvent(Message message, String expectedEventType) {
        try {
            String json = ((TextMessage) message).getText();
            JsonNode root = objectMapper.readTree(json);

            String eventType = root.has("eventType") ? root.get("eventType").asText() : expectedEventType;
            LocalDateTime eventTime = LocalDateTime.now();
            if (root.has("eventTime") && !root.get("eventTime").isNull()) {
                try {
                    eventTime = objectMapper.treeToValue(root.get("eventTime"), LocalDateTime.class);
                } catch (Exception ignored) {
                    eventTime = LocalDateTime.now();
                }
            }

            Event event = new Event(eventType, json, eventTime, LocalDateTime.now());
            eventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process incoming JMS message: " + expectedEventType, e);
        }
    }
}
