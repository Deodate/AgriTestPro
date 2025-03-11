package com.AgriTest.util;

import com.AgriTest.model.ExportFormat;
import com.AgriTest.model.ReportType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileNameGenerator {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static String generateFileName(
        ReportType reportType, 
        ExportFormat format, 
        LocalDateTime timestamp
    ) {
        String formattedTimestamp = timestamp.format(TIMESTAMP_FORMATTER);
        return String.format("%s_%s%s", 
            reportType.name().toLowerCase(), 
            formattedTimestamp, 
            getFileExtension(format)
        );
    }

    public static String getFileExtension(ExportFormat format) {
        switch (format) {
            case CSV:
                return ".csv";
            case EXCEL:
                return ".xlsx";
            case PDF:
                return ".pdf";
            default:
                return ".txt";
        }
    }

    public static String getMimeType(ExportFormat format) {
        switch (format) {
            case CSV:
                return "text/csv";
            case EXCEL:
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case PDF:
                return "application/pdf";
            default:
                return "application/octet-stream";
        }
    }
}