// File: src/main/java/com/AgriTest/service/impl/DashboardServiceImpl.java
package com.AgriTest.service.impl;

import com.AgriTest.dto.DashboardSummaryResponse;
import com.AgriTest.dto.InventoryStatisticsResponse;
import com.AgriTest.dto.TestCaseStatisticsResponse;
import com.AgriTest.model.Inventory;
import com.AgriTest.model.Product;
import com.AgriTest.model.TestCase;
import com.AgriTest.repository.InventoryRepository;
import com.AgriTest.repository.NotificationRepository;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.repository.TestCaseRepository;
import com.AgriTest.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public DashboardSummaryResponse getDashboardSummary() {
        // Get counts from repositories
        long totalProducts = productRepository.count();
        long activeTestCases = testCaseRepository.findByStatus("ACTIVE").size();
        long completedTestCases = testCaseRepository.findByStatus("COMPLETED").size();
        long inventoryItems = inventoryRepository.count();
        
        // Get low stock items (quantity < 100)
        List<Inventory> allInventory = inventoryRepository.findAll();
        long lowStockItems = allInventory.stream()
                .filter(item -> item.getQuantity() < 100)
                .count();
        
        // Get items expiring in the next 30 days
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        long expiringItems = inventoryRepository.findByExpirationDateBefore(thirtyDaysFromNow).size();
        
        // Get pending notifications
        long pendingNotifications = notificationRepository.findBySent(false).size();
        
        return DashboardSummaryResponse.builder()
                .totalProducts(totalProducts)
                .activeTestCases(activeTestCases)
                .completedTestCases(completedTestCases)
                .inventoryItems(inventoryItems)
                .lowStockItems(lowStockItems)
                .expiringItems(expiringItems)
                .pendingNotifications(pendingNotifications)
                .build();
    }

    @Override
    public TestCaseStatisticsResponse getTestCaseStatistics(String period) {
        List<TestCase> testCases = testCaseRepository.findAll();
        
        // Calculate status counts
        Map<String, Long> statusCounts = testCases.stream()
                .collect(Collectors.groupingBy(TestCase::getStatus, Collectors.counting()));
        
        // Calculate average test duration (days)
        double avgDuration = testCases.stream()
                .filter(tc -> tc.getEndDate() != null)
                .mapToLong(tc -> tc.getStartDate().until(tc.getEndDate()).getDays())
                .average()
                .orElse(0.0);
        
        return TestCaseStatisticsResponse.builder()
                .statusCounts(statusCounts)
                .averageTestDuration(avgDuration)
                .build();
    }

    @Override
    public InventoryStatisticsResponse getInventoryStatistics() {
        List<Inventory> inventoryItems = inventoryRepository.findAll();
        
        // Calculate product counts
        Map<String, Long> productCounts = inventoryItems.stream()
                .collect(Collectors.groupingBy(
                        inventory -> inventory.getProduct().getName(), 
                        Collectors.counting()));
        
        // Calculate location counts
        Map<String, Long> locationCounts = inventoryItems.stream()
                .collect(Collectors.groupingBy(Inventory::getLocation, Collectors.counting()));
        
        // Calculate total inventory value (simplified - quantity * 100)
        double totalValue = inventoryItems.stream()
                .mapToDouble(inventory -> inventory.getQuantity() * 100) // Placeholder for actual price
                .sum();
        
        return InventoryStatisticsResponse.builder()
                .productCounts(productCounts)
                .locationCounts(locationCounts)
                .totalInventoryValue(totalValue)
                .build();
    }
}