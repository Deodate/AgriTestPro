package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_documentation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDocumentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String testName;

    @Column(nullable = false)
    private String testType;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String testProcedure;

    @Column(nullable = false)
    private String expectedResults;

    @Column
    private String actualResults;

    @Column
    private String testStatus;

    @Column
    private String attachments;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
} 