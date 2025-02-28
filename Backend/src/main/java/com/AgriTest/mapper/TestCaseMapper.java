// File: src/main/java/com/AgriTest/mapper/TestCaseMapper.java
package com.AgriTest.mapper;

import com.AgriTest.dto.TestCaseRequest;
import com.AgriTest.dto.TestCaseResponse;
import com.AgriTest.model.Product;
import com.AgriTest.model.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestCaseMapper {

    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private TestPhaseMapper testPhaseMapper;
    
    public TestCaseResponse toDto(TestCase testCase) {
        if (testCase == null) {
            return null;
        }
        
        return TestCaseResponse.builder()
                .id(testCase.getId())
                .product(productMapper.toDto(testCase.getProduct()))
                .title(testCase.getTitle())
                .description(testCase.getDescription())
                .methodology(testCase.getMethodology())
                .startDate(testCase.getStartDate())
                .endDate(testCase.getEndDate())
                .status(testCase.getStatus())
                .createdBy(testCase.getCreatedBy())
                .createdAt(testCase.getCreatedAt())
                .updatedAt(testCase.getUpdatedAt())
                .phases(testPhaseMapper.toDtoList(testCase.getPhases()))
                .build();
    }
    
    public List<TestCaseResponse> toDtoList(List<TestCase> testCases) {
        return testCases.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public TestCase toEntity(TestCaseRequest testCaseRequest, Product product) {
        if (testCaseRequest == null) {
            return null;
        }
        
        TestCase testCase = new TestCase();
        testCase.setProduct(product);
        testCase.setTitle(testCaseRequest.getTitle());
        testCase.setDescription(testCaseRequest.getDescription());
        testCase.setMethodology(testCaseRequest.getMethodology());
        testCase.setStartDate(testCaseRequest.getStartDate());
        testCase.setEndDate(testCaseRequest.getEndDate());
        testCase.setStatus(testCaseRequest.getStatus());
        
        return testCase;
    }
    
    public void updateEntityFromDto(TestCaseRequest testCaseRequest, TestCase testCase, Product product) {
        if (testCaseRequest == null) {
            return;
        }
        
        testCase.setProduct(product);
        testCase.setTitle(testCaseRequest.getTitle());
        testCase.setDescription(testCaseRequest.getDescription());
        testCase.setMethodology(testCaseRequest.getMethodology());
        testCase.setStartDate(testCaseRequest.getStartDate());
        testCase.setEndDate(testCaseRequest.getEndDate());
        testCase.setStatus(testCaseRequest.getStatus());
    }
}