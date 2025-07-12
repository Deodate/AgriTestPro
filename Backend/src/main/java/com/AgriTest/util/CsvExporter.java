package com.AgriTest.util;

import com.AgriTest.model.AnalyticsData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvExporter {
    private static final Logger logger = LoggerFactory.getLogger(CsvExporter.class);

    // Default columns if none are specified
    private static final List<String> DEFAULT_COLUMNS = Arrays.asList(
        "id", "dataSource", "dataDate", "value", 
        "entityId", "entityType", "metricName", "createdAt"
    );

    public byte[] export(List<AnalyticsData> data, List<String> columns) {
        // Validate input
        if (data == null || data.isEmpty()) {
            logger.warn("Attempted to export empty data set");
            return new byte[0];
        }

        // Use default columns if null or empty
        List<String> selectedColumns = (columns == null || columns.isEmpty()) 
            ? DEFAULT_COLUMNS 
            : validateAndFilterColumns(columns);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(selectedColumns.toArray(new String[0])))) {
            
            logger.info("Exporting {} records with columns: {}", data.size(), selectedColumns);

            for (AnalyticsData item : data) {
                csvPrinter.printRecord(
                    getValueForColumn("id", item, selectedColumns),
                    getValueForColumn("dataSource", item, selectedColumns),
                    getValueForColumn("dataDate", item, selectedColumns),
                    getValueForColumn("value", item, selectedColumns),
                    getValueForColumn("entityId", item, selectedColumns),
                    getValueForColumn("entityType", item, selectedColumns),
                    getValueForColumn("metricName", item, selectedColumns),
                    getValueForColumn("createdAt", item, selectedColumns)
                );
            }
            
            csvPrinter.flush();
            byte[] csvData = out.toByteArray();
            logger.info("CSV export completed. File size: {} bytes", csvData.length);
            return csvData;
        } catch (IOException e) {
            logger.error("Failed to export data as CSV", e);
            throw new RuntimeException("Failed to export data as CSV", e);
        }
    }
    
    private List<String> validateAndFilterColumns(List<String> columns) {
        List<String> validColumns = columns.stream()
            .filter(DEFAULT_COLUMNS::contains)
            .collect(Collectors.toList());

        if (validColumns.isEmpty()) {
            logger.warn("No valid columns provided. Using default columns.");
            return DEFAULT_COLUMNS;
        }

        if (validColumns.size() < columns.size()) {
            logger.warn("Some provided columns were invalid and were removed.");
        }

        return validColumns;
    }
    
    private Object getValueForColumn(String column, AnalyticsData data, List<String> columns) {
        if (columns == null || !columns.contains(column)) {
            return null;
        }
        
        try {
            switch (column) {
                case "id":
                    return data.getId();
                case "dataSource":
                    return data.getDataSource();
                case "dataDate":
                    return data.getDataDate();
                case "value":
                    return data.getValue();
                case "entityId":
                    return data.getEntityId();
                case "entityType":
                    return data.getEntityType();
                case "metricName":
                    return data.getMetricName();
                case "createdAt":
                    return data.getCreatedAt();
                default:
                    return null;
            }
        } catch (NullPointerException e) {
            // Handle potential null values
            logger.debug("Null value encountered for column: {}", column);
            return "";
        }
    }
}