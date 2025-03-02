// File: src/main/java/com/AgriTest/model/ReportSchedule.java
package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "report_schedules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExportFormat exportFormat;

    @ElementCollection
    @CollectionTable(name = "report_schedule_entities", joinColumns = @JoinColumn(name = "report_schedule_id"))
    @Column(name = "entity_id")
    private Set<Long> entityIds = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "report_schedule_recipients", joinColumns = @JoinColumn(name = "report_schedule_id"))
    @Column(name = "email")
    private Set<String> recipients = new HashSet<>();

    @Column(nullable = false)
    private LocalTime scheduleTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleFrequency frequency;

    // For weekly schedules
    @Column
    private Integer dayOfWeek;

    // For monthly schedules
    @Column
    private Integer dayOfMonth;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalDate nextExecution;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Long createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Calculate the next execution date based on frequency
    public void calculateNextExecution() {
        if (!isActive || nextExecution == null) {
            return;
        }

        LocalDate currentDate = LocalDate.now();
        if (nextExecution.isBefore(currentDate) || nextExecution.isEqual(currentDate)) {
            switch (frequency) {
                case DAILY:
                    nextExecution = currentDate.plusDays(1);
                    break;
                case WEEKLY:
                    // If dayOfWeek is specified, find next occurrence of that day
                    if (dayOfWeek != null) {
                        nextExecution = currentDate.plusDays(1);
                        while (nextExecution.getDayOfWeek().getValue() != dayOfWeek) {
                            nextExecution = nextExecution.plusDays(1);
                        }
                    } else {
                        nextExecution = currentDate.plusWeeks(1);
                    }
                    break;
                case MONTHLY:
                    // If dayOfMonth is specified, find next occurrence of that day
                    if (dayOfMonth != null) {
                        nextExecution = currentDate.plusMonths(1).withDayOfMonth(1);
                        int maxDay = nextExecution.lengthOfMonth();
                        nextExecution = nextExecution.withDayOfMonth(Math.min(dayOfMonth, maxDay));
                    } else {
                        nextExecution = currentDate.plusMonths(1);
                    }
                    break;
                default:
                    // Default to daily if frequency is unknown
                    nextExecution = currentDate.plusDays(1);
            }
        }

        // Check if next execution exceeds end date
        if (endDate != null && nextExecution.isAfter(endDate)) {
            isActive = false;
        }
    }
}