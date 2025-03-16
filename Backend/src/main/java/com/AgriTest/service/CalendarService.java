package com.AgriTest.service;

import com.AgriTest.dto.CalendarEventRequest;
import com.AgriTest.dto.CalendarEventResponse;
import com.AgriTest.model.CalendarEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarService {
    CalendarEventResponse createEvent(CalendarEventRequest request);
    
    CalendarEventResponse updateEvent(Long id, CalendarEventRequest request);
    
    CalendarEventResponse getEventById(Long id);
    
    List<CalendarEventResponse> getAllEvents();
    
    List<CalendarEventResponse> getEventsByType(CalendarEvent.EventType eventType);
    
    List<CalendarEventResponse> getEventsByParticipant(Long userId);
    
    List<CalendarEventResponse> getEventsByDateRange(LocalDateTime start, LocalDateTime end);
    
    List<CalendarEventResponse> getEventsByParticipantAndDateRange(Long userId, LocalDateTime start, LocalDateTime end);
    
    List<CalendarEventResponse> getActiveEvents();
    
    CalendarEventResponse cancelEvent(Long id);
    
    void deleteEvent(Long id);
}