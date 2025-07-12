package com.AgriTest.util;

import com.AgriTest.model.AnalyticsData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelExporter {

    public byte[] export(List<AnalyticsData> data, List<String> columns) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Analytics Data");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i));
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (AnalyticsData item : data) {
                Row row = sheet.createRow(rowNum++);
                
                for (int i = 0; i < columns.size(); i++) {
                    String column = columns.get(i);
                    Cell cell = row.createCell(i);
                    
                    switch (column) {
                        case "id":
                            cell.setCellValue(item.getId());
                            break;
                        case "dataSource":
                            cell.setCellValue(item.getDataSource());
                            break;
                        case "dataDate":
                            cell.setCellValue(item.getDataDate().toString());
                            break;
                        case "value":
                            cell.setCellValue(item.getValue());
                            break;
                        case "entityId":
                            cell.setCellValue(item.getEntityId());
                            break;
                        case "entityType":
                            cell.setCellValue(item.getEntityType().toString());
                            break;
                        case "metricName":
                            cell.setCellValue(item.getMetricName());
                            break;
                        case "createdAt":
                            cell.setCellValue(item.getCreatedAt().toString());
                            break;
                    }
                }
            }
            
            // Auto size columns
            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data as Excel", e);
        }
    }
}