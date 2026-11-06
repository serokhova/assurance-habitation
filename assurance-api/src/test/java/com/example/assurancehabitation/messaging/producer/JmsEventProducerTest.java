package com.example.assurancehabitation.messaging.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JmsEventProducerTest {

    @Mock
    private JmsTemplate jmsTemplate;

    private JmsEventProducer producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        producer = new JmsEventProducer(jmsTemplate);
    }

    @Test
    void publish_shouldSerializeEnvelopeAndSendToQueue() throws Exception {
        String queue = "user.created.queue";
        String eventType = "USER_CREATED";
        Map<String, Object> payload = Map.of("id", 1, "name", "John Doe");

        producer.publish(queue, eventType, payload);

        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(jmsTemplate).convertAndSend(eq(queue), jsonCaptor.capture());

        String capturedJson = jsonCaptor.getValue();
        assertThat(capturedJson).isNotNull();

        JsonNode root = objectMapper.readTree(capturedJson);
        assertThat(root.get("eventType").asText()).isEqualTo("USER_CREATED");
        assertThat(root.get("eventTime")).isNotNull();
        assertThat(root.get("payload").get("id").asInt()).isEqualTo(1);
        assertThat(root.get("payload").get("name").asText()).isEqualTo("John Doe");
    }
}
