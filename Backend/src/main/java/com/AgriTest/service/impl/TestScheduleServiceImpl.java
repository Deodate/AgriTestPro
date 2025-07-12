package com.AgriTest.service.impl;

import com.AgriTest.dto.TestScheduleRequest;
import com.AgriTest.model.TestSchedule;
import com.AgriTest.repository.TestScheduleRepository;
import com.AgriTest.service.TestScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestScheduleServiceImpl implements TestScheduleService {

    private final TestScheduleRepository testScheduleRepository;

    @Autowired
    public TestScheduleServiceImpl(TestScheduleRepository testScheduleRepository) {
        this.testScheduleRepository = testScheduleRepository;
    }

    @Override
    public TestSchedule createTestSchedule(TestScheduleRequest request) {
        TestSchedule testSchedule = new TestSchedule();
        testSchedule.setTestName(request.getTestName());
        testSchedule.setTestType(request.getTestType());
        testSchedule.setScheduledDate(request.getScheduledDate());
        testSchedule.setAssignedTo(request.getAssignedTo());
        testSchedule.setPriority(request.getPriority());
        testSchedule.setStatus(request.getStatus());
        testSchedule.setDescription(request.getDescription());
        
        return testScheduleRepository.save(testSchedule);
    }

    @Override
    public List<TestSchedule> getAllTestSchedules() {
        return testScheduleRepository.findAll();
    }

    @Override
    public TestSchedule getTestScheduleById(Long id) {
        return testScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Test Schedule not found with id: " + id));
    }

    @Override
    public TestSchedule updateTestSchedule(Long id, TestScheduleRequest request) {
        TestSchedule existingSchedule = getTestScheduleById(id);
        
        existingSchedule.setTestName(request.getTestName());
        existingSchedule.setTestType(request.getTestType());
        existingSchedule.setScheduledDate(request.getScheduledDate());
        existingSchedule.setAssignedTo(request.getAssignedTo());
        existingSchedule.setPriority(request.getPriority());
        existingSchedule.setStatus(request.getStatus());
        existingSchedule.setDescription(request.getDescription());
        
        return testScheduleRepository.save(existingSchedule);
    }

    @Override
    public void deleteTestSchedule(Long id) {
        if (!testScheduleRepository.existsById(id)) {
            throw new EntityNotFoundException("Test Schedule not found with id: " + id);
        }
        testScheduleRepository.deleteById(id);
    }

    @Override
    public List<TestSchedule> getTestSchedulesByIds(List<Long> scheduleIds) {
        return testScheduleRepository.findAllById(scheduleIds);
    }
}