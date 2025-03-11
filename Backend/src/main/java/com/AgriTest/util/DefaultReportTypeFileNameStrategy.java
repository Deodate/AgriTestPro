package com.AgriTest.util;

import com.AgriTest.model.ReportType;
import com.AgriTest.model.ExportFormat;

import java.time.LocalDateTime;

public class DefaultReportTypeFileNameStrategy implements ReportTypeFileNameStrategy {
    @Override
    public String generateFileName(
        ReportType reportType, 
        ExportFormat format, 
        Long entityId
    ) {
        String baseFileName = generateBaseFileName(reportType, entityId);
        return FileNameGenerator.generateFileName(
            reportType, 
            format, 
            LocalDateTime.now()
        );
    }

    private String generateBaseFileName(ReportType reportType, Long entityId) {
        switch (reportType) {
            case TEST_CASES:
                return entityId != null 
                    ? String.format("test_case_%d", entityId)
                    : "all_test_cases";
            case TEST_SCHEDULES:
                return entityId != null
                    ? String.format("test_schedule_%d", entityId)
                    : "all_test_schedules";
            case TEST_RESULTS:
                return entityId != null
                    ? String.format("test_results_%d", entityId)
                    : "all_test_results";
            default:
                return "unknown_report";
        }
    }
}