package com.AgriTest.dto;

import com.AgriTest.model.Expense;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseRequest {
    private String expenseId; // Optional, generated if not provided
    
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    @NotNull(message = "Expense type is required")
    private Expense.ExpenseType expenseType;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Amount cannot exceed 8 digits in integer part and 2 digits in fraction part")
    private BigDecimal amount;
    
    @NotNull(message = "Paid by user ID is required")
    private Long paidById;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    private MultipartFile receipt;
    
    private Expense.ApprovalStatus approvalStatus;
    
    private Long approvedById;
    
    private Boolean isReimbursed;
}