// File: src/main/java/com/AgriTest/service/impl/ReportScheduleServiceImpl.java
package com.AgriTest.service.impl;

import com.AgriTest.dto.ReportScheduleRequest;
import com.AgriTest.dto.ReportScheduleResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.ExportFormat;
import com.AgriTest.model.ReportSchedule;
import com.AgriTest.model.ReportType;
import com.AgriTest.repository.ReportScheduleRepository;
import com.AgriTest.service.ExportService;
import com.AgriTest.service.ReportScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportScheduleServiceImpl implements ReportScheduleService {

    private final ReportScheduleRepository reportScheduleRepository;
    private final ExportService exportService;
    private static final Logger logger = LoggerFactory.getLogger(ReportScheduleServiceImpl.class);

    @Autowired
    public ReportScheduleServiceImpl(
            ReportScheduleRepository reportScheduleRepository,
            ExportService exportService) {
        this.reportScheduleRepository = reportScheduleRepository;
        this.exportService = exportService;
    }

    @Override
    public List<ReportScheduleResponse> getAllReportSchedules() {
        return reportScheduleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReportScheduleResponse> getReportScheduleById(Long id) {
        return reportScheduleRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public ReportScheduleResponse createReportSchedule(ReportScheduleRequest request, Long userId) {
        ReportSchedule reportSchedule = mapToEntity(request);
        reportSchedule.setCreatedBy(userId);
        
        // Set initial next execution date
        if (request.getStartDate().isBefore(LocalDate.now())) {
            reportSchedule.setNextExecution(LocalDate.now());
        } else {
            reportSchedule.setNextExecution(request.getStartDate());
        }
        reportSchedule.calculateNextExecution();
        
        ReportSchedule savedSchedule = reportScheduleRepository.save(reportSchedule);
        return mapToResponse(savedSchedule);
    }

    @Override
    @Transactional
    public ReportScheduleResponse updateReportSchedule(Long id, ReportScheduleRequest request) {
        ReportSchedule existingSchedule = reportScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report schedule not found with id: " + id));
        
        existingSchedule.setName(request.getName());
        existingSchedule.setDescription(request.getDescription());
        existingSchedule.setReportType(request.getReportType());
        existingSchedule.setExportFormat(request.getExportFormat());
        existingSchedule.setEntityIds(request.getEntityIds());
        existingSchedule.setRecipients(request.getRecipients());
        existingSchedule.setScheduleTime(request.getScheduleTime());
        existingSchedule.setFrequency(request.getFrequency());
        existingSchedule.setDayOfWeek(request.getDayOfWeek());
        existingSchedule.setDayOfMonth(request.getDayOfMonth());
        
        // Update date fields
        existingSchedule.setStartDate(request.getStartDate());
        existingSchedule.setEndDate(request.getEndDate());
        
        // Recalculate next execution date
        if (request.getStartDate().isBefore(LocalDate.now())) {
            existingSchedule.setNextExecution(LocalDate.now());
        } else if (existingSchedule.getNextExecution().isBefore(request.getStartDate())) {
            existingSchedule.setNextExecution(request.getStartDate());
        }
        existingSchedule.calculateNextExecution();
        
        ReportSchedule updatedSchedule = reportScheduleRepository.save(existingSchedule);
        return mapToResponse(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteReportSchedule(Long id) {
        reportScheduleRepository.deleteById(id);
    }

    @Override
    public List<ReportScheduleResponse> getReportSchedulesByUser(Long userId) {
        return reportScheduleRepository.findByCreatedBy(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportScheduleResponse> getReportSchedulesByType(ReportType reportType) {
        return reportScheduleRepository.findByReportType(reportType.name()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReportScheduleResponse activateReportSchedule(Long id) {
        ReportSchedule schedule = reportScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report schedule not found with id: " + id));
        
        schedule.setIsActive(true);
        
        // Reset next execution date if needed
        if (schedule.getNextExecution().isBefore(LocalDate.now())) {
            schedule.setNextExecution(LocalDate.now());
            schedule.calculateNextExecution();
        }
        
        ReportSchedule updatedSchedule = reportScheduleRepository.save(schedule);
        return mapToResponse(updatedSchedule);
    }

    @Override
    @Transactional
    public ReportScheduleResponse deactivateReportSchedule(Long id) {
        ReportSchedule schedule = reportScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report schedule not found with id: " + id));
        
        schedule.setIsActive(false);
        ReportSchedule updatedSchedule = reportScheduleRepository.save(schedule);
        return mapToResponse(updatedSchedule);
    }

    @Override
    @Transactional
    public void processDueSchedules() {
        List<ReportSchedule> dueSchedules = reportScheduleRepository.findDueSchedules(LocalDate.now());
        
        for (ReportSchedule schedule : dueSchedules) {
            try {
                // Generate and send the report
                executeSchedule(schedule);
                
                // Update next execution date
                schedule.calculateNextExecution();
                reportScheduleRepository.save(schedule);
            } catch (Exception e) {
                // Log error but continue processing other schedules
                System.err.println("Error processing schedule " + schedule.getId() + ": " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void executeScheduleNow(Long id) {
        ReportSchedule schedule = reportScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report schedule not found with id: " + id));
        
        executeSchedule(schedule);
    }
    
    // Helper method to execute a single schedule
    private void executeSchedule(ReportSchedule schedule) {
        // Generate the report based on report type
        byte[] reportData;
        String reportFileName;
        String reportName = schedule.getName();
        ExportFormat format = schedule.getExportFormat();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        
        switch (schedule.getReportType()) {
            case TEST_CASES:
                reportData = exportService.exportTestCases(format, schedule.getEntityIds().isEmpty() ? null : schedule.getEntityIds().stream().toList());
                reportFileName = "test_cases_" + timestamp + getFileExtension(format);
                break;
            case TEST_SCHEDULES:
                reportData = exportService.exportTestSchedules(format, schedule.getEntityIds().isEmpty() ? null : schedule.getEntityIds().stream().toList());
                reportFileName = "test_schedules_" + timestamp + getFileExtension(format);
                break;
            case TEST_RESULTS:
                if (schedule.getEntityIds().isEmpty()) {
                    throw new IllegalStateException("Entity ID is required for test results report");
                }
                // Assume we're only using the first entity ID for test results report (the test case ID)
                Long testCaseId = schedule.getEntityIds().iterator().next();
                reportData = exportService.exportTestResults(format, testCaseId);
                reportFileName = "test_results_" + timestamp + getFileExtension(format);
                break;
            default:
                throw new IllegalStateException("Unsupported report type: " + schedule.getReportType());
        }
        
        // Save the report to a file or storage system instead of sending via email
        // TODO: Implement report storage mechanism
        logger.info("Report {} generated successfully. File name: {}", reportName, reportFileName);
    }
    
    // Helper method to map entity to response DTO
    private ReportScheduleResponse mapToResponse(ReportSchedule entity) {
        return ReportScheduleResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .reportType(entity.getReportType())
                .exportFormat(entity.getExportFormat())
                .entityIds(entity.getEntityIds())
                .recipients(entity.getRecipients())
                .scheduleTime(entity.getScheduleTime())
                .frequency(entity.getFrequency())
                .dayOfWeek(entity.getDayOfWeek())
                .dayOfMonth(entity.getDayOfMonth())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .nextExecution(entity.getNextExecution())
                .active(entity.getIsActive())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    // Helper method to map request DTO to entity
    private ReportSchedule mapToEntity(ReportScheduleRequest request) {
        return ReportSchedule.builder()
                .name(request.getName())
                .description(request.getDescription())
                .reportType(request.getReportType())
                .exportFormat(request.getExportFormat())
                .entityIds(request.getEntityIds())
                .recipients(request.getRecipients())
                .scheduleTime(request.getScheduleTime())
                .frequency(request.getFrequency())
                .dayOfWeek(request.getDayOfWeek())
                .dayOfMonth(request.getDayOfMonth())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isActive(true) // New schedules are active by default
                .build();
    }
    
    // Helper method to get file extension based on format
    private String getFileExtension(ExportFormat format) {
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
    
    // Helper method to get MIME type based on format
    private String getMimeType(ExportFormat format) {
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