package com.AgriTest.dto;

import com.AgriTest.model.CalendarEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CalendarEventResponse {
    private Long id;
    private String title;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;
    
    private CalendarEvent.EventType eventType;
    private Set<UserDto> participants;
    private String location;
    private Boolean isAllDay;
    private Boolean isCancelled;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private UserDto createdBy;

    @Data
    public static class UserDto {
        private Long id;
        private String username;
        private String fullName;
        private String email;
    }
}