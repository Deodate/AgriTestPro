package com.AgriTest.util;

import com.AgriTest.model.ReportType;
import com.AgriTest.model.ExportFormat;

public class ReportFileNameContext {
    private final ReportTypeFileNameStrategy strategy;

    public ReportFileNameContext() {
        this.strategy = new DefaultReportTypeFileNameStrategy();
    }

    public ReportFileNameContext(ReportTypeFileNameStrategy strategy) {
        this.strategy = strategy;
    }

    public String generateFileName(
        ReportType reportType, 
        ExportFormat format, 
        Long entityId
    ) {
        return strategy.generateFileName(reportType, format, entityId);
    }
}