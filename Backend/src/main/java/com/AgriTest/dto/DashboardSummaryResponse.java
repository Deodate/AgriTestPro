package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private Long totalProducts;
    private Long activeTestCases;
    private Long completedTestCases;
    private Long inventoryItems;
    private Long lowStockItems;
    private Long expiringItems;
    private Long pendingNotifications;
}