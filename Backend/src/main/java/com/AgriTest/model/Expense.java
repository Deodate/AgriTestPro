package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "expenses")
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "expense_id", nullable = false, unique = true)
    private String expenseId;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseType expenseType;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paid_by", nullable = false)
    private User paidBy;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column
    private String receiptUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approved_by")
    private User approvedBy;
    
    @Column
    private LocalDateTime approvedAt;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @Column(nullable = false)
    private Boolean isReimbursed = false;
    
    public enum ExpenseType {
        LABOR,
        MATERIALS,
        TESTING_FEES,
        EQUIPMENT,
        TRAVEL,
        UTILITIES,
        CONSULTING,
        OTHER
    }
    
    public enum ApprovalStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.approvalStatus == null) {
            this.approvalStatus = ApprovalStatus.PENDING;
        }
        if (this.expenseId == null || this.expenseId.isEmpty()) {
            // Generate expense ID with format: EXP-YYYYMMDD-XXXX (X = random alphanumeric)
            this.expenseId = "EXP-" + LocalDate.now().toString().replace("-", "") + "-" 
                + String.format("%04d", (int)(Math.random() * 10000));
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}