package com.AgriTest.service.impl;

import com.AgriTest.model.ActivityLog;
import com.AgriTest.repository.ActivityLogRepository;
import com.AgriTest.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Override
    @Transactional
    public void logActivity(Long userId, String actionPerformed) {
        ActivityLog log = new ActivityLog();
        log.setUserId(userId);
        log.setActionPerformed(actionPerformed);
        activityLogRepository.save(log);
    }

    @Override
    public List<ActivityLog> getActivityLogsByUserId(Long userId) {
        return activityLogRepository.findAll().stream()
                .filter(log -> log.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ActivityLog> getAllActivityLogs() {
        return activityLogRepository.findAll();
    }
} 