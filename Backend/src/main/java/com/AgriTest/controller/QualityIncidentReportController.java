package com.AgriTest.controller;

import com.AgriTest.dto.QualityIncidentReportDTO;
import com.AgriTest.model.QualityIncidentReport;
import com.AgriTest.service.QualityIncidentReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/quality-incident-reports")
public class QualityIncidentReportController {

    @Autowired
    private QualityIncidentReportService qualityIncidentReportService;

    @Autowired
    private ObjectMapper objectMapper;

    // Flexible create endpoint supporting both JSON and form-data
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createQualityIncidentReport(
            @RequestBody(required = false) String jsonBody,
            @RequestParam(value = "dto", required = false) String dtoJson,
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(value = "incidentDate", required = false) String incidentDate,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "correctiveActions", required = false) String correctiveActions,
            @RequestParam(value = "status", required = false) QualityIncidentReport.IncidentStatus status,
            @RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles
    ) {
        try {
            QualityIncidentReportDTO reportDTO;

            // Determine the source of the DTO
            if (jsonBody != null && !jsonBody.isEmpty()) {
                // If pure JSON body is sent
                reportDTO = objectMapper.readValue(jsonBody, QualityIncidentReportDTO.class);
            } else if (dtoJson != null && !dtoJson.isEmpty()) {
                // If DTO is sent as a form-data parameter
                reportDTO = objectMapper.readValue(dtoJson, QualityIncidentReportDTO.class);
            } else {
                // Create a new DTO from form-data parameters
                reportDTO = new QualityIncidentReportDTO();
            }

            // Override DTO with form-data parameters if provided
            if (productId != null) reportDTO.setProductId(productId);
            if (incidentDate != null) reportDTO.setIncidentDate(LocalDateTime.parse(incidentDate));
            if (description != null) reportDTO.setDescription(description);
            if (correctiveActions != null) reportDTO.setCorrectiveActions(correctiveActions);
            if (status != null) reportDTO.setStatus(status);
            if (mediaFiles != null) reportDTO.setMediaFiles(mediaFiles);

            // Validate and create the report
            QualityIncidentReport createdReport = qualityIncidentReportService.createQualityIncidentReport(reportDTO);
            return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating report: " + e.getMessage());
        }
    }

    // Get a quality incident report by Incident ID
    @GetMapping("/{incidentId}")
    public ResponseEntity<?> getQualityIncidentReportByIncidentId(@PathVariable String incidentId) {
        try {
            QualityIncidentReport report = qualityIncidentReportService.getQualityIncidentReportByIncidentId(incidentId);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update a quality incident report
    @PutMapping(value = "/{incidentId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateQualityIncidentReport(
            @PathVariable String incidentId,
            @RequestBody(required = false) String jsonBody,
            @RequestParam(value = "dto", required = false) String dtoJson,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "correctiveActions", required = false) String correctiveActions,
            @RequestParam(value = "status", required = false) QualityIncidentReport.IncidentStatus status,
            @RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles
    ) {
        try {
            QualityIncidentReportDTO reportDTO;

            // Determine the source of the DTO
            if (jsonBody != null && !jsonBody.isEmpty()) {
                // If pure JSON body is sent
                reportDTO = objectMapper.readValue(jsonBody, QualityIncidentReportDTO.class);
            } else if (dtoJson != null && !dtoJson.isEmpty()) {
                // If DTO is sent as a form-data parameter
                reportDTO = objectMapper.readValue(dtoJson, QualityIncidentReportDTO.class);
            } else {
                // Create a new DTO from form-data parameters
                reportDTO = new QualityIncidentReportDTO();
            }

            // Override DTO with form-data parameters if provided
            if (description != null) reportDTO.setDescription(description);
            if (correctiveActions != null) reportDTO.setCorrectiveActions(correctiveActions);
            if (status != null) reportDTO.setStatus(status);
            if (mediaFiles != null) reportDTO.setMediaFiles(mediaFiles);

            // Update the report
            QualityIncidentReport updatedReport = qualityIncidentReportService.updateQualityIncidentReport(incidentId, reportDTO);
            return ResponseEntity.ok(updatedReport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating report: " + e.getMessage());
        }
    }

    // Delete a quality incident report
    @DeleteMapping("/{incidentId}")
    public ResponseEntity<?> deleteQualityIncidentReport(@PathVariable String incidentId) {
        try {
            qualityIncidentReportService.deleteQualityIncidentReport(incidentId);
            return ResponseEntity.ok("Quality Incident Report deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // List all quality incident reports
    @GetMapping
    public ResponseEntity<List<QualityIncidentReport>> getAllQualityIncidentReports() {
        List<QualityIncidentReport> reports = qualityIncidentReportService.getAllQualityIncidentReports();
        return ResponseEntity.ok(reports);
    }

    // List quality incident reports by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<QualityIncidentReport>> getQualityIncidentReportsByStatus(
            @PathVariable QualityIncidentReport.IncidentStatus status
    ) {
        List<QualityIncidentReport> reports = qualityIncidentReportService.getQualityIncidentReportsByStatus(status);
        return ResponseEntity.ok(reports);
    }

    // List quality incident reports by product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<QualityIncidentReport>> getQualityIncidentReportsByProduct(
            @PathVariable Long productId
    ) {
        List<QualityIncidentReport> reports = qualityIncidentReportService.getQualityIncidentReportsByProduct(productId);
        return ResponseEntity.ok(reports);
    }
}