package com.AgriTest.util;

import com.AgriTest.model.ComplianceChecklist;
import com.AgriTest.model.ChecklistItem;

import java.util.List;
import java.util.stream.Collectors;

public class ComplianceChecklistUtils {

    /**
     * Determines the overall compliance status based on checklist items
     * @param items List of checklist items
     * @return Compliance status
     */
    public static ComplianceChecklist.ComplianceStatus determineOverallStatus(List<ChecklistItem> items) {
        if (items == null || items.isEmpty()) {
            return ComplianceChecklist.ComplianceStatus.NON_COMPLIANT;
        }

        long totalItems = items.size();
        long passedItems = items.stream()
            .filter(ChecklistItem::getPassed)
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
     * @param items List of checklist items
     * @return Percentage of passed items
     */
    public static double calculateCompliancePercentage(List<ChecklistItem> items) {
        if (items == null || items.isEmpty()) {
            return 0.0;
        }

        long totalItems = items.size();
        long passedItems = items.stream()
            .filter(ChecklistItem::getPassed)
            .count();

        return (double) passedItems / totalItems * 100;
    }
}