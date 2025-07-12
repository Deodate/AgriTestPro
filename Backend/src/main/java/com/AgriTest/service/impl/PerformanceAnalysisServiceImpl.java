package com.AgriTest.service.impl;

import com.AgriTest.dto.PerformanceAnalysisMetricsResponse;
import com.AgriTest.dto.PerformanceAnalysisRequest;
import com.AgriTest.dto.PerformanceAnalysisResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.PerformanceAnalysisMapper;
import com.AgriTest.model.PerformanceAnalysis;
import com.AgriTest.model.Product;
import com.AgriTest.repository.PerformanceAnalysisRepository;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.service.PerformanceAnalysisService;
import com.AgriTest.util.PerformanceAnalysisMetricsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PerformanceAnalysisServiceImpl implements PerformanceAnalysisService {
    
    @Autowired
    private PerformanceAnalysisRepository performanceAnalysisRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PerformanceAnalysisMapper performanceAnalysisMapper;
    
    @Override
    @Transactional
    public PerformanceAnalysisResponse createPerformanceAnalysis(PerformanceAnalysisRequest request) {
        // Find the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        
        // Create performance analysis entity
        PerformanceAnalysis performanceAnalysis = performanceAnalysisMapper.toEntity(request, product);
        
        // Save the performance analysis
        PerformanceAnalysis savedAnalysis = performanceAnalysisRepository.save(performanceAnalysis);
        
        // Return the DTO
        return performanceAnalysisMapper.toDto(savedAnalysis);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PerformanceAnalysisResponse> getAllPerformanceAnalyses() {
        return performanceAnalysisRepository.findAll().stream()
                .map(performanceAnalysisMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PerformanceAnalysisResponse getPerformanceAnalysisById(Long id) {
        PerformanceAnalysis performanceAnalysis = performanceAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance analysis not found with id: " + id));
        
        return performanceAnalysisMapper.toDto(performanceAnalysis);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PerformanceAnalysisResponse> getPerformanceAnalysesByProductId(Long productId) {
        // Verify the product exists
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        
        return performanceAnalysisRepository.findByProductId(productId).stream()
                .map(performanceAnalysisMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PerformanceAnalysisResponse> getPerformanceAnalysesByTestCaseId(String testCaseId) {
        return performanceAnalysisRepository.findByTestCaseId(testCaseId).stream()
                .map(performanceAnalysisMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PerformanceAnalysisResponse> getPerformanceAnalysesByDateRange(LocalDate startDate, LocalDate endDate) {
        // Validate input dates
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Both start and end dates must be provided");
        }
        
        // Ensure start date is before or equal to end date
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        
        // Retrieve performance analyses within the date range
        return performanceAnalysisRepository.findByTrialDateBetween(startDate, endDate).stream()
                .map(performanceAnalysisMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PerformanceAnalysisResponse> getTopPerformingAnalyses(int limit) {
        // Validate limit
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be a positive number");
        }
        
        // Retrieve top performing analyses
        return performanceAnalysisRepository.findTopPerformingAnalyses(limit).stream()
                .map(performanceAnalysisMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public PerformanceAnalysisResponse updatePerformanceAnalysis(Long id, PerformanceAnalysisRequest request) {
        // Find the existing performance analysis
        PerformanceAnalysis existingAnalysis = performanceAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance analysis not found with id: " + id));
        
        // Find the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        
        // Update the existing performance analysis
        existingAnalysis.setProduct(product);
        existingAnalysis.setTestCaseId(request.getTestCaseId());
        existingAnalysis.setTrialResults(request.getTrialResults());
        existingAnalysis.setSeasonalPerformanceMetrics(request.getSeasonalPerformanceMetrics());
        existingAnalysis.setEffectivenessRating(request.getEffectivenessRating());
        existingAnalysis.setPreviousSeasonComparison(request.getPreviousSeasonComparison());
        existingAnalysis.setTrialDate(request.getTrialDate());
        
        // Save the updated performance analysis
        PerformanceAnalysis updatedAnalysis = performanceAnalysisRepository.save(existingAnalysis);
        
        // Return the updated DTO
        return performanceAnalysisMapper.toDto(updatedAnalysis);
    }
    
    @Override
    @Transactional
    public void deletePerformanceAnalysis(Long id) {
        // Find the performance analysis
        PerformanceAnalysis performanceAnalysis = performanceAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance analysis not found with id: " + id));
        
        // Delete the performance analysis
        performanceAnalysisRepository.delete(performanceAnalysis);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PerformanceAnalysisMetricsResponse calculatePerformanceMetrics(Long id) {
        // Find the performance analysis
        PerformanceAnalysis performanceAnalysis = performanceAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance analysis not found with id: " + id));
        
        // Extract metrics
        Map<String, Double> metrics = PerformanceAnalysisMetricsUtil.extractPerformanceMetrics(
            performanceAnalysis.getTrialResults(), 
            performanceAnalysis.getSeasonalPerformanceMetrics()
        );
        
        // Calculate performance score
        double performanceScore = PerformanceAnalysisMetricsUtil.calculatePerformanceScore(
            performanceAnalysis.getEffectivenessRating(), 
            metrics
        );
        
        // Create response
        PerformanceAnalysisMetricsResponse response = new PerformanceAnalysisMetricsResponse();
        response.setMetrics(metrics);
        response.setPerformanceScore(performanceScore);
        response.setEffectivenessRating(performanceAnalysis.getEffectivenessRating());
        
        return response;
    }
    
    @Override
    public PerformanceAnalysisMetricsResponse calculatePerformanceMetricsFromRequest(
            PerformanceAnalysisRequest request) {
        // Extract metrics
        Map<String, Double> metrics = PerformanceAnalysisMetricsUtil.extractPerformanceMetrics(
            request.getTrialResults(), 
            request.getSeasonalPerformanceMetrics()
        );
        
        // Calculate performance score
        double performanceScore = PerformanceAnalysisMetricsUtil.calculatePerformanceScore(
            request.getEffectivenessRating(), 
            metrics
        );
        
        // Create response
        PerformanceAnalysisMetricsResponse response = new PerformanceAnalysisMetricsResponse();
        response.setMetrics(metrics);
        response.setPerformanceScore(performanceScore);
        response.setEffectivenessRating(request.getEffectivenessRating());
        
        return response;
    }
}