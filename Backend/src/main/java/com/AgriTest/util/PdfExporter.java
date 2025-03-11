package com.AgriTest.util;

import com.AgriTest.model.AnalyticsData;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfExporter {

    public byte[] export(List<AnalyticsData> data, List<String> columns, String title) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, out);
            
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titleParagraph = new Paragraph(title != null ? title : "Analytics Data Export", titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            titleParagraph.setSpacingAfter(20);
            document.add(titleParagraph);
            
            // Create table
            PdfPTable table = new PdfPTable(columns.size());
            table.setWidthPercentage(100);
            
            // Add header cells
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            for (String column : columns) {
                PdfPCell cell = new PdfPCell(new Phrase(column, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new BaseColor(220, 220, 220)); // Changed from java.awt.Color to BaseColor
                cell.setPadding(5);
                table.addCell(cell);
            }
            
            // Add data cells
            for (AnalyticsData item : data) {
                for (String column : columns) {
                    String value = "";
                    
                    switch (column) {
                        case "id":
                            value = String.valueOf(item.getId());
                            break;
                        case "dataSource":
                            value = item.getDataSource();
                            break;
                        case "dataDate":
                            value = item.getDataDate().toString();
                            break;
                        case "value":
                            value = String.valueOf(item.getValue());
                            break;
                        case "entityId":
                            value = String.valueOf(item.getEntityId());
                            break;
                        case "entityType":
                            value = item.getEntityType().toString();
                            break;
                        case "metricName":
                            value = item.getMetricName();
                            break;
                        case "createdAt":
                            value = item.getCreatedAt().toString();
                            break;
                    }
                    
                    PdfPCell cell = new PdfPCell(new Phrase(value));
                    cell.setPadding(5);
                    table.addCell(cell);
                }
            }
            
            document.add(table);
            document.close();
            
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export data as PDF", e);
        }
    }
}