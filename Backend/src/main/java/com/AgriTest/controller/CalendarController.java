package com.AgriTest.controller;

import com.AgriTest.dto.CalendarEventRequest;
import com.AgriTest.dto.CalendarEventResponse;
import com.AgriTest.model.CalendarEvent;
import com.AgriTest.service.CalendarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    // Create Event
    @PostMapping("/events")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CalendarEventResponse> createEvent(@Valid @RequestBody CalendarEventRequest request) {
        CalendarEventResponse createdEvent = calendarService.createEvent(request);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    // Update Event
    @PutMapping("/events/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CalendarEventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody CalendarEventRequest request) {
        CalendarEventResponse updatedEvent = calendarService.updateEvent(id, request);
        return ResponseEntity.ok(updatedEvent);
    }

    // Get Event by ID
    @GetMapping("/events/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<CalendarEventResponse> getEventById(@PathVariable Long id) {
        CalendarEventResponse event = calendarService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    // Get All Events
    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<List<CalendarEventResponse>> getAllEvents() {
        List<CalendarEventResponse> events = calendarService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Get Events by Type
    @GetMapping("/events/type/{eventType}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByType(
            @PathVariable CalendarEvent.EventType eventType) {
        List<CalendarEventResponse> events = calendarService.getEventsByType(eventType);
        return ResponseEntity.ok(events);
    }

    // Get Events by Participant
    @GetMapping("/events/participant/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or (hasRole('TESTER') and #userId == authentication.principal.id)")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByParticipant(@PathVariable Long userId) {
        List<CalendarEventResponse> events = calendarService.getEventsByParticipant(userId);
        return ResponseEntity.ok(events);
    }

    // Get Events by Date Range
    @GetMapping("/events/range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<CalendarEventResponse> events = calendarService.getEventsByDateRange(start, end);
        return ResponseEntity.ok(events);
    }

    // Get Events by Participant and Date Range
    @GetMapping("/events/participant/{userId}/range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or (hasRole('TESTER') and #userId == authentication.principal.id)")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByParticipantAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<CalendarEventResponse> events = calendarService.getEventsByParticipantAndDateRange(userId, start, end);
        return ResponseEntity.ok(events);
    }

    // Get Active Events
    @GetMapping("/events/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<List<CalendarEventResponse>> getActiveEvents() {
        List<CalendarEventResponse> events = calendarService.getActiveEvents();
        return ResponseEntity.ok(events);
    }

    // Cancel Event
    @PatchMapping("/events/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<CalendarEventResponse> cancelEvent(@PathVariable Long id) {
        CalendarEventResponse updatedEvent = calendarService.cancelEvent(id);
        return ResponseEntity.ok(updatedEvent);
    }

    // Delete Event
    @DeleteMapping("/events/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        calendarService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}