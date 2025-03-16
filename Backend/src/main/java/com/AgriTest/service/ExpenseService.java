package com.AgriTest.service;

import com.AgriTest.dto.ExpenseRequest;
import com.AgriTest.dto.ExpenseResponse;
import com.AgriTest.model.Expense;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExpenseService {
    ExpenseResponse createExpense(ExpenseRequest request);
    
    ExpenseResponse updateExpense(Long id, ExpenseRequest request);
    
    ExpenseResponse getExpenseById(Long id);
    
    ExpenseResponse getExpenseByExpenseId(String expenseId);
    
    List<ExpenseResponse> getAllExpenses();
    
    List<ExpenseResponse> getExpensesByType(Expense.ExpenseType expenseType);
    
    List<ExpenseResponse> getExpensesByPaidBy(Long userId);
    
    List<ExpenseResponse> getExpensesByApprovalStatus(Expense.ApprovalStatus approvalStatus);
    
    List<ExpenseResponse> getExpensesByDateRange(LocalDate startDate, LocalDate endDate);
    
    List<ExpenseResponse> getExpensesByFilters(
            Expense.ExpenseType expenseType,
            LocalDate startDate,
            LocalDate endDate,
            Long paidById,
            Expense.ApprovalStatus approvalStatus,
            Boolean isReimbursed);
    
    ExpenseResponse approveExpense(Long id, Long approverId);
    
    ExpenseResponse rejectExpense(Long id, Long approverId);
    
    ExpenseResponse markAsReimbursed(Long id);
    
    void deleteExpense(Long id);
    
    String uploadReceipt(Long id, MultipartFile receipt);
    
    BigDecimal getTotalExpensesByType(Expense.ExpenseType expenseType);
    
    BigDecimal getTotalExpensesByDateRange(LocalDate startDate, LocalDate endDate);
    
    Map<Expense.ExpenseType, BigDecimal> getExpenseSummaryByType();
}