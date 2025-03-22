package com.AgriTest.mapper;

import com.AgriTest.dto.TestCaseRequest;
import com.AgriTest.dto.TestCaseResponse;
import com.AgriTest.model.Product;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestCaseMapper {

    @Autowired
    private TestPhaseMapper testPhaseMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    public TestCaseResponse toDto(TestCase testCase) {
        if (testCase == null) {
            return null;
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
    
    public List<TestCaseResponse> toDtoList(List<TestCase> testCases) {
        if (testCases == null) {
            return null;
        }
        return testCases.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public TestCase toEntity(TestCaseRequest request, Product product, User assignedWorker, Long createdBy) {
        if (request == null) {
            return null;
        }
        
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
        testCase.setCreatedBy(createdBy);
        
        return testCase;
    }
    
    public void updateEntityFromDto(TestCaseRequest request, TestCase testCase, Product product, User assignedWorker) {
        if (request == null) {
            return;
        }
        
        if (product != null) {
            testCase.setProduct(product);
        }
        
        if (assignedWorker != null) {
            testCase.setAssignedWorker(assignedWorker);
        }
        
        testCase.setTestName(request.getTestName());
        testCase.setTestDescription(request.getTestDescription());
        testCase.setTestObjectives(request.getTestObjectives());
        testCase.setProductType(request.getProductType());
        testCase.setProductBatchNumber(request.getProductBatchNumber());
        testCase.setTestingLocation(request.getTestingLocation());
        testCase.setStartDate(request.getStartDate());
        testCase.setEndDate(request.getEndDate());
        testCase.setNotes(request.getNotes());
    }
}