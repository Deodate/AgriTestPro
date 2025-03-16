package com.AgriTest.dto;

import com.AgriTest.model.Alert;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AlertResponse {
    private Long id;
    private Alert.AlertType alertType;
    private Set<UserDto> recipients;
    private Set<Alert.NotificationMethod> notificationMethods;
    private String customMessage;
    private Boolean isActive;
    
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