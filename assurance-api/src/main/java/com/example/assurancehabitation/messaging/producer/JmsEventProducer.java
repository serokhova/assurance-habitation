package com.example.assurancehabitation.messaging.producer;

import com.example.assurancehabitation.messaging.EventMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Message pattern: publishes structured business events onto JMS queues.
 * Message Translator pattern: translates domain entities/DTOs into JSON String messages.
 */
@Component
public class JmsEventProducer {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public JmsEventProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Publishes an event to the specified queue.
     * Message Translator: translates the payload into an EventMessage JSON string.
     *
     * @param destination JMS queue name
     * @param eventType   event type identifier (e.g. USER_CREATED)
     * @param payload     business domain payload to serialize
     */
    public void publish(String destination, String eventType, Object payload) {
        EventMessage message = new EventMessage(eventType, payload, LocalDateTime.now());
        try {
            String json = objectMapper.writeValueAsString(message);
            jmsTemplate.convertAndSend(destination, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize JMS event message: " + eventType, e);
        }
    }
}
