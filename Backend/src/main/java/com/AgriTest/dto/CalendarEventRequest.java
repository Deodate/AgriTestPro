package com.AgriTest.dto;

import com.AgriTest.model.CalendarEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CalendarEventRequest {
    @NotBlank(message = "Event title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;
    
    @NotNull(message = "Start date and time is required")
    @FutureOrPresent(message = "Start date must be in present or future")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;
    
    @NotNull(message = "Event type is required")
    private CalendarEvent.EventType eventType;
    
    private Set<Long> participantIds;
    
    private String location;
    
    private Boolean isAllDay = false;
}