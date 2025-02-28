// File: src/main/java/com/AgriTest/controller/NotificationController.java
package com.AgriTest.controller;

import com.AgriTest.dto.NotificationRequest;
import com.AgriTest.dto.NotificationResponse;
import com.AgriTest.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<NotificationResponse> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER') or hasRole('INVENTORY_MANAGER')")
    public NotificationResponse createNotification(@Valid @RequestBody NotificationRequest notificationRequest) {
        return notificationService.createNotification(notificationRequest);
    }

    @PostMapping("/send/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> sendNotification(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.sendNotification(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unsent")
    @PreAuthorize("hasRole('ADMIN')")
    public List<NotificationResponse> getUnsentNotifications() {
        return notificationService.getNotificationsBySent(false);
    }

    @GetMapping("/sent")
    @PreAuthorize("hasRole('ADMIN')")
    public List<NotificationResponse> getSentNotifications() {
        return notificationService.getNotificationsBySent(true);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<NotificationResponse> getNotificationsByEmail(@PathVariable String email) {
        return notificationService.getNotificationsByRecipientEmail(email);
    }

    @GetMapping("/phone/{phone}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<NotificationResponse> getNotificationsByPhone(@PathVariable String phone) {
        return notificationService.getNotificationsByRecipientPhone(phone);
    }
}