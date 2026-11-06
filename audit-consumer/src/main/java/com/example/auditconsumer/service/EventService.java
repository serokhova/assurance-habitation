package com.example.auditconsumer.service;

import com.example.auditconsumer.dto.EventResponse;
import com.example.auditconsumer.entity.Event;
import com.example.auditconsumer.exception.ResourceNotFoundException;
import com.example.auditconsumer.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventResponse> findAll() {
        return eventRepository.findAll().stream().map(this::toResponse).toList();
    }

    public EventResponse findById(Long id) {
        return eventRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    private EventResponse toResponse(Event e) {
        return new EventResponse(
                e.getId(),
                e.getEventType(),
                e.getPayload(),
                e.getEventTime(),
                e.getConsumerReceivedAt()
        );
    }
}
