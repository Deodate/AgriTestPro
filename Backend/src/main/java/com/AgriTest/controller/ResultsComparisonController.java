package com.AgriTest.controller;

import com.AgriTest.dto.ResultsComparisonRequest;
import com.AgriTest.service.ResultsComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/comparison")
public class ResultsComparisonController {

    @Autowired
    private ResultsComparisonService resultsComparisonService;

    @PostMapping("/generate-report")
    public ResponseEntity<?> generateComparisonReport(@RequestBody ResultsComparisonRequest request) {
        try {
            ByteArrayOutputStream reportStream = resultsComparisonService.generateReport(request);

            HttpHeaders headers = new HttpHeaders();
            String filename = "results_comparison";
            MediaType mediaType = null;

            if ("PDF".equalsIgnoreCase(request.getDownloadFormat())) {
                mediaType = MediaType.APPLICATION_PDF;
                filename += ".pdf";
            } else if ("CSV".equalsIgnoreCase(request.getDownloadFormat())) {
                mediaType = MediaType.parseMediaType("text/csv");
                 filename += ".csv";
            } else {
                return ResponseEntity.badRequest().body("Unsupported download format");
            }

            headers.setContentType(mediaType);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(reportStream.size());

            return new ResponseEntity<>(reportStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            // Log the exception properly
            e.printStackTrace(); // Replace with logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating report: " + e.getMessage());
        }
    }
} 