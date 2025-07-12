package com.AgriTest.service;

import com.AgriTest.dto.TestCaseResponse;
import com.AgriTest.dto.TestResultResponse;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.exception.ExportException;
import com.AgriTest.model.ExportFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final TestCaseService testCaseService;
    private final TestScheduleService testScheduleService;

    /**
     * Export test cases to the specified format
     */
    public byte[] exportTestCases(ExportFormat format, List<Long> testCaseIds) {
        List<TestCaseResponse> testCases;
        
        if (testCaseIds != null && !testCaseIds.isEmpty()) {
            // Export specific test cases
            testCases = testCaseService.getTestCasesByIds(testCaseIds);
        } else {
            // Export all test cases
            testCases = testCaseService.getAllTestCases();
        }
        
        if (testCases.isEmpty()) {
            throw new ExportException("No test cases found for export");
        }
        
        // For now, just support CSV to simplify setup
        return exportTestCasesToCsv(testCases);
    }
    
    /**
     * Export test schedules to the specified format
     */
    public byte[] exportTestSchedules(ExportFormat format, List<Long> scheduleIds) {
        List<TestScheduleResponse> schedules;
        
        if (scheduleIds != null && !scheduleIds.isEmpty()) {
            // Export specific schedules
            schedules = testScheduleService.getTestSchedulesByIds(scheduleIds);
        } else {
            // Export all schedules
            schedules = testScheduleService.getAllTestSchedules();
        }
        
        if (schedules.isEmpty()) {
            throw new ExportException("No schedules found for export");
        }
        
        // For now, just support CSV to simplify setup
        return exportSchedulesToCsv(schedules);
    }
    
    /**
     * Export test results to the specified format
     */
    public byte[] exportTestResults(ExportFormat format, Long testCaseId) {
        // Get test results for the specified test case
        List<TestResultResponse> results = testCaseService.getTestResultsByTestCase(testCaseId);
        
        if (results.isEmpty()) {
            throw new ExportException("No test results found for test case ID: " + testCaseId);
        }
        
        // For now, just support CSV to simplify setup
        return exportTestResultsToCsv(results);
    }
    
    /* Implementation of specific export methods */
    
    // CSV exports
    private byte[] exportTestCasesToCsv(List<TestCaseResponse> testCases) {
        StringBuilder csv = new StringBuilder();
        
        // Add header
        csv.append("ID,Test Name,Test Description,Test Objectives,Product Type,Product Batch Number,Start Date,End Date,Status\n");
        
        // Add data
        for (TestCaseResponse testCase : testCases) {
            csv.append(testCase.getId()).append(",");
            csv.append("\"").append(escapeCSV(testCase.getTestName())).append("\",");
            csv.append("\"").append(escapeCSV(testCase.getTestDescription())).append("\",");
            csv.append("\"").append(escapeCSV(testCase.getTestObjectives())).append("\",");
            csv.append("\"").append(escapeCSV(testCase.getProductType())).append("\",");
            csv.append("\"").append(escapeCSV(testCase.getProductBatchNumber())).append("\",");
            csv.append(testCase.getStartDate() != null ? testCase.getStartDate().format(DATE_FORMATTER) : "").append(",");
            csv.append(testCase.getEndDate() != null ? testCase.getEndDate().format(DATE_FORMATTER) : "").append(",");
            csv.append("\"").append(escapeCSV(testCase.getStatus())).append("\"");
            csv.append("\n");
        }
        
        return csv.toString().getBytes();
    }
    
    private byte[] exportSchedulesToCsv(List<TestScheduleResponse> schedules) {
        StringBuilder csv = new StringBuilder();
        
        // Add header with updated fields
        csv.append("ID,Test Name,Schedule Name,Trial Phase,Assigned Personnel,Location,")
           .append("Test Objective,Equipment Required,Notification Preference,")
           .append("Frequency,Day of Month,Day of Week,Start Date,End Date,Next Execution,")
           .append("Is Active,Test Case ID,Test Case Title,Priority,Status,Notes,Created At,Updated At\n");
        
        // Add data
        for (TestScheduleResponse schedule : schedules) {
            csv.append(schedule.getId()).append(",");
            csv.append("\"").append(escapeCSV(schedule.getTestName())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getScheduleName())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getTrialPhase())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getAssignedPersonnel())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getLocation())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getTestObjective())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getEquipmentRequired())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getNotificationPreference())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getFrequency())).append("\",");
            csv.append(schedule.getDayOfMonth() != null ? schedule.getDayOfMonth() : "").append(",");
            csv.append(schedule.getDayOfWeek() != null ? schedule.getDayOfWeek() : "").append(",");
            csv.append(schedule.getStartDate() != null ? schedule.getStartDate().format(DATE_FORMATTER) : "").append(",");
            csv.append(schedule.getEndDate() != null ? schedule.getEndDate().format(DATE_FORMATTER) : "").append(",");
            csv.append(schedule.getNextExecution() != null ? schedule.getNextExecution().format(DATE_FORMATTER) : "").append(",");
            csv.append(schedule.getIsActive()).append(",");
            csv.append(schedule.getTestCaseId() != null ? schedule.getTestCaseId() : "").append(",");
            csv.append("\"").append(escapeCSV(schedule.getTestCaseTitle())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getPriority())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getStatus())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getNotes())).append("\",");
            csv.append(schedule.getCreatedAt() != null ? schedule.getCreatedAt().format(DATETIME_FORMATTER) : "").append(",");
            csv.append(schedule.getUpdatedAt() != null ? schedule.getUpdatedAt().format(DATETIME_FORMATTER) : "");
            csv.append("\n");
        }
        
        return csv.toString().getBytes();
    }
    
    private byte[] exportTestResultsToCsv(List<TestResultResponse> results) {
        StringBuilder csv = new StringBuilder();
        
        // Add header
        csv.append("ID,Phase ID,Parameter,Value,Unit,Notes,Recorded Date,Recorded By\n");
        
        // Add data
        for (TestResultResponse result : results) {
            csv.append(result.getId()).append(",");
            csv.append(result.getTestPhaseId()).append(",");
            csv.append("\"").append(escapeCSV(result.getParameterName())).append("\",");
            csv.append("\"").append(escapeCSV(result.getValue())).append("\",");
            csv.append("\"").append(escapeCSV(result.getUnit())).append("\",");
            csv.append("\"").append(escapeCSV(result.getNotes())).append("\",");
            csv.append(result.getRecordedAt() != null ? result.getRecordedAt().format(DATETIME_FORMATTER) : "").append(",");
            csv.append("\"").append(escapeCSV(result.getRecordedBy())).append("\"");
            csv.append("\n");
        }
        
        return csv.toString().getBytes();
    }
    
    // Helper method to escape CSV special characters
    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\"\"");
    }
}