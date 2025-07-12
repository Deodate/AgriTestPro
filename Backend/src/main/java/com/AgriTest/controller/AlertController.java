package com.AgriTest.controller;

import com.AgriTest.dto.AlertRequest;
import com.AgriTest.dto.AlertResponse;
import com.AgriTest.model.Alert;
import com.AgriTest.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    // Create Alert
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<AlertResponse> createAlert(@Valid @RequestBody AlertRequest request) {
        AlertResponse createdAlert = alertService.createAlert(request);
        return new ResponseEntity<>(createdAlert, HttpStatus.CREATED);
    }

    // Update Alert
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<AlertResponse> updateAlert(
            @PathVariable Long id,
            @Valid @RequestBody AlertRequest request) {
        AlertResponse updatedAlert = alertService.updateAlert(id, request);
        return ResponseEntity.ok(updatedAlert);
    }

    // Get Alert by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<AlertResponse> getAlertById(@PathVariable Long id) {
        AlertResponse alert = alertService.getAlertById(id);
        return ResponseEntity.ok(alert);
    }

    // Get All Alerts
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<AlertResponse>> getAllAlerts() {
        List<AlertResponse> alerts = alertService.getAllAlerts();
        return ResponseEntity.ok(alerts);
    }

    // Get Alerts by Type
    @GetMapping("/type/{alertType}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<AlertResponse>> getAlertsByType(
            @PathVariable Alert.AlertType alertType) {
        List<AlertResponse> alerts = alertService.getAlertsByType(alertType);
        return ResponseEntity.ok(alerts);
    }

    // Get Alerts by Recipient
    @GetMapping("/recipient/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or (hasRole('TESTER') and #userId == authentication.principal.id)")
    public ResponseEntity<List<AlertResponse>> getAlertsByRecipient(@PathVariable Long userId) {
        List<AlertResponse> alerts = alertService.getAlertsByRecipient(userId);
        return ResponseEntity.ok(alerts);
    }

    // Get Alerts by Notification Method
    @GetMapping("/method/{method}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<AlertResponse>> getAlertsByNotificationMethod(
            @PathVariable Alert.NotificationMethod method) {
        List<AlertResponse> alerts = alertService.getAlertsByNotificationMethod(method);
        return ResponseEntity.ok(alerts);
    }

    // Get Active Alerts
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TESTER')")
    public ResponseEntity<List<AlertResponse>> getActiveAlerts() {
        List<AlertResponse> alerts = alertService.getActiveAlerts();
        return ResponseEntity.ok(alerts);
    }

    // Toggle Alert Status (Active/Inactive)
    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<AlertResponse> toggleAlertStatus(@PathVariable Long id) {
        AlertResponse updatedAlert = alertService.toggleAlertStatus(id);
        return ResponseEntity.ok(updatedAlert);
    }

    // Delete Alert
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        alertService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }
}