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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired(required = false)  // Make this optional since we might not have email configured yet
    private JavaMailSender mailSender;

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
            if (notification.getType() == NotificationType.EMAIL && mailSender != null) {
                sendEmail(notification.getRecipientEmail(), notification.getMessage());
                sentSuccessfully = true;
            } else {
                // For now, just mark as sent for other types
                sentSuccessfully = true;
            }
            
            if (sentSuccessfully) {
                notification.setSent(true);
                notification.setSentAt(LocalDateTime.now());
                notification = notificationRepository.save(notification);
            }
        } catch (Exception e) {
            // Log the error but don't throw it to the client
            System.err.println("Failed to send notification: " + e.getMessage());
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
    
    private void sendEmail(String recipient, String message) {
        if (recipient == null || recipient.isEmpty() || mailSender == null) {
            return;
        }
        
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipient);
        mailMessage.setSubject("AgriTest Pro Notification");
        mailMessage.setText(message);
        
        mailSender.send(mailMessage);
    }
}