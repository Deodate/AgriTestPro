package com.AgriTest.service.impl;

import com.AgriTest.dto.ReportGenerationRequest;
import com.AgriTest.dto.ReportGenerationResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.ReportGenerationMapper;
import com.AgriTest.model.ReportGeneration;
import com.AgriTest.repository.ReportGenerationRepository;
import com.AgriTest.service.ReportGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportGenerationServiceImpl implements ReportGenerationService {
    
    @Autowired
    private ReportGenerationRepository reportGenerationRepository;
    
    @Autowired
    private ReportGenerationMapper reportGenerationMapper;
    
    @Override
    @Transactional
    public ReportGenerationResponse createReportGeneration(ReportGenerationRequest request) {
        // Convert request to entity
        ReportGeneration reportGeneration = reportGenerationMapper.toEntity(request);
        
        // Save the report generation
        ReportGeneration savedReportGeneration = reportGenerationRepository.save(reportGeneration);
        
        // Convert and return the response
        return reportGenerationMapper.toDto(savedReportGeneration);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReportGenerationResponse> getAllReportGenerations() {
        return reportGenerationRepository.findAll().stream()
                .map(reportGenerationMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ReportGenerationResponse getReportGenerationById(Long id) {
        ReportGeneration reportGeneration = reportGenerationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report generation not found with id: " + id));
        
        return reportGenerationMapper.toDto(reportGeneration);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReportGenerationResponse> getReportsByType(ReportGeneration.ReportType reportType) {
        return reportGenerationRepository.findByReportType(reportType).stream()
                .map(reportGenerationMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReportGenerationResponse> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // Validate and set default dates if not provided
        if (startDate == null || endDate == null) {
            LocalDateTime now = LocalDateTime.now();
            startDate = now.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
            endDate = now.withMonth(12).withDayOfMonth(31).withHour(23).withMinute(59).withSecond(59);
        }
        
        // Ensure start date is before or equal to end date
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        
        // Retrieve reports within the date range
        List<ReportGeneration> reports = reportGenerationRepository.findReportsByDateRange(startDate, endDate);
        
        // Convert to DTOs
        return reports.stream()
                .map(reportGenerationMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ReportGenerationResponse updateReportGeneration(Long id, ReportGenerationRequest request) {
        // Find the existing report generation
        ReportGeneration existingReportGeneration = reportGenerationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report generation not found with id: " + id));
        
        // Update the existing report generation with new values
        existingReportGeneration.setReportType(request.getReportType());
        existingReportGeneration.setStartDate(request.getStartDate());
        existingReportGeneration.setEndDate(request.getEndDate());
        existingReportGeneration.setProductType(request.getProductType());
        existingReportGeneration.setLocation(request.getLocation());
        existingReportGeneration.setStatus(request.getStatus());
        existingReportGeneration.setReportFormat(request.getReportFormat());
        existingReportGeneration.setEmailReport(request.getEmailReport());
        
        // Save the updated report generation
        ReportGeneration updatedReportGeneration = reportGenerationRepository.save(existingReportGeneration);
        
        // Convert and return the response
        return reportGenerationMapper.toDto(updatedReportGeneration);
    }
    
    @Override
    @Transactional
    public void deleteReportGeneration(Long id) {
        // Find the report generation
        ReportGeneration reportGeneration = reportGenerationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report generation not found with id: " + id));
        
        // Delete the report generation
        reportGenerationRepository.delete(reportGeneration);
    }
}