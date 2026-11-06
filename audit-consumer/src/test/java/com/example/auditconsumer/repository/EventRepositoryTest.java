package com.example.auditconsumer.repository;

import com.example.auditconsumer.entity.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void saveAndFindByEventType_shouldPersistEventInMessagingSchema() {
        LocalDateTime now = LocalDateTime.now();
        Event event1 = new Event("USER_CREATED", "{\"id\":1,\"name\":\"Alice\"}", now, now);
        Event event2 = new Event("CONTRACT_CREATED", "{\"id\":10,\"formula\":\"basic\"}", now, now);

        eventRepository.save(event1);
        eventRepository.save(event2);

        List<Event> userCreatedEvents = eventRepository.findByEventType("USER_CREATED");

        assertThat(userCreatedEvents).hasSize(1);
        Event saved = userCreatedEvents.get(0);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEventType()).isEqualTo("USER_CREATED");
        assertThat(saved.getPayload()).contains("Alice");
        assertThat(saved.getConsumerReceivedAt()).isNotNull();
    }
}
