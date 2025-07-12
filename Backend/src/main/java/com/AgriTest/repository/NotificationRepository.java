package com.AgriTest.repository;

import com.AgriTest.model.Notification;
import com.AgriTest.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByType(NotificationType type);
    
    List<Notification> findBySent(Boolean sent);
    
    List<Notification> findByRecipientEmail(String email);
    
    List<Notification> findByRecipientPhone(String phone);
    
    List<Notification> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}