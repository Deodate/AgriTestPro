package com.AgriTest.controller;

import com.AgriTest.dto.ForecastModelRequest;
import com.AgriTest.dto.ForecastModelResponse;
import com.AgriTest.dto.ForecastResponse;
import com.AgriTest.service.ForecastService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/forecasts")
public class ForecastController {

    @Autowired
    private ForecastService forecastService;

    @GetMapping("/models")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST') or hasRole('MANAGER')")
    public List<ForecastModelResponse> getAllForecastModels() {
        return forecastService.getAllForecastModels();
    }

    @GetMapping("/models/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST') or hasRole('MANAGER')")
    public ResponseEntity<ForecastModelResponse> getForecastModelById(@PathVariable Long id) {
        return forecastService.getForecastModelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/models/user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST') or hasRole('MANAGER')")
    public List<ForecastModelResponse> getCurrentUserForecastModels() {
        Long userId = getCurrentUserId();
        return forecastService.getForecastModelsByUser(userId);
    }

    @PostMapping("/models")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    public ForecastModelResponse createForecastModel(@Valid @RequestBody ForecastModelRequest request) {
        Long userId = getCurrentUserId();
        return forecastService.createForecastModel(request, userId);
    }

    @PutMapping("/models/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<ForecastModelResponse> updateForecastModel(
            @PathVariable Long id,
            @Valid @RequestBody ForecastModelRequest request) {
        return ResponseEntity.ok(forecastService.updateForecastModel(id, request));
    }

    @DeleteMapping("/models/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteForecastModel(@PathVariable Long id) {
        forecastService.deleteForecastModel(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/models/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<ForecastModelResponse> activateForecastModel(@PathVariable Long id) {
        return ResponseEntity.ok(forecastService.activateForecastModel(id));
    }

    @PostMapping("/models/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<ForecastModelResponse> deactivateForecastModel(@PathVariable Long id) {
        return ResponseEntity.ok(forecastService.deactivateForecastModel(id));
    }

    @PostMapping("/models/{id}/train")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<Void> trainModel(@PathVariable Long id) {
        forecastService.trainModel(id);
        return ResponseEntity.accepted().build();
    }
    
    @GetMapping("/models/{id}/forecast")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST') or hasRole('MANAGER')")
    public ResponseEntity<ForecastResponse> getForecast(@PathVariable Long id) {
        return ResponseEntity.ok(forecastService.getForecast(id));
    }
    
    // Simplified method to get current user ID
    private Long getCurrentUserId() {
        // For testing purposes, return a default user ID
        // In a real application, you would extract this from your security context
        // based on your specific authentication implementation
        return 1L; // Default admin ID
        
        /* 
        // Uncomment and modify based on your security setup:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Option 1: If username is numeric and represents the user ID
            // return Long.parseLong(authentication.getName());
            
            // Option 2: If you have a user service to look up the ID by username
            // return userService.findByUsername(authentication.getName()).getId();
        }
        throw new RuntimeException("User not authenticated");
        */
    }
}