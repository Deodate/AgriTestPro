package com.AgriTest.service;

import com.AgriTest.dto.ExportRequest;
import com.AgriTest.model.AnalyticsData;
import com.AgriTest.model.ExportFormat;
import com.AgriTest.repository.AnalyticsDataRepository;
import com.AgriTest.util.CsvExporter;
import com.AgriTest.util.ExcelExporter;
import com.AgriTest.util.PdfExporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DataExportService {
    private static final Logger logger = LoggerFactory.getLogger(DataExportService.class);

    @Autowired
    private AnalyticsDataRepository analyticsDataRepository;
    
    @Autowired
    private CsvExporter csvExporter;
    
    @Autowired
    private ExcelExporter excelExporter;
    
    @Autowired
    private PdfExporter pdfExporter;
    
    public byte[] exportData(ExportRequest request) {
        // Validate request
        if (request == null || request.getDataSource() == null) {
            logger.error("Invalid export request: Missing data source");
            throw new IllegalArgumentException("Invalid export request");
        }

        // Fetch data based on export request parameters
        List<AnalyticsData> data = fetchData(request);
        
        // Log export details
        logger.info("Preparing to export {} records in {} format", 
            data.size(), 
            request.getExportFormat()
        );

        // Export based on requested format
        switch (request.getExportFormat()) {
            case CSV:
                logger.info("Exporting data in CSV format");
                return csvExporter.export(data, request.getColumns());
            case EXCEL:
                logger.info("Exporting data in Excel format");
                return excelExporter.export(data, request.getColumns());
            case PDF:
                logger.info("Exporting data in PDF format");
                return pdfExporter.export(data, request.getColumns(), request.getTitle());
            default:
                logger.error("Unsupported export format: {}", request.getExportFormat());
                throw new IllegalArgumentException("Unsupported export format: " + request.getExportFormat());
        }
    }
    
    private List<AnalyticsData> fetchData(ExportRequest request) {
        // If entity ID and type are provided
        if (request.getEntityId() != null && request.getEntityType() != null) {
            return analyticsDataRepository.findByDataSourceAndEntityTypeAndEntityIdAndDataDateBetweenOrderByDataDate(
                    request.getDataSource(),
                    request.getEntityType(),
                    request.getEntityId(),
                    request.getStartDate(),
                    request.getEndDate() != null ? request.getEndDate() : LocalDate.now()
            );
        }
        
        // If only data source is provided
        return analyticsDataRepository.findByDataSourceAndDataDateBetweenOrderByDataDate(
                request.getDataSource(),
                request.getStartDate(),
                request.getEndDate() != null ? request.getEndDate() : LocalDate.now()
        );
    }
}