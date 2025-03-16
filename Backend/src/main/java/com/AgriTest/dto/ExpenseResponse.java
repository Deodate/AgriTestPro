package com.AgriTest.dto;

import com.AgriTest.model.Expense;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ExpenseResponse {
    private Long id;
    private String expenseId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    private Expense.ExpenseType expenseType;
    private BigDecimal amount;
    private UserDto paidBy;
    private String description;
    private String receiptUrl;
    private Expense.ApprovalStatus approvalStatus;
    private UserDto approvedBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime approvedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private UserDto createdBy;
    private Boolean isReimbursed;

    @Data
    public static class UserDto {
        private Long id;
        private String username;
        private String fullName;
    }
}