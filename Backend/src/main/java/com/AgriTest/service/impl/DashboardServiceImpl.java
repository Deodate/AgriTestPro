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
        
        // Calculate category counts by aggregating products' categories
        Map<String, Long> categoryCounts = new HashMap<>();
        for (TestCase testCase : testCases) {
            String category = testCase.getProduct().getCategory();
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0L) + 1);
        }
        
        // Calculate average test duration (days)
        double avgDuration = testCases.stream()
                .filter(tc -> tc.getEndDate() != null)
                .mapToLong(tc -> tc.getStartDate().until(tc.getEndDate()).getDays())
                .average()
                .orElse(0.0);
        
        // Calculate success rate by category
        Map<String, Double> successRateByCategory = new HashMap<>();
        Map<String, Integer> totalByCategory = new HashMap<>();
        Map<String, Integer> successByCategory = new HashMap<>();
        
        for (TestCase testCase : testCases) {
            if ("COMPLETED".equals(testCase.getStatus())) {
                String category = testCase.getProduct().getCategory();
                totalByCategory.put(category, totalByCategory.getOrDefault(category, 0) + 1);
                
                // Consider a test successful if status is "COMPLETED" (simplified logic)
                successByCategory.put(category, successByCategory.getOrDefault(category, 0) + 1);
            }
        }
        
        for (Map.Entry<String, Integer> entry : totalByCategory.entrySet()) {
            String category = entry.getKey();
            int total = entry.getValue();
            int success = successByCategory.getOrDefault(category, 0);
            double rate = (double) success / total * 100;
            successRateByCategory.put(category, rate);
        }
        
        return TestCaseStatisticsResponse.builder()
                .statusCounts(statusCounts)
                .categoryCounts(categoryCounts)
                .averageTestDuration(avgDuration)
                .successRateByCategory(successRateByCategory)
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
        
        // Calculate value by category
        Map<String, Double> valueByCategory = new HashMap<>();
        for (Inventory inventory : inventoryItems) {
            Product product = inventory.getProduct();
            String category = product.getCategory();
            double itemValue = inventory.getQuantity() * 100; // Placeholder for actual price
            valueByCategory.put(category, valueByCategory.getOrDefault(category, 0.0) + itemValue);
        }
        
        return InventoryStatisticsResponse.builder()
                .productCounts(productCounts)
                .locationCounts(locationCounts)
                .totalInventoryValue(totalValue)
                .valueByCategory(valueByCategory)
                .build();
    }
}