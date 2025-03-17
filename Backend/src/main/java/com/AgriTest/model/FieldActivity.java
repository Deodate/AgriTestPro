package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "field_activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_name", nullable = false)
    private String activityName;

    @Column(name = "location")
    private String location;

    @ManyToOne
    @JoinColumn(name = "responsible_person_id", nullable = false)
    private User responsiblePerson;

    @Column(name = "activity_datetime", nullable = false)
    private LocalDateTime activityDateTime;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    @OneToMany(mappedBy = "fieldActivity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FieldActivityAttachment> attachments;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FieldActivityStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = FieldActivityStatus.PLANNED;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum FieldActivityStatus {
        PLANNED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}