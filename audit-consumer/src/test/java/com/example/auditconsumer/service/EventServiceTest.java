package com.example.auditconsumer.service;

import com.example.auditconsumer.dto.EventResponse;
import com.example.auditconsumer.entity.Event;
import com.example.auditconsumer.exception.ResourceNotFoundException;
import com.example.auditconsumer.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = new EventService(eventRepository);
    }

    @Test
    void findAll_shouldReturnAllEventsAsResponses() {
        LocalDateTime now = LocalDateTime.now();
        Event e1 = new Event("USER_CREATED", "{\"id\":1}", now, now);
        e1.setId(1L);
        Event e2 = new Event("CONTRACT_CREATED", "{\"id\":2}", now, now);
        e2.setId(2L);

        when(eventRepository.findAll()).thenReturn(List.of(e1, e2));

        List<EventResponse> result = eventService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).eventType()).isEqualTo("USER_CREATED");
        assertThat(result.get(1).eventType()).isEqualTo("CONTRACT_CREATED");
        verify(eventRepository).findAll();
    }

    @Test
    void findById_existing_shouldReturnEventResponse() {
        LocalDateTime now = LocalDateTime.now();
        Event event = new Event("USER_CREATED", "{\"id\":1}", now, now);
        event.setId(10L);

        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));

        EventResponse response = eventService.findById(10L);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.eventType()).isEqualTo("USER_CREATED");
    }

    @Test
    void findById_notFound_shouldThrowException() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }
}
