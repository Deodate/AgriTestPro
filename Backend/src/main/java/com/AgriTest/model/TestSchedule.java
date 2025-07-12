// File: src/main/java/com/AgriTest/model/TestSchedule.java
package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_schedules")
@Data
public class TestSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;
    
    @Column(name = "test_name", nullable = false)
    private String testName;
    
    @Column(name = "schedule_name", nullable = false)
    private String scheduleName;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "frequency", nullable = false)
    private String frequency; // DAILY, WEEKLY, BIWEEKLY, MONTHLY
    
    @Column(name = "day_of_week")
    private Integer dayOfWeek; // 1-7 (Monday to Sunday)
    
    @Column(name = "day_of_month")
    private Integer dayOfMonth;
    
    @Column(name = "next_execution")
    private LocalDate nextExecution;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "trial_phase")
    private String trialPhase;
    
    @Column(name = "assigned_personnel")
    private String assignedPersonnel;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "test_objective", columnDefinition = "TEXT")
    private String testObjective;
    
    @Column(name = "equipment_required", columnDefinition = "TEXT")
    private String equipmentRequired;
    
    @Column(name = "notification_preference")
    private String notificationPreference;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}