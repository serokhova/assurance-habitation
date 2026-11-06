package com.example.assurancehabitation.messaging;

import java.time.LocalDateTime;

/**
 * Message Translator pattern : objet de transfert structuré
 * représentant un événement publié sur une queue JMS.
 */
public record EventMessage(
        String eventType,
        Object payload,
        LocalDateTime eventTime
) {}
