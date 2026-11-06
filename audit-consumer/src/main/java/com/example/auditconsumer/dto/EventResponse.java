package com.example.auditconsumer.dto;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String eventType,
        String payload,
        LocalDateTime eventTime,
        LocalDateTime consumerReceivedAt
) {}
