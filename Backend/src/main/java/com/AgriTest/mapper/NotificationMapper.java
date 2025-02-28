// File: src/main/java/com/AgriTest/mapper/NotificationMapper.java
package com.AgriTest.mapper;

import com.AgriTest.dto.NotificationRequest;
import com.AgriTest.dto.NotificationResponse;
import com.AgriTest.model.Notification;
import com.AgriTest.model.NotificationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {
    
    public NotificationResponse toDto(Notification notification) {
        if (notification == null) {
            return null;
        }
        
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType().name())
                .message(notification.getMessage())
                .recipientEmail(notification.getRecipientEmail())
                .recipientPhone(notification.getRecipientPhone())
                .sent(notification.getSent())
                .sentAt(notification.getSentAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
    
    public List<NotificationResponse> toDtoList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public Notification toEntity(NotificationRequest request) {
        if (request == null) {
            return null;
        }
        
        Notification notification = new Notification();
        notification.setType(NotificationType.valueOf(request.getType()));
        notification.setMessage(request.getMessage());
        notification.setRecipientEmail(request.getRecipientEmail());
        notification.setRecipientPhone(request.getRecipientPhone());
        notification.setSent(false);
        
        return notification;
    }
}