package com.AgriTest.util;

import com.AgriTest.model.ComplianceChecklist;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComplianceChecklistUtils {

    /**
     * Determines the overall compliance status based on checklist items
     * @param checklistItems Map of checklist items
     * @return Compliance status
     */
    public static ComplianceChecklist.ComplianceStatus determineOverallStatus(Map<String, Boolean> checklistItems) {
        if (checklistItems == null || checklistItems.isEmpty()) {
            return ComplianceChecklist.ComplianceStatus.NON_COMPLIANT;
        }

        long totalItems = checklistItems.size();
        long passedItems = checklistItems.values().stream()
            .filter(value -> Boolean.TRUE.equals(value))
            .count();

        if (passedItems == totalItems) {
            return ComplianceChecklist.ComplianceStatus.COMPLIANT;
        } else if (passedItems > 0) {
            return ComplianceChecklist.ComplianceStatus.PARTIALLY_COMPLIANT;
        } else {
            return ComplianceChecklist.ComplianceStatus.NON_COMPLIANT;
        }
    }

    /**
     * Filters checklists by compliance status
     * @param checklists List of checklists
     * @param status Compliance status to filter
     * @return Filtered list of checklists
     */
    public static List<ComplianceChecklist> filterByComplianceStatus(
            List<ComplianceChecklist> checklists, 
            ComplianceChecklist.ComplianceStatus status) {
        return checklists.stream()
            .filter(checklist -> checklist.getOverallStatus() == status)
            .collect(Collectors.toList());
    }

    /**
     * Calculates compliance percentage
     * @param checklistItems Map of checklist items
     * @return Percentage of passed items
     */
    public static double calculateCompliancePercentage(Map<String, Boolean> checklistItems) {
        if (checklistItems == null || checklistItems.isEmpty()) {
            return 0.0;
        }

        long totalItems = checklistItems.size();
        long passedItems = checklistItems.values().stream()
            .filter(value -> Boolean.TRUE.equals(value))
            .count();

        return (double) passedItems / totalItems * 100;
    }
}