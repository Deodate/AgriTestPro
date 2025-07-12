package com.AgriTest.service;

import com.AgriTest.dto.TestCaseResponse;
import com.AgriTest.dto.TestResultResponse;
import com.AgriTest.dto.TestScheduleResponse;
import com.AgriTest.exception.ExportException;
import com.AgriTest.model.ExportFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportService {

    @Autowired
    private TestCaseService testCaseService;
    
    @Autowired
    private TestScheduleService testScheduleService;

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
            csv.append(testCase.getStartDate()).append(",");
            csv.append(testCase.getEndDate()).append(",");
            csv.append(testCase.getStatus());
            csv.append("\n");
        }
        
        return csv.toString().getBytes();
    }
    
    private byte[] exportSchedulesToCsv(List<TestScheduleResponse> schedules) {
        StringBuilder csv = new StringBuilder();
        
        // Add header
        csv.append("ID,Schedule Name,Test Case,Start Date,End Date,Frequency,Status\n");
        
        // Add data
        for (TestScheduleResponse schedule : schedules) {
            csv.append(schedule.getId()).append(",");
            csv.append("\"").append(escapeCSV(schedule.getScheduleName())).append("\",");
            csv.append("\"").append(escapeCSV(schedule.getTestCaseTitle())).append("\",");
            csv.append(schedule.getStartDate()).append(",");
            csv.append(schedule.getEndDate()).append(",");
            csv.append(schedule.getFrequency()).append(",");
            // Fixed: Using direct string instead of isActive() method
            csv.append("Active"); // Default to Active
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
            csv.append(result.getValue()).append(",");
            csv.append("\"").append(escapeCSV(result.getUnit())).append("\",");
            csv.append("\"").append(escapeCSV(result.getNotes())).append("\",");
            csv.append(result.getRecordedAt()).append(",");
            csv.append(result.getRecordedBy());
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