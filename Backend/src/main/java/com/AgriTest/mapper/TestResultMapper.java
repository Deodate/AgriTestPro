// File: src/main/java/com/AgriTest/mapper/TestResultMapper.java
package com.AgriTest.mapper;

import com.AgriTest.dto.TestResultRequest;
import com.AgriTest.dto.TestResultResponse;
import com.AgriTest.model.TestPhase;
import com.AgriTest.model.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestResultMapper {

    @Autowired
    private MediaFileMapper mediaFileMapper;
    
    public TestResultResponse toDto(TestResult testResult) {
        if (testResult == null) {
            return null;
        }
        
        return TestResultResponse.builder()
                .id(testResult.getId())
                .testPhaseId(testResult.getTestPhase().getId())
                .parameterName(testResult.getParameterName())
                .value(testResult.getValue())
                .unit(testResult.getUnit())
                .notes(testResult.getNotes())
                .recordedBy(testResult.getRecordedBy())
                .recordedAt(testResult.getRecordedAt())
                .mediaFiles(mediaFileMapper.toDtoList(testResult.getMediaFiles()))
                .build();
    }
    
    public List<TestResultResponse> toDtoList(List<TestResult> testResults) {
        if (testResults == null) {
            return List.of();
        }
        return testResults.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public TestResult toEntity(TestResultRequest testResultRequest, TestPhase testPhase, Long userId) {
        if (testResultRequest == null) {
            return null;
        }
        
        TestResult testResult = new TestResult();
        testResult.setTestPhase(testPhase);
        testResult.setParameterName(testResultRequest.getParameterName());
        testResult.setValue(testResultRequest.getValue());
        testResult.setUnit(testResultRequest.getUnit());
        testResult.setNotes(testResultRequest.getNotes());
        testResult.setRecordedBy(userId);
        
        return testResult;
    }
}