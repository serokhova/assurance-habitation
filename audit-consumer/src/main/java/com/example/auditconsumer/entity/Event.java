package com.example.auditconsumer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant un événement JMS consommé et persisté.
 * Stockée dans le schéma "messaging".
 */
@Entity
@Table(name = "event", schema = "messaging")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "consumer_received_at", nullable = false)
    private LocalDateTime consumerReceivedAt;

    public Event() {}

    public Event(String eventType, String payload, LocalDateTime eventTime, LocalDateTime consumerReceivedAt) {
        this.eventType = eventType;
        this.payload = payload;
        this.eventTime = eventTime;
        this.consumerReceivedAt = consumerReceivedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public LocalDateTime getEventTime() { return eventTime; }
    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }

    public LocalDateTime getConsumerReceivedAt() { return consumerReceivedAt; }
    public void setConsumerReceivedAt(LocalDateTime consumerReceivedAt) { this.consumerReceivedAt = consumerReceivedAt; }
}
