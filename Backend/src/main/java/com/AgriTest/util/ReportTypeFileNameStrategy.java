package com.AgriTest.util;

import com.AgriTest.model.ReportType;
import com.AgriTest.model.ExportFormat;

public interface ReportTypeFileNameStrategy {
    String generateFileName(
        ReportType reportType, 
        ExportFormat format, 
        Long entityId
    );
}