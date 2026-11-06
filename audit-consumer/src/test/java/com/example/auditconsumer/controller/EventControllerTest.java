package com.example.auditconsumer.controller;

import com.example.auditconsumer.dto.EventResponse;
import com.example.auditconsumer.exception.GlobalExceptionHandler;
import com.example.auditconsumer.exception.ResourceNotFoundException;
import com.example.auditconsumer.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
@Import(GlobalExceptionHandler.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Test
    void findAllEvents_shouldReturn200WithList() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        List<EventResponse> events = List.of(
                new EventResponse(1L, "USER_CREATED", "{\"id\":1}", now, now),
                new EventResponse(2L, "CONTRACT_CREATED", "{\"id\":2}", now, now)
        );

        when(eventService.findAll()).thenReturn(events);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].eventType").value("USER_CREATED"))
                .andExpect(jsonPath("$[1].eventType").value("CONTRACT_CREATED"));
    }

    @Test
    void findEventById_existing_shouldReturn200() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        EventResponse event = new EventResponse(1L, "USER_CREATED", "{\"id\":1}", now, now);

        when(eventService.findById(1L)).thenReturn(event);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.eventType").value("USER_CREATED"));
    }

    @Test
    void findEventById_notFound_shouldReturn404() throws Exception {
        when(eventService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Event not found with id: 999"));

        mockMvc.perform(get("/events/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Event not found with id: 999"));
    }
}
