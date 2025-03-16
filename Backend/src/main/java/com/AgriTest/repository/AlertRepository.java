package com.AgriTest.repository;

import com.AgriTest.model.Alert;
import com.AgriTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByAlertType(Alert.AlertType alertType);
    
    List<Alert> findByIsActive(Boolean isActive);
    
    List<Alert> findByCreatedBy(User createdBy);
    
    @Query("SELECT a FROM Alert a JOIN a.recipients r WHERE r.id = :userId")
    List<Alert> findByRecipientId(Long userId);
    
    @Query("SELECT a FROM Alert a JOIN a.notificationMethods m WHERE m = :method")
    List<Alert> findByNotificationMethod(Alert.NotificationMethod method);
}