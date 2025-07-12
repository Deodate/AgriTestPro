// File: src/main/java/com/AgriTest/util/ExcelUtils.java
package com.AgriTest.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Utility class for Excel operations.
 */
public class ExcelUtils {

    /**
     * Generate an Excel workbook from the provided data.
     *
     * @param sheetName the name of the sheet
     * @param headers the column headers
     * @param data the data rows, each row is a map of column name to value
     * @return the generated workbook as ByteArrayInputStream
     * @throws IOException if an I/O error occurs
     */
    public static ByteArrayInputStream generateExcel(
            String sheetName, 
            List<String> headers, 
            List<Map<String, Object>> data) throws IOException {
        
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet(sheetName);
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (Map<String, Object> rowData : data) {
                Row row = sheet.createRow(rowNum++);
                
                int colNum = 0;
                for (String header : headers) {
                    Cell cell = row.createCell(colNum++);
                    
                    Object value = rowData.get(header);
                    if (value != null) {
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                        } else if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                        } else if (value instanceof Long) {
                            cell.setCellValue((Long) value);
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                        } else if (value instanceof Boolean) {
                            cell.setCellValue((Boolean) value);
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }
            
            // Resize columns to fit content
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
    
    /**
     * Create a cell style for date cells.
     *
     * @param workbook the workbook
     * @return the date cell style
     */
    public static CellStyle createDateCellStyle(Workbook workbook) {
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
        return dateCellStyle;
    }
    
    /**
     * Create a cell style for numeric cells.
     *
     * @param workbook the workbook
     * @return the numeric cell style
     */
    public static CellStyle createNumericCellStyle(Workbook workbook) {
        CellStyle numericCellStyle = workbook.createCellStyle();
        numericCellStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        return numericCellStyle;
    }
    
    /**
     * Create a cell style for header cells.
     *
     * @param workbook the workbook
     * @return the header cell style
     */
    public static CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        return headerStyle;
    }
}