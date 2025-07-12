package com.AgriTest.service;

import com.AgriTest.dto.FieldActivityRequest;
import com.AgriTest.dto.FieldActivityResponse;
import com.AgriTest.model.FieldActivity;

import java.time.LocalDateTime;
import java.util.List;

public interface FieldActivityService {
    FieldActivityResponse createFieldActivity(FieldActivityRequest request);
    FieldActivityResponse updateFieldActivity(Long id, FieldActivityRequest request);
    FieldActivityResponse getFieldActivityById(Long id);
    List<FieldActivityResponse> getAllFieldActivities();
    List<FieldActivityResponse> getFieldActivitiesByResponsiblePerson(Long userId);
    List<FieldActivityResponse> getFieldActivitiesByStatus(FieldActivity.FieldActivityStatus status);
    List<FieldActivityResponse> getFieldActivitiesByDateRange(
        LocalDateTime startDateTime, 
        LocalDateTime endDateTime
    );
    void deleteFieldActivity(Long id);
    FieldActivityResponse updateFieldActivityStatus(Long id, FieldActivity.FieldActivityStatus status);
}