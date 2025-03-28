package com.AgriTest.service;

import com.AgriTest.model.ActivityLog;
import java.util.List;

public interface ActivityLogService {
    void logActivity(Long userId, String actionPerformed);
    List<ActivityLog> getActivityLogsByUserId(Long userId);
    List<ActivityLog> getAllActivityLogs();
} 