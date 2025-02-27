package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    @NotBlank(message = "Type is required")
    private String type;
    
    @NotBlank(message = "Message is required")
    private String message;
    
    private String recipientEmail;
    private String recipientPhone;
}
