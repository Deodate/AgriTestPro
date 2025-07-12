package com.AgriTest.mapper;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.model.TestSchedule;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestScheduleMapper {

    public TestSchedule toEntity(TestScheduleRequest request) {
        if (request == null) {
            return null;
        }
        
        TestSchedule testSchedule = new TestSchedule();
        testSchedule.setTestName(request.getTestName());
        testSchedule.setTestType(request.getTestType());
        testSchedule.setScheduledDate(request.getScheduledDate());
        testSchedule.setAssignedTo(request.getAssignedTo());
        testSchedule.setPriority(request.getPriority());
        testSchedule.setStatus(request.getStatus());
        testSchedule.setDescription(request.getDescription());
        
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