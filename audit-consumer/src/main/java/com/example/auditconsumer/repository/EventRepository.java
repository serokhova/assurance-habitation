package com.example.auditconsumer.repository;

import com.example.auditconsumer.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventType(String eventType);
}
