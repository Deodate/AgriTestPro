// File: src/main/java/com/AgriTest/mapper/TestPhaseMapper.java
package com.AgriTest.mapper;

import com.AgriTest.dto.TestPhaseRequest;
import com.AgriTest.dto.TestPhaseResponse;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.TestPhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestPhaseMapper {

    @Autowired
    private TestResultMapper testResultMapper;
    
    public TestPhaseResponse toDto(TestPhase testPhase) {
        if (testPhase == null) {
            return null;
        }
        
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
                .results(testResultMapper.toDtoList(testPhase.getResults()))
                .build();
    }
    
    public List<TestPhaseResponse> toDtoList(List<TestPhase> testPhases) {
        if (testPhases == null) {
            return List.of();
        }
        return testPhases.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public TestPhase toEntity(TestPhaseRequest testPhaseRequest, TestCase testCase) {
        if (testPhaseRequest == null) {
            return null;
        }
        
        TestPhase testPhase = new TestPhase();
        testPhase.setTestCase(testCase);
        testPhase.setName(testPhaseRequest.getName());
        testPhase.setDescription(testPhaseRequest.getDescription());
        testPhase.setStartDate(testPhaseRequest.getStartDate());
        testPhase.setEndDate(testPhaseRequest.getEndDate());
        testPhase.setStatus(testPhaseRequest.getStatus());
        
        return testPhase;
    }
}