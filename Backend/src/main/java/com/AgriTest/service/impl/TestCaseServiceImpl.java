package com.AgriTest.service.impl;

import com.AgriTest.dto.*;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.Product;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.TestPhase;
import com.AgriTest.model.TestResult;
import com.AgriTest.model.User;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.repository.TestCaseRepository;
import com.AgriTest.repository.TestPhaseRepository;
import com.AgriTest.repository.TestResultRepository;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.service.TestCaseService;
import com.AgriTest.util.SecurityUtils;
import com.AgriTest.mapper.TestCaseMapper;
import com.AgriTest.mapper.MediaFileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final UserRepository userRepository;
    private final TestCaseMapper testCaseMapper;
    private final MediaFileMapper mediaFileMapper;
    private static final Logger logger = LoggerFactory.getLogger(TestCaseServiceImpl.class);

    @Autowired
    public TestCaseServiceImpl(
            TestCaseRepository testCaseRepository,
            TestPhaseRepository testPhaseRepository,
            TestResultRepository testResultRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            TestCaseMapper testCaseMapper,
            MediaFileMapper mediaFileMapper) {
        this.testCaseRepository = testCaseRepository;
        this.testPhaseRepository = testPhaseRepository;
        this.testResultRepository = testResultRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.testCaseMapper = testCaseMapper;
        this.mediaFileMapper = mediaFileMapper;
    }

    @Override
    public List<TestCaseResponse> getAllTestCases() {
        List<TestCase> allTestCases = testCaseRepository.findAll();
        return allTestCases.stream()
                                .map(testCaseMapper::toDto)
                                .collect(Collectors.toList());
    }

    @Override
    public Optional<TestCaseResponse> getTestCaseById(Long id) {
        return testCaseRepository.findById(id)
                .map(this::mapTestCaseToTestCaseResponse);
    }

    @Override
    @Transactional
    public TestCaseResponse createTestCase(TestCaseRequest request, Long userId) {
        // Find a product based on name or product type
        Product product = productRepository.findByNameContainingIgnoreCase(request.getProductType())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    // Create new product if not found
                    Product newProduct = new Product();
                    newProduct.setName(request.getProductType());
                    // Store status if your Product entity has this field
                    newProduct.setStatus("TESTING");
                    // Store createdBy if your Product entity has this field
                    newProduct.setCreatedBy(userId);
                    return productRepository.save(newProduct);
                });
        
        // Find assigned worker
        User assignedWorker = userRepository.findById(request.getAssignedWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedWorkerId()));
        
        TestCase testCase = new TestCase();
        testCase.setProduct(product);
        testCase.setTestName(request.getTestName());
        testCase.setTestDescription(request.getTestDescription());
        testCase.setTestObjectives(request.getTestObjectives());
        testCase.setProductType(request.getProductType());
        testCase.setProductBatchNumber(request.getProductBatchNumber());
        testCase.setTestingLocation(request.getTestingLocation());
        testCase.setAssignedWorker(assignedWorker);
        testCase.setStartDate(request.getStartDate());
        testCase.setEndDate(request.getEndDate());
        testCase.setNotes(request.getNotes());
        testCase.setStatus("PENDING"); // Default status for new test cases
        testCase.setCreatedBy(userId);
        
        TestCase savedTestCase = testCaseRepository.save(testCase);
        return mapTestCaseToTestCaseResponse(savedTestCase);
    }

    @Override
    @Transactional
    public TestCaseResponse updateTestCase(Long id, TestCaseRequest request) {
        TestCase existingTestCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + id));
        
        // Update product based on product type
        if (!existingTestCase.getProductType().equals(request.getProductType())) {
            Product product = productRepository.findByNameContainingIgnoreCase(request.getProductType())
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        // Create new product if not found
                        Product newProduct = new Product();
                        newProduct.setName(request.getProductType());
                        newProduct.setStatus("TESTING");
                        newProduct.setCreatedBy(existingTestCase.getCreatedBy());
                        return productRepository.save(newProduct);
                    });
            
            existingTestCase.setProduct(product);
        }
        
        // Update assigned worker if changed
        if (!existingTestCase.getAssignedWorker().getId().equals(request.getAssignedWorkerId())) {
            User assignedWorker = userRepository.findById(request.getAssignedWorkerId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedWorkerId()));
            
            existingTestCase.setAssignedWorker(assignedWorker);
        }
        
        existingTestCase.setTestName(request.getTestName());
        existingTestCase.setTestDescription(request.getTestDescription());
        existingTestCase.setTestObjectives(request.getTestObjectives());
        existingTestCase.setProductType(request.getProductType());
        existingTestCase.setProductBatchNumber(request.getProductBatchNumber());
        existingTestCase.setTestingLocation(request.getTestingLocation());
        existingTestCase.setStartDate(request.getStartDate());
        existingTestCase.setEndDate(request.getEndDate());
        existingTestCase.setNotes(request.getNotes());
        
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
    
    private TestCaseResponse mapTestCaseToTestCaseResponse(TestCase testCase) {
        List<TestCaseResponse.TestPhaseDto> phaseDtos = null;
        if (testCase.getPhases() != null) {
            phaseDtos = testCase.getPhases().stream()
                    .map(phase -> TestCaseResponse.TestPhaseDto.builder()
                            .id(phase.getId())
                            .name(phase.getName())
                            .description(phase.getDescription())
                            .startDate(phase.getStartDate())
                            .endDate(phase.getEndDate())
                            .status(phase.getStatus())
                            .build())
                    .collect(Collectors.toList());
        }
        
        TestCaseResponse.UserDto assignedWorkerDto = null;
        if (testCase.getAssignedWorker() != null) {
            assignedWorkerDto = TestCaseResponse.UserDto.builder()
                    .id(testCase.getAssignedWorker().getId())
                    .username(testCase.getAssignedWorker().getUsername())
                    .fullName(testCase.getAssignedWorker().getFullName())
                    .email(testCase.getAssignedWorker().getEmail())
                    .build();
        }
        
        return TestCaseResponse.builder()
                .id(testCase.getId())
                .testName(testCase.getTestName())
                .testDescription(testCase.getTestDescription())
                .testObjectives(testCase.getTestObjectives())
                .productType(testCase.getProductType())
                .productBatchNumber(testCase.getProductBatchNumber())
                .testingLocation(testCase.getTestingLocation())
                .assignedWorker(assignedWorkerDto)
                .startDate(testCase.getStartDate())
                .endDate(testCase.getEndDate())
                .notes(testCase.getNotes())
                .status(testCase.getStatus())
                .createdBy(testCase.getCreatedBy())
                .createdAt(testCase.getCreatedAt())
                .updatedAt(testCase.getUpdatedAt())
                .phases(phaseDtos)
                .build();
    }
    
    private TestPhaseResponse mapTestPhaseToTestPhaseResponse(TestPhase testPhase) {
        // You will need to define TestPhaseResponse based on your requirements
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
                // Add other fields as needed
                .build();
    }
    
    private TestResultResponse mapTestResultToTestResultResponse(TestResult testResult) {
        // Map all fields from TestResult entity to TestResultResponse DTO
        List<MediaFileResponse> mediaFileResponses = null;
        if (testResult.getMediaFiles() != null) {
            mediaFileResponses = testResult.getMediaFiles().stream()
                .map(mediaFileMapper::toDto)
                .collect(Collectors.toList());
        }

        return TestResultResponse.builder()
                .id(testResult.getId())
                .testPhaseId(testResult.getTestPhase().getId()) // Assuming testPhase is not null
                .parameterName(testResult.getParameterName())
                .value(testResult.getValue())
                .unit(testResult.getUnit())
                .notes(testResult.getNotes())
                .recordedBy(testResult.getRecordedBy())
                .recordedAt(testResult.getRecordedAt())
                .productId(testResult.getProductId())
                .trialPhase(testResult.getTrialPhase())
                .growthRate(testResult.getGrowthRate())
                .yield(testResult.getYield())
                .pestResistance(testResult.getPestResistance())
                .finalVerdict(testResult.getFinalVerdict())
                .recommendations(testResult.getRecommendations())
                .approvedBy(testResult.getApprovedBy())
                .dateOfApproval(testResult.getDateOfApproval())
                .createdAt(testResult.getCreatedAt())
                .updatedAt(testResult.getUpdatedAt())
                .createdBy(testResult.getCreatedBy())
                .updatedBy(testResult.getUpdatedBy())
                .mediaFiles(mediaFileResponses)
                .build();
    }

    @Override
    public long getTotalTestCaseCount() {
        return testCaseRepository.count();
    }
}