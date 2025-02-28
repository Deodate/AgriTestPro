// File: src/main/java/com/AgriTest/service/NotificationService.java
package com.AgriTest.service;

import com.AgriTest.dto.NotificationRequest;
import com.AgriTest.dto.NotificationResponse;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<NotificationResponse> getAllNotifications();
    Optional<NotificationResponse> getNotificationById(Long id);
    NotificationResponse createNotification(NotificationRequest notificationRequest);
    NotificationResponse sendNotification(Long id);
    void deleteNotification(Long id);
    List<NotificationResponse> getNotificationsBySent(boolean sent);
    List<NotificationResponse> getNotificationsByRecipientEmail(String email);
    List<NotificationResponse> getNotificationsByRecipientPhone(String phone);
}