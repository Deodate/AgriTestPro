package com.AgriTest.service.impl;

import com.AgriTest.dto.ResultsComparisonRequest;
import com.AgriTest.service.ResultsComparisonService;
import com.AgriTest.repository.TestResultRepository;
import com.AgriTest.repository.AnalyticsDataRepository;
import com.AgriTest.model.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// iText 7 imports for PDF generation
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

// Import necessary libraries for CSV/PDF generation (placeholders)
// import com.opencsv.CSVWriter; // Example for CSV - currently using manual approach
// import com.itextpdf.kernel.pdf.PdfDocument; // Example for PDF
// import com.itextpdf.kernel.pdf.PdfWriter; // Example for PDF
// import com.itextpdf.layout.Document; // Example for PDF
// import com.itextpdf.layout.element.Paragraph; // Example for PDF

@Service
public class ResultsComparisonServiceImpl implements ResultsComparisonService {

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private AnalyticsDataRepository analyticsDataRepository;

    @Override
    public ByteArrayOutputStream generateReport(ResultsComparisonRequest request) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        List<TestResult> comparisonData = fetchComparisonData(request);

        try {
            if ("CSV".equalsIgnoreCase(request.getDownloadFormat())) {
                // Implement CSV generation logic using comparisonData
                // Using a manual approach for simplicity. Consider a library like OpenCSV for more complex cases.
                OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

                // Write CSV header
                writer.append("Product ID,Trial ID,Parameter,Value,Date\n");

                // Write data rows
                for (TestResult result : comparisonData) {
                    writer.append(result.getProductId() != null ? result.getProductId().toString() : "N/A").append(",");
                    // Assuming trial ID is test case ID and can be accessed via testPhase -> testCase
                    writer.append(result.getTestPhase() != null && result.getTestPhase().getTestCase() != null ? result.getTestPhase().getTestCase().getId().toString() : "N/A").append(",");
                    // Basic handling for commas in parameterName and value - wrap in quotes if needed (simplified)
                    writer.append("").append(result.getParameterName() != null ? result.getParameterName() : "N/A").append("").append(",");
                    writer.append("").append(result.getValue() != null ? result.getValue() : "N/A").append("").append(",");
                    writer.append(result.getDateOfApproval() != null ? result.getDateOfApproval().toLocalDate().toString() : "N/A");
                    writer.append("\n");
                }

                writer.flush();

            } else if ("PDF".equalsIgnoreCase(request.getDownloadFormat())) {
                // Implement PDF generation logic using comparisonData with iText 7
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Results Comparison Report"));

                // Add a table with comparison data
                Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 2, 2, 2})); // 5 columns

                // Add table headers
                table.addHeaderCell("Product ID");
                table.addHeaderCell("Trial ID");
                table.addHeaderCell("Parameter");
                table.addHeaderCell("Value");
                table.addHeaderCell("Date");

                // Add data rows from comparisonData
                for (TestResult result : comparisonData) {
                    table.addCell(result.getProductId() != null ? result.getProductId().toString() : "N/A");
                    table.addCell(result.getTestPhase() != null && result.getTestPhase().getTestCase() != null ? result.getTestPhase().getTestCase().getId().toString() : "N/A");
                    table.addCell(result.getParameterName() != null ? result.getParameterName() : "N/A");
                    table.addCell(result.getValue() != null ? result.getValue() : "N/A");
                    table.addCell(result.getDateOfApproval() != null ? result.getDateOfApproval().toLocalDate().toString() : "N/A");
                }

                document.add(table);

                // TODO: Consider request.getComparisonType() for different PDF layouts (charts, etc.)
                // Generating charts in PDF is more complex and would require a charting library integrated with iText.

                document.close(); // Finalize the PDF

            } else {
                throw new IllegalArgumentException("Unsupported download format: " + request.getDownloadFormat());
            }
        } catch (IOException e) {
            // Log the exception
            e.printStackTrace(); // Replace with logger
             throw new RuntimeException("Error generating report", e);
        }

        return outputStream;
    }

    private List<TestResult> fetchComparisonData(ResultsComparisonRequest request) {
        List<TestResult> results = new ArrayList<>();

        // Extract start and end dates from timeFrame string
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (request.getTimeFrame() != null && !request.getTimeFrame().isEmpty()) {
            String[] dates = request.getTimeFrame().split(","); // Assuming format "YYYY-MM-DD,YYYY-MM-DD"
            if (dates.length == 2) {
                try {
                    startDate = LocalDate.parse(dates[0].trim());
                    endDate = LocalDate.parse(dates[1].trim());
                } catch (DateTimeParseException e) {
                    // Log warning or handle invalid date format
                    System.err.println("Warning: Could not parse time frame dates: " + request.getTimeFrame());
                    // startDate and endDate remain null
                }
            }
        }

        // Declare final variables for use in lambda
        final LocalDate finalStartDate = startDate;
        final LocalDate finalEndDate = endDate;

        // **** WARNING: In-memory filtering - not efficient for large datasets ****
        // A better approach is to implement proper repository query methods or use Specifications
        List<TestResult> allResults = testResultRepository.findAll();

        results = allResults.stream()
            .filter(result -> {
                boolean matchesProduct = request.getProductIds() == null || request.getProductIds().isEmpty() || (result.getProductId() != null && request.getProductIds().contains(result.getProductId()));
                boolean matchesParameter = request.getParameterToCompare() == null || request.getParameterToCompare().isEmpty() || (result.getParameterName() != null && request.getParameterToCompare().equalsIgnoreCase(result.getParameterName()));
                
                // TODO: Add filtering by trialIds if needed, requires linking TestResult to trials/test cases/phases and filtering by request.getTrialIds()
                // boolean matchesTrial = request.getTrialIds() == null || request.getTrialIds().isEmpty() || (result.getTestPhase() != null && result.getTestPhase().getTestCase() != null && request.getTrialIds().contains(result.getTestPhase().getTestCase().getId()));
                
                boolean matchesDate = true;
                if (finalStartDate != null && finalEndDate != null && result.getDateOfApproval() != null) {
                    LocalDate resultDate = result.getDateOfApproval().toLocalDate();
                    matchesDate = !resultDate.isBefore(finalStartDate) && !resultDate.isAfter(finalEndDate);
                }

                return matchesProduct && matchesParameter && matchesDate; // Include matchesTrial if implemented
            })
            .collect(Collectors.toList());

        // TODO: Add fetching logic for AnalyticsData if needed and merge with TestResult data

        return results;
    }

    // TODO: Implement actual CSV writing method
    // private void writeCsvToStream(List<TestResult> data, ByteArrayOutputStream os) throws IOException {
    //    // Implementation using CSV library
    // }

    // TODO: Implement actual PDF writing method
    // private void writePdfToStream(List<TestResult> data, ByteArrayOutputStream os, String comparisonType) throws IOException {
    //    // Implementation using PDF library, considering comparisonType for layout
    // }

} 