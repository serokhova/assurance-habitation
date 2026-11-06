package com.example.auditconsumer.controller;

import com.example.auditconsumer.dto.EventResponse;
import com.example.auditconsumer.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@Tag(name = "JMS Events", description = "Consultation of consumed events (Audit Trail)")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @Operation(summary = "List all consumed events")
    public ResponseEntity<List<EventResponse>> findAll() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an event by ID")
    public ResponseEntity<EventResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }
}
