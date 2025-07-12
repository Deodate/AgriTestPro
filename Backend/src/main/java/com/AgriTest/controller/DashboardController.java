// File: src/main/java/com/AgriTest/controller/DashboardController.java
package com.AgriTest.controller;

import com.AgriTest.dto.DashboardSummaryResponse;
import com.AgriTest.dto.InventoryStatisticsResponse;
import com.AgriTest.dto.TestCaseStatisticsResponse;
import com.AgriTest.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public DashboardSummaryResponse getDashboardSummary() {
        return dashboardService.getDashboardSummary();
    }

    @GetMapping("/test-statistics")
    public TestCaseStatisticsResponse getTestCaseStatistics(@RequestParam(required = false) String period) {
        return dashboardService.getTestCaseStatistics(period);
    }

    @GetMapping("/inventory-statistics")
    public InventoryStatisticsResponse getInventoryStatistics() {
        return dashboardService.getInventoryStatistics();
    }
}