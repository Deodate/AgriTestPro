package com.AgriTest.service.impl;

import com.AgriTest.dto.*;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.Product;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.TestPhase;
import com.AgriTest.model.TestResult;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.repository.TestCaseRepository;
import com.AgriTest.repository.TestPhaseRepository;
import com.AgriTest.repository.TestResultRepository;
import com.AgriTest.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TestPhaseRepository testPhaseRepository;
    private final TestResultRepository testResultRepository;
    private final ProductRepository productRepository;

    @Autowired
    public TestCaseServiceImpl(
            TestCaseRepository testCaseRepository,
            TestPhaseRepository testPhaseRepository,
            TestResultRepository testResultRepository,
            ProductRepository productRepository) {
        this.testCaseRepository = testCaseRepository;
        this.testPhaseRepository = testPhaseRepository;
        this.testResultRepository = testResultRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<TestCaseResponse> getAllTestCases() {
        return testCaseRepository.findAll().stream()
                .map(this::mapTestCaseToTestCaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TestCaseResponse> getTestCaseById(Long id) {
        return testCaseRepository.findById(id)
                .map(this::mapTestCaseToTestCaseResponse);
    }

    @Override
    @Transactional
    public TestCaseResponse createTestCase(TestCaseRequest testCaseRequest, Long userId) {
        Product product = productRepository.findById(testCaseRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + testCaseRequest.getProductId()));
        
        TestCase testCase = new TestCase();
        testCase.setProduct(product);
        testCase.setTitle(testCaseRequest.getTitle());
        testCase.setDescription(testCaseRequest.getDescription());
        testCase.setMethodology(testCaseRequest.getMethodology());
        testCase.setStartDate(testCaseRequest.getStartDate());
        testCase.setEndDate(testCaseRequest.getEndDate());
        testCase.setStatus(testCaseRequest.getStatus());
        testCase.setCreatedBy(userId);
        
        TestCase savedTestCase = testCaseRepository.save(testCase);
        return mapTestCaseToTestCaseResponse(savedTestCase);
    }

    @Override
    @Transactional
    public TestCaseResponse updateTestCase(Long id, TestCaseRequest testCaseRequest) {
        TestCase existingTestCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + id));
        
        Product product = productRepository.findById(testCaseRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + testCaseRequest.getProductId()));
        
        existingTestCase.setProduct(product);
        existingTestCase.setTitle(testCaseRequest.getTitle());
        existingTestCase.setDescription(testCaseRequest.getDescription());
        existingTestCase.setMethodology(testCaseRequest.getMethodology());
        existingTestCase.setStartDate(testCaseRequest.getStartDate());
        existingTestCase.setEndDate(testCaseRequest.getEndDate());
        existingTestCase.setStatus(testCaseRequest.getStatus());
        
        TestCase updatedTestCase = testCaseRepository.save(existingTestCase);
        return mapTestCaseToTestCaseResponse(updatedTestCase);
    }

    @Override
    @Transactional
    public void deleteTestCase(Long id) {
        testCaseRepository.deleteById(id);
    }

    @Override
    public List<TestCaseResponse> getTestCasesByProduct(Long productId) {
        return testCaseRepository.findByProductId(productId).stream()
                .map(this::mapTestCaseToTestCaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestCaseResponse> getTestCasesByStatus(String status) {
        return testCaseRepository.findByStatus(status).stream()
                .map(this::mapTestCaseToTestCaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestCaseResponse> getTestCasesByUser(Long userId) {
        return testCaseRepository.findByCreatedBy(userId).stream()
                .map(this::mapTestCaseToTestCaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TestPhaseResponse addTestPhase(Long testCaseId, TestPhaseRequest testPhaseRequest) {
        TestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + testCaseId));
        
        TestPhase testPhase = new TestPhase();
        testPhase.setTestCase(testCase);
        testPhase.setName(testPhaseRequest.getName());
        testPhase.setDescription(testPhaseRequest.getDescription());
        testPhase.setStartDate(testPhaseRequest.getStartDate());
        testPhase.setEndDate(testPhaseRequest.getEndDate());
        testPhase.setStatus(testPhaseRequest.getStatus());
        
        TestPhase savedTestPhase = testPhaseRepository.save(testPhase);
        return mapTestPhaseToTestPhaseResponse(savedTestPhase);
    }

    @Override
    @Transactional
    public TestResultResponse addTestResult(Long phaseId, TestResultRequest testResultRequest, Long userId) {
        TestPhase testPhase = testPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Test phase not found with id: " + phaseId));
        
        TestResult testResult = new TestResult();
        testResult.setTestPhase(testPhase);
        testResult.setParameterName(testResultRequest.getParameterName());
        testResult.setValue(testResultRequest.getValue());
        testResult.setUnit(testResultRequest.getUnit());
        testResult.setNotes(testResultRequest.getNotes());
        testResult.setRecordedBy(userId);
        
        TestResult savedTestResult = testResultRepository.save(testResult);
        return mapTestResultToTestResultResponse(savedTestResult);
    }
    
    @Override
    public List<TestCaseResponse> getTestCasesByIds(List<Long> testCaseIds) {
        List<TestCase> testCases = testCaseRepository.findAllById(testCaseIds);
        return testCases.stream()
                .map(this::mapTestCaseToTestCaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestResultResponse> getTestResultsByTestCase(Long testCaseId) {
        TestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + testCaseId));
        
        List<TestResult> results = new ArrayList<>();
        
        // Collect results from all phases of this test case
        for (TestPhase phase : testCase.getPhases()) {
            results.addAll(phase.getResults());
        }
        
        return results.stream()
                .map(this::mapTestResultToTestResultResponse)
                .collect(Collectors.toList());
    }
    
    private ProductResponse mapProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .manufacturer(product.getManufacturer())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
    
    private TestCaseResponse mapTestCaseToTestCaseResponse(TestCase testCase) {
        List<TestPhaseResponse> phases = testCase.getPhases().stream()
                .map(this::mapTestPhaseToTestPhaseResponse)
                .collect(Collectors.toList());
        
        return TestCaseResponse.builder()
                .id(testCase.getId())
                .product(mapProductToProductResponse(testCase.getProduct()))
                .title(testCase.getTitle())
                .description(testCase.getDescription())
                .methodology(testCase.getMethodology())
                .startDate(testCase.getStartDate())
                .endDate(testCase.getEndDate())
                .status(testCase.getStatus())
                .createdBy(testCase.getCreatedBy())
                .createdAt(testCase.getCreatedAt())
                .updatedAt(testCase.getUpdatedAt())
                .phases(phases)
                .build();
    }
    
    private TestPhaseResponse mapTestPhaseToTestPhaseResponse(TestPhase testPhase) {
        List<TestResultResponse> results = testPhase.getResults().stream()
                .map(this::mapTestResultToTestResultResponse)
                .collect(Collectors.toList());
        
        return TestPhaseResponse.builder()
                .id(testPhase.getId())
                .testCaseId(testPhase.getTestCase().getId())
                .name(testPhase.getName())
                .description(testPhase.getDescription())
                .startDate(testPhase.getStartDate())
                .endDate(testPhase.getEndDate())
                .status(testPhase.getStatus())
                .createdAt(testPhase.getCreatedAt())
                .updatedAt(testPhase.getUpdatedAt())
                .results(results)
                .build();
    }
    
    private TestResultResponse mapTestResultToTestResultResponse(TestResult testResult) {
        return TestResultResponse.builder()
                .id(testResult.getId())
                .testPhaseId(testResult.getTestPhase().getId())
                .parameterName(testResult.getParameterName())
                .value(testResult.getValue())
                .unit(testResult.getUnit())
                .notes(testResult.getNotes())
                .recordedBy(testResult.getRecordedBy())
                .recordedAt(testResult.getRecordedAt())
                .mediaFiles(new ArrayList<>()) // This would need to be populated if you have media files
                .build();
    }
}