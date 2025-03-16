package com.AgriTest.service.impl;

import com.AgriTest.dto.AlertRequest;
import com.AgriTest.dto.AlertResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.Alert;
import com.AgriTest.model.User;
import com.AgriTest.repository.AlertRepository;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.service.AlertService;
import com.AgriTest.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlertServiceImpl implements AlertService {
    private static final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public AlertResponse createAlert(AlertRequest request) {
        log.info("Creating new alert of type: {}", request.getAlertType());
        
        // Get current user
        User currentUser = userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        
        // Create alert entity
        Alert alert = new Alert();
        alert.setAlertType(request.getAlertType());
        alert.setNotificationMethods(request.getNotificationMethods());
        alert.setCustomMessage(request.getCustomMessage());
        alert.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        alert.setCreatedBy(currentUser);
        
        // Find and set recipients
        Set<User> recipients = new HashSet<>();
        for (Long userId : request.getRecipientIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
            recipients.add(user);
        }
        alert.setRecipients(recipients);
        
        // Save alert
        Alert savedAlert = alertRepository.save(alert);
        log.info("Alert created with ID: {}", savedAlert.getId());
        
        // Return response
        return mapToAlertResponse(savedAlert);
    }

    @Override
    @Transactional
    public AlertResponse updateAlert(Long id, AlertRequest request) {
        log.info("Updating alert with ID: {}", id);
        
        // Find existing alert
        Alert existingAlert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));
        
        // Update alert fields
        existingAlert.setAlertType(request.getAlertType());
        existingAlert.setNotificationMethods(request.getNotificationMethods());
        existingAlert.setCustomMessage(request.getCustomMessage());
        existingAlert.setIsActive(request.getIsActive() != null ? request.getIsActive() : existingAlert.getIsActive());
        
        // Update recipients
        Set<User> recipients = new HashSet<>();
        for (Long userId : request.getRecipientIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
            recipients.add(user);
        }
        existingAlert.setRecipients(recipients);
        
        // Save updated alert
        Alert updatedAlert = alertRepository.save(existingAlert);
        log.info("Alert updated with ID: {}", updatedAlert.getId());
        
        // Return response
        return mapToAlertResponse(updatedAlert);
    }

    @Override
    public AlertResponse getAlertById(Long id) {
        log.info("Fetching alert with ID: {}", id);
        
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));
                
        return mapToAlertResponse(alert);
    }

    @Override
    public List<AlertResponse> getAllAlerts() {
        log.info("Fetching all alerts");
        
        return alertRepository.findAll().stream()
                .map(this::mapToAlertResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertResponse> getAlertsByType(Alert.AlertType alertType) {
        log.info("Fetching alerts with type: {}", alertType);
        
        return alertRepository.findByAlertType(alertType).stream()
                .map(this::mapToAlertResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertResponse> getAlertsByRecipient(Long userId) {
        log.info("Fetching alerts for recipient with ID: {}", userId);
        
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                
        return alertRepository.findByRecipientId(userId).stream()
                .map(this::mapToAlertResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertResponse> getAlertsByNotificationMethod(Alert.NotificationMethod method) {
        log.info("Fetching alerts with notification method: {}", method);
        
        return alertRepository.findByNotificationMethod(method).stream()
                .map(this::mapToAlertResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertResponse> getActiveAlerts() {
        log.info("Fetching active alerts");
        
        return alertRepository.findByIsActive(true).stream()
                .map(this::mapToAlertResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AlertResponse toggleAlertStatus(Long id) {
        log.info("Toggling status for alert with ID: {}", id);
        
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));
                
        // Toggle active status
        alert.setIsActive(!alert.getIsActive());
        
        Alert updatedAlert = alertRepository.save(alert);
        log.info("Alert with ID: {} is now {}", id, updatedAlert.getIsActive() ? "active" : "inactive");
        
        return mapToAlertResponse(updatedAlert);
    }

    @Override
    @Transactional
    public void deleteAlert(Long id) {
        log.info("Deleting alert with ID: {}", id);
        
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));
                
        alertRepository.delete(alert);
        log.info("Alert with ID: {} deleted successfully", id);
    }
    
    // Helper method to map Alert to AlertResponse
    private AlertResponse mapToAlertResponse(Alert alert) {
        AlertResponse response = new AlertResponse();
        response.setId(alert.getId());
        response.setAlertType(alert.getAlertType());
        response.setNotificationMethods(alert.getNotificationMethods());
        response.setCustomMessage(alert.getCustomMessage());
        response.setIsActive(alert.getIsActive());
        response.setCreatedAt(alert.getCreatedAt());
        response.setUpdatedAt(alert.getUpdatedAt());
        
        // Map recipients
        Set<AlertResponse.UserDto> recipientDtos = new HashSet<>();
        for (User user : alert.getRecipients()) {
            AlertResponse.UserDto userDto = new AlertResponse.UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setFullName(user.getFullName());
            userDto.setEmail(user.getEmail());
            recipientDtos.add(userDto);
        }
        response.setRecipients(recipientDtos);
        
        // Map created by user if available
        if (alert.getCreatedBy() != null) {
            AlertResponse.UserDto createdBy = new AlertResponse.UserDto();
            createdBy.setId(alert.getCreatedBy().getId());
            createdBy.setUsername(alert.getCreatedBy().getUsername());
            createdBy.setFullName(alert.getCreatedBy().getFullName());
            createdBy.setEmail(alert.getCreatedBy().getEmail());
            response.setCreatedBy(createdBy);
        }
        
        return response;
    }
}