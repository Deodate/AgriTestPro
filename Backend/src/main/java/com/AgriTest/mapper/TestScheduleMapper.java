package com.AgriTest.mapper;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.model.TestSchedule;
import com.AgriTest.model.TestCase;
import com.AgriTest.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestScheduleMapper {

    @Autowired
    private TestCaseRepository testCaseRepository;

    public TestSchedule toEntity(TestScheduleRequest request) {
        if (request == null) {
            return null;
        }
        
        TestSchedule testSchedule = new TestSchedule();
        testSchedule.setTestName(request.getTestName());
        testSchedule.setScheduleName(request.getScheduleName());
        testSchedule.setTrialPhase(request.getTrialPhase());
        testSchedule.setAssignedPersonnel(request.getAssignedPersonnel());
        testSchedule.setLocation(request.getLocation());
        testSchedule.setTestObjective(request.getTestObjective());
        testSchedule.setEquipmentRequired(request.getEquipmentRequired());
        testSchedule.setNotificationPreference(request.getNotificationPreference());
        testSchedule.setNotes(request.getNotes());
        testSchedule.setFrequency(request.getFrequency());
        testSchedule.setDayOfMonth(request.getDayOfMonth());
        testSchedule.setDayOfWeek(request.getDayOfWeek());
        testSchedule.setStartDate(request.getStartDate());
        testSchedule.setEndDate(request.getEndDate());
        testSchedule.setIsActive(request.getIsActive());
        testSchedule.setDescription(request.getDescription());
        testSchedule.setPriority(request.getPriority());
        testSchedule.setStatus(request.getStatus());
        testSchedule.setCreatedBy(request.getCreatedBy());

        // Handle test case relationship
        if (request.getTestCaseId() != null) {
            TestCase testCase = testCaseRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new EntityNotFoundException("TestCase not found with id: " + request.getTestCaseId()));
            testSchedule.setTestCase(testCase);
        }
        
        return testSchedule;
    }

    public List<TestSchedule> toEntityList(List<TestScheduleRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}