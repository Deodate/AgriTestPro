// File: src/main/java/com/AgriTest/service/impl/NotificationServiceImpl.java
package com.AgriTest.service.impl;

import com.AgriTest.dto.NotificationRequest;
import com.AgriTest.dto.NotificationResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.NotificationMapper;
import com.AgriTest.model.Notification;
import com.AgriTest.model.NotificationType;
import com.AgriTest.repository.NotificationRepository;
import com.AgriTest.service.NotificationService;
import com.AgriTest.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private SmsService smsService;

    @Override
    public List<NotificationResponse> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notificationMapper.toDtoList(notifications);
    }

    @Override
    public Optional<NotificationResponse> getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .map(notificationMapper::toDto);
    }

    @Override
    public NotificationResponse createNotification(NotificationRequest notificationRequest) {
        Notification notification = notificationMapper.toEntity(notificationRequest);
        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toDto(savedNotification);
    }

    @Override
    public NotificationResponse sendNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        
        // If already sent, just return it
        if (notification.getSent()) {
            return notificationMapper.toDto(notification);
        }
        
        // Send the notification based on type
        boolean sentSuccessfully = false;
        
        try {
            if (notification.getType() == NotificationType.SMS) {
                sentSuccessfully = smsService.sendSms(notification.getRecipientPhone(), notification.getMessage());
            }
            
            if (sentSuccessfully) {
                notification.setSent(true);
                notification.setSentAt(LocalDateTime.now());
                notification = notificationRepository.save(notification);
                logger.info("Notification {} sent successfully", id);
            } else {
                logger.error("Failed to send notification {}", id);
            }
        } catch (Exception e) {
            logger.error("Error sending notification {}: {}", id, e.getMessage());
        }
        
        return notificationMapper.toDto(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public List<NotificationResponse> getNotificationsBySent(boolean sent) {
        List<Notification> notifications = notificationRepository.findBySent(sent);
        return notificationMapper.toDtoList(notifications);
    }

    @Override
    public List<NotificationResponse> getNotificationsByRecipientEmail(String email) {
        List<Notification> notifications = notificationRepository.findByRecipientEmail(email);
        return notificationMapper.toDtoList(notifications);
    }

    @Override
    public List<NotificationResponse> getNotificationsByRecipientPhone(String phone) {
        List<Notification> notifications = notificationRepository.findByRecipientPhone(phone);
        return notificationMapper.toDtoList(notifications);
    }
}