package com.AgriTest.service;

import com.AgriTest.dto.TestCaseResponse;
import com.AgriTest.dto.TestResultResponse;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.exception.ExportException;
import com.AgriTest.model.ExportFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String CSV_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";
    
    private final TestCaseService testCaseService;
    private final TestScheduleService testScheduleService;

    /**
     * Export test cases to the specified format
     */
    public byte[] exportTestCases(ExportFormat format, List<Long> testCaseIds) {
        try {
            log.debug("Exporting test cases. Format: {}, IDs: {}", format, testCaseIds);
            List<TestCaseResponse> testCases;
            
            if (!CollectionUtils.isEmpty(testCaseIds)) {
                testCases = testCaseService.getTestCasesByIds(testCaseIds);
                if (testCases.size() != testCaseIds.size()) {
                    log.warn("Some test cases were not found. Requested: {}, Found: {}", 
                        testCaseIds.size(), testCases.size());
                }
            } else {
                testCases = testCaseService.getAllTestCases();
            }
            
            if (testCases.isEmpty()) {
                throw new ExportException("No test cases found for export");
            }
            
            byte[] exportData = exportTestCasesToCsv(testCases);
            log.info("Successfully exported {} test cases", testCases.size());
            return exportData;
        } catch (Exception e) {
            log.error("Failed to export test cases", e);
            throw new ExportException("Failed to export test cases: " + e.getMessage());
        }
    }
    
    /**
     * Export test schedules to the specified format
     */
    public byte[] exportTestSchedules(ExportFormat format, List<Long> scheduleIds) {
        try {
            log.debug("Exporting test schedules. Format: {}, IDs: {}", format, scheduleIds);
            List<TestScheduleResponse> schedules;
            
            if (!CollectionUtils.isEmpty(scheduleIds)) {
                schedules = testScheduleService.getTestSchedulesByIds(scheduleIds);
                if (schedules.size() != scheduleIds.size()) {
                    log.warn("Some schedules were not found. Requested: {}, Found: {}", 
                        scheduleIds.size(), schedules.size());
                }
            } else {
                schedules = testScheduleService.getAllTestSchedules();
            }
            
            if (schedules.isEmpty()) {
                throw new ExportException("No schedules found for export");
            }
            
            byte[] exportData = exportSchedulesToCsv(schedules);
            log.info("Successfully exported {} schedules", schedules.size());
            return exportData;
        } catch (Exception e) {
            log.error("Failed to export test schedules", e);
            throw new ExportException("Failed to export test schedules: " + e.getMessage());
        }
    }
    
    /**
     * Export test results to the specified format
     */
    public byte[] exportTestResults(ExportFormat format, Long testCaseId) {
        try {
            if (testCaseId == null) {
                throw new IllegalArgumentException("Test case ID is required");
            }
            
            log.debug("Exporting test results for test case: {}", testCaseId);
            List<TestResultResponse> results = testCaseService.getTestResultsByTestCase(testCaseId);
            
            if (results.isEmpty()) {
                throw new ExportException("No test results found for test case ID: " + testCaseId);
            }
            
            byte[] exportData = exportTestResultsToCsv(results);
            log.info("Successfully exported {} test results for test case {}", results.size(), testCaseId);
            return exportData;
        } catch (Exception e) {
            log.error("Failed to export test results for test case: {}", testCaseId, e);
            throw new ExportException("Failed to export test results: " + e.getMessage());
        }
    }
    
    /* Implementation of specific export methods */
    
    private byte[] exportTestCasesToCsv(List<TestCaseResponse> testCases) {
        StringBuilder csv = new StringBuilder();
        
        // Add header
        StringJoiner header = new StringJoiner(CSV_DELIMITER)
            .add("ID")
            .add("Test Name")
            .add("Test Description")
            .add("Test Objectives")
            .add("Product Type")
            .add("Product Batch Number")
            .add("Start Date")
            .add("End Date")
            .add("Status");
        csv.append(header).append(LINE_SEPARATOR);
        
        // Add data
        for (TestCaseResponse testCase : testCases) {
            StringJoiner row = new StringJoiner(CSV_DELIMITER)
                .add(String.valueOf(testCase.getId()))
                .add(escapeCsvField(testCase.getTestName()))
                .add(escapeCsvField(testCase.getTestDescription()))
                .add(escapeCsvField(testCase.getTestObjectives()))
                .add(escapeCsvField(testCase.getProductType()))
                .add(escapeCsvField(testCase.getProductBatchNumber()))
                .add(testCase.getStartDate() != null ? testCase.getStartDate().format(DATE_FORMATTER) : "")
                .add(testCase.getEndDate() != null ? testCase.getEndDate().format(DATE_FORMATTER) : "")
                .add(escapeCsvField(testCase.getStatus()));
            csv.append(row).append(LINE_SEPARATOR);
        }
        
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }
    
    private byte[] exportSchedulesToCsv(List<TestScheduleResponse> schedules) {
        StringBuilder csv = new StringBuilder();
        
        // Add header
        StringJoiner header = new StringJoiner(CSV_DELIMITER)
            .add("ID")
            .add("Test Name")
            .add("Schedule Name")
            .add("Trial Phase")
            .add("Assigned Personnel")
            .add("Location")
            .add("Test Objective")
            .add("Equipment Required")
            .add("Notification Preference")
            .add("Frequency")
            .add("Day of Month")
            .add("Day of Week")
            .add("Start Date")
            .add("End Date")
            .add("Next Execution")
            .add("Is Active")
            .add("Test Case ID")
            .add("Test Case Title")
            .add("Priority")
            .add("Status")
            .add("Notes")
            .add("Created At")
            .add("Updated At");
        csv.append(header).append(LINE_SEPARATOR);
        
        // Add data
        for (TestScheduleResponse schedule : schedules) {
            StringJoiner row = new StringJoiner(CSV_DELIMITER)
                .add(String.valueOf(schedule.getId()))
                .add(escapeCsvField(schedule.getTestName()))
                .add(escapeCsvField(schedule.getScheduleName()))
                .add(escapeCsvField(schedule.getTrialPhase()))
                .add(escapeCsvField(schedule.getAssignedPersonnel()))
                .add(escapeCsvField(schedule.getLocation()))
                .add(escapeCsvField(schedule.getTestObjective()))
                .add(escapeCsvField(schedule.getEquipmentRequired()))
                .add(escapeCsvField(schedule.getNotificationPreference()))
                .add(escapeCsvField(schedule.getFrequency()))
                .add(schedule.getDayOfMonth() != null ? String.valueOf(schedule.getDayOfMonth()) : "")
                .add(schedule.getDayOfWeek() != null ? String.valueOf(schedule.getDayOfWeek()) : "")
                .add(schedule.getStartDate() != null ? schedule.getStartDate().format(DATE_FORMATTER) : "")
                .add(schedule.getEndDate() != null ? schedule.getEndDate().format(DATE_FORMATTER) : "")
                .add(schedule.getNextExecution() != null ? schedule.getNextExecution().format(DATE_FORMATTER) : "")
                .add(String.valueOf(schedule.getIsActive()))
                .add(schedule.getTestCaseId() != null ? String.valueOf(schedule.getTestCaseId()) : "")
                .add(escapeCsvField(schedule.getTestCaseTitle()))
                .add(escapeCsvField(schedule.getPriority()))
                .add(escapeCsvField(schedule.getStatus()))
                .add(escapeCsvField(schedule.getNotes()))
                .add(schedule.getCreatedAt() != null ? schedule.getCreatedAt().format(DATETIME_FORMATTER) : "")
                .add(schedule.getUpdatedAt() != null ? schedule.getUpdatedAt().format(DATETIME_FORMATTER) : "");
            csv.append(row).append(LINE_SEPARATOR);
        }
        
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }
    
    private byte[] exportTestResultsToCsv(List<TestResultResponse> results) {
        StringBuilder csv = new StringBuilder();
        
        // Add header
        StringJoiner header = new StringJoiner(CSV_DELIMITER)
            .add("ID")
            .add("Phase ID")
            .add("Parameter")
            .add("Value")
            .add("Unit")
            .add("Notes")
            .add("Recorded Date")
            .add("Recorded By");
        csv.append(header).append(LINE_SEPARATOR);
        
        // Add data
        for (TestResultResponse result : results) {
            StringJoiner row = new StringJoiner(CSV_DELIMITER)
                .add(String.valueOf(result.getId()))
                .add(String.valueOf(result.getTestPhaseId()))
                .add(escapeCsvField(result.getParameterName()))
                .add(escapeCsvField(result.getValue()))
                .add(escapeCsvField(result.getUnit()))
                .add(escapeCsvField(result.getNotes()))
                .add(result.getRecordedAt() != null ? result.getRecordedAt().format(DATETIME_FORMATTER) : "")
                .add(escapeCsvField(result.getRecordedBy()));
            csv.append(row).append(LINE_SEPARATOR);
        }
        
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }
    
    /**
     * Escapes special characters in CSV fields according to RFC 4180
     */
    private String escapeCsvField(String value) {
        if (value == null) {
            return "";
        }
        
        // If the value contains quotes, delimiter, or newlines, it needs to be quoted
        boolean needsQuoting = value.contains("\"") || 
                             value.contains(CSV_DELIMITER) || 
                             value.contains("\n") || 
                             value.contains("\r");
        
        if (needsQuoting) {
            // Replace quotes with double quotes and wrap in quotes
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}