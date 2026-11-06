package com.example.auditconsumer.messaging;

import com.example.auditconsumer.entity.Event;
import com.example.auditconsumer.repository.EventRepository;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JmsEventConsumerTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TextMessage textMessage;

    private JmsEventConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new JmsEventConsumer(eventRepository);
    }

    @Test
    void onUserCreated_shouldParseJsonAndPersistEvent() throws Exception {
        String jsonPayload = """
                {
                  "eventType": "USER_CREATED",
                  "payload": { "id": 1, "name": "Alice Dupont" },
                  "eventTime": "2026-11-06T10:00:00"
                }
                """;

        when(textMessage.getText()).thenReturn(jsonPayload);

        consumer.onUserCreated(textMessage);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(eventCaptor.capture());

        Event savedEvent = eventCaptor.getValue();
        assertThat(savedEvent.getEventType()).isEqualTo("USER_CREATED");
        assertThat(savedEvent.getPayload()).isEqualTo(jsonPayload);
        assertThat(savedEvent.getEventTime()).isEqualTo("2026-11-06T10:00:00");
        assertThat(savedEvent.getConsumerReceivedAt()).isNotNull();
    }

    @Test
    void onContractCreated_shouldPersistContractEvent() throws Exception {
        String jsonPayload = """
                {
                  "eventType": "CONTRACT_CREATED",
                  "payload": { "id": 42, "formula": "premium" },
                  "eventTime": "2026-11-06T11:00:00"
                }
                """;

        when(textMessage.getText()).thenReturn(jsonPayload);

        consumer.onContractCreated(textMessage);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(eventCaptor.capture());

        Event savedEvent = eventCaptor.getValue();
        assertThat(savedEvent.getEventType()).isEqualTo("CONTRACT_CREATED");
        assertThat(savedEvent.getConsumerReceivedAt()).isNotNull();
    }

    @Test
    void onClaimCreated_shouldPersistClaimEvent() throws Exception {
        String jsonPayload = """
                {
                  "eventType": "CLAIM_CREATED",
                  "payload": { "id": 100, "description": "Water leak" },
                  "eventTime": "2026-11-06T12:00:00"
                }
                """;

        when(textMessage.getText()).thenReturn(jsonPayload);

        consumer.onClaimCreated(textMessage);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(eventCaptor.capture());

        Event savedEvent = eventCaptor.getValue();
        assertThat(savedEvent.getEventType()).isEqualTo("CLAIM_CREATED");
    }

    @Test
    void onUserCreated_invalidJson_shouldThrowRuntimeException() throws Exception {
        when(textMessage.getText()).thenReturn("INVALID_NOT_JSON{");

        assertThatThrownBy(() -> consumer.onUserCreated(textMessage))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to process incoming JMS message: USER_CREATED");
    }
}
