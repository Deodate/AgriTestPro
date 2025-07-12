package com.AgriTest.dto;

import com.AgriTest.model.Alert;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class AlertRequest {
    @NotNull(message = "Alert type is required")
    private Alert.AlertType alertType;
    
    @NotEmpty(message = "At least one recipient is required")
    private Set<Long> recipientIds;
    
    @NotEmpty(message = "At least one notification method is required")
    private Set<Alert.NotificationMethod> notificationMethods;
    
    @Size(max = 5000, message = "Custom message must be less than 5000 characters")
    private String customMessage;
    
    private Boolean isActive = true;
}