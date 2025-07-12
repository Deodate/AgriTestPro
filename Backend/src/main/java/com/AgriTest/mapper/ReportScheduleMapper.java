package com.AgriTest.mapper;

import com.AgriTest.dto.ReportScheduleRequest;
import com.AgriTest.dto.ReportScheduleResponse;
import com.AgriTest.model.ReportSchedule;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReportScheduleMapper {

    public ReportSchedule toEntity(ReportScheduleRequest request, Long userId) {
        ReportSchedule schedule = ReportSchedule.builder()
                .name(request.getName())
                .description(request.getDescription())
                .reportType(request.getReportType())
                .exportFormat(request.getExportFormat())
                .entityIds(request.getEntityIds())
                .recipients(request.getRecipients())
                .scheduleTime(request.getScheduleTime())
                .frequency(request.getFrequency())
                .dayOfWeek(request.getDayOfWeek())
                .dayOfMonth(request.getDayOfMonth())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isActive(true)
                .createdBy(userId)
                .build();

        // Set next execution based on start date or current date
        LocalDate nextExecution = request.getStartDate();
        if (nextExecution.isBefore(LocalDate.now())) {
            nextExecution = LocalDate.now();
            schedule.setNextExecution(nextExecution);
            schedule.calculateNextExecution();
        } else {
            schedule.setNextExecution(nextExecution);
        }

        return schedule;
    }

    public ReportScheduleResponse toDto(ReportSchedule entity) {
        return ReportScheduleResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .reportType(entity.getReportType())
                .exportFormat(entity.getExportFormat())
                .entityIds(entity.getEntityIds())
                .recipients(entity.getRecipients())
                .scheduleTime(entity.getScheduleTime())
                .frequency(entity.getFrequency())
                .dayOfWeek(entity.getDayOfWeek())
                .dayOfMonth(entity.getDayOfMonth())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .nextExecution(entity.getNextExecution())
                .active(entity.getIsActive())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}