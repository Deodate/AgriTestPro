package com.AgriTest.service;

import com.AgriTest.dto.AlertRequest;
import com.AgriTest.dto.AlertResponse;
import com.AgriTest.model.Alert;

import java.util.List;

public interface AlertService {
    AlertResponse createAlert(AlertRequest request);
    
    AlertResponse updateAlert(Long id, AlertRequest request);
    
    AlertResponse getAlertById(Long id);
    
    List<AlertResponse> getAllAlerts();
    
    List<AlertResponse> getAlertsByType(Alert.AlertType alertType);
    
    List<AlertResponse> getAlertsByRecipient(Long userId);
    
    List<AlertResponse> getAlertsByNotificationMethod(Alert.NotificationMethod method);
    
    List<AlertResponse> getActiveAlerts();
    
    AlertResponse toggleAlertStatus(Long id);
    
    void deleteAlert(Long id);
}