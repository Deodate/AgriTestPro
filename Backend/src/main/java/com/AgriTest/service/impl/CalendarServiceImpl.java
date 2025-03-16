package com.AgriTest.service.impl;

import com.AgriTest.dto.CalendarEventRequest;
import com.AgriTest.dto.CalendarEventResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.CalendarEvent;
import com.AgriTest.model.User;
import com.AgriTest.repository.CalendarEventRepository;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.service.CalendarService;
import com.AgriTest.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {
    private static final Logger log = LoggerFactory.getLogger(CalendarServiceImpl.class);

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public CalendarEventResponse createEvent(CalendarEventRequest request) {
        log.info("Creating new calendar event: {}", request.getTitle());
        
        // Get current user
        User currentUser = userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        
        // Create event entity
        CalendarEvent event = new CalendarEvent();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDateTime(request.getStartDateTime());
        event.setEndDateTime(request.getEndDateTime());
        event.setEventType(request.getEventType());
        event.setLocation(request.getLocation());
        event.setIsAllDay(request.getIsAllDay() != null ? request.getIsAllDay() : false);
        event.setIsCancelled(false);
        event.setCreatedBy(currentUser);
        
        // Set participants if provided
        if (request.getParticipantIds() != null && !request.getParticipantIds().isEmpty()) {
            Set<User> participants = new HashSet<>();
            for (Long userId : request.getParticipantIds()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                participants.add(user);
            }
            event.setParticipants(participants);
        }
        
        // Save event
        CalendarEvent savedEvent = calendarEventRepository.save(event);
        log.info("Calendar event created with ID: {}", savedEvent.getId());
        
        // Return response
        return mapToEventResponse(savedEvent);
    }

    @Override
    @Transactional
    public CalendarEventResponse updateEvent(Long id, CalendarEventRequest request) {
        log.info("Updating calendar event with ID: {}", id);
        
        // Find existing event
        CalendarEvent existingEvent = calendarEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar event not found with id: " + id));
        
        // Update event fields
        existingEvent.setTitle(request.getTitle());
        existingEvent.setDescription(request.getDescription());
        existingEvent.setStartDateTime(request.getStartDateTime());
        existingEvent.setEndDateTime(request.getEndDateTime());
        existingEvent.setEventType(request.getEventType());
        existingEvent.setLocation(request.getLocation());
        existingEvent.setIsAllDay(request.getIsAllDay() != null ? request.getIsAllDay() : existingEvent.getIsAllDay());
        
        // Update participants if provided
        if (request.getParticipantIds() != null) {
            Set<User> participants = new HashSet<>();
            for (Long userId : request.getParticipantIds()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                participants.add(user);
            }
            existingEvent.setParticipants(participants);
        }
        
        // Save updated event
        CalendarEvent updatedEvent = calendarEventRepository.save(existingEvent);
        log.info("Calendar event updated with ID: {}", updatedEvent.getId());
        
        // Return response
        return mapToEventResponse(updatedEvent);
    }

    @Override
    public CalendarEventResponse getEventById(Long id) {
        log.info("Fetching calendar event with ID: {}", id);
        
        CalendarEvent event = calendarEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar event not found with id: " + id));
                
        return mapToEventResponse(event);
    }

    @Override
    public List<CalendarEventResponse> getAllEvents() {
        log.info("Fetching all calendar events");
        
        return calendarEventRepository.findAll().stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarEventResponse> getEventsByType(CalendarEvent.EventType eventType) {
        log.info("Fetching calendar events with type: {}", eventType);
        
        return calendarEventRepository.findByEventType(eventType).stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarEventResponse> getEventsByParticipant(Long userId) {
        log.info("Fetching calendar events for participant with ID: {}", userId);
        
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                
        return calendarEventRepository.findByParticipantId(userId).stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarEventResponse> getEventsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Fetching calendar events between {} and {}", start, end);
        
        return calendarEventRepository.findByDateRange(start, end).stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarEventResponse> getEventsByParticipantAndDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        log.info("Fetching calendar events for participant with ID: {} between {} and {}", userId, start, end);
        
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                
        return calendarEventRepository.findByParticipantIdAndDateRange(userId, start, end).stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarEventResponse> getActiveEvents() {
        log.info("Fetching active calendar events");
        
        return calendarEventRepository.findByIsCancelled(false).stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CalendarEventResponse cancelEvent(Long id) {
        log.info("Cancelling calendar event with ID: {}", id);
        
        CalendarEvent event = calendarEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar event not found with id: " + id));
                
        event.setIsCancelled(true);
        
        CalendarEvent updatedEvent = calendarEventRepository.save(event);
        log.info("Calendar event with ID: {} has been cancelled", id);
        
        return mapToEventResponse(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        log.info("Deleting calendar event with ID: {}", id);
        
        CalendarEvent event = calendarEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar event not found with id: " + id));
                
        calendarEventRepository.delete(event);
        log.info("Calendar event with ID: {} deleted successfully", id);
    }
    
    // Helper method to map CalendarEvent to CalendarEventResponse
    private CalendarEventResponse mapToEventResponse(CalendarEvent event) {
        CalendarEventResponse response = new CalendarEventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setStartDateTime(event.getStartDateTime());
        response.setEndDateTime(event.getEndDateTime());
        response.setEventType(event.getEventType());
        response.setLocation(event.getLocation());
        response.setIsAllDay(event.getIsAllDay());
        response.setIsCancelled(event.getIsCancelled());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());
        
        // Map participants
        Set<CalendarEventResponse.UserDto> participantDtos = new HashSet<>();
        for (User user : event.getParticipants()) {
            CalendarEventResponse.UserDto userDto = new CalendarEventResponse.UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setFullName(user.getFullName());
            userDto.setEmail(user.getEmail());
            participantDtos.add(userDto);
        }
        response.setParticipants(participantDtos);
        
        // Map created by user if available
        if (event.getCreatedBy() != null) {
            CalendarEventResponse.UserDto createdBy = new CalendarEventResponse.UserDto();
            createdBy.setId(event.getCreatedBy().getId());
            createdBy.setUsername(event.getCreatedBy().getUsername());
            createdBy.setFullName(event.getCreatedBy().getFullName());
            createdBy.setEmail(event.getCreatedBy().getEmail());
            response.setCreatedBy(createdBy);
        }
        
        return response;
    }
}