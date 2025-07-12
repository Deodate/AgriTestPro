// File: src/main/java/com/AgriTest/service/DashboardService.java
package com.AgriTest.service;

import com.AgriTest.dto.DashboardSummaryResponse;
import com.AgriTest.dto.InventoryStatisticsResponse;
import com.AgriTest.dto.TestCaseStatisticsResponse;

public interface DashboardService {
    DashboardSummaryResponse getDashboardSummary();
    TestCaseStatisticsResponse getTestCaseStatistics(String period);
    InventoryStatisticsResponse getInventoryStatistics();
}