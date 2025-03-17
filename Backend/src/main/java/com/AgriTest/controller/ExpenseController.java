package com.AgriTest.controller;

import com.AgriTest.dto.ExpenseRequest;
import com.AgriTest.dto.ExpenseResponse;
import com.AgriTest.model.Expense;
import com.AgriTest.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // Create a new expense
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody(required = false) ExpenseRequest jsonRequest,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "expenseType", required = false) Expense.ExpenseType expenseType,
            @RequestParam(value = "amount", required = false) BigDecimal amount,
            @RequestParam(value = "paidById", required = false) Long paidById,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "receipt", required = false) MultipartFile receipt
    ) {
        // If JSON request is null, create a new DTO
        ExpenseRequest requestDto = jsonRequest != null ? jsonRequest : new ExpenseRequest();

        // Override with form data if provided
        if (date != null) requestDto.setDate(date);
        if (expenseType != null) requestDto.setExpenseType(expenseType);
        if (amount != null) requestDto.setAmount(amount);
        if (paidById != null) requestDto.setPaidById(paidById);
        if (description != null) requestDto.setDescription(description);
        if (receipt != null) requestDto.setReceipt(receipt);

        // Create expense
        ExpenseResponse createdExpense = expenseService.createExpense(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
    }

    // Update an existing expense
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody(required = false) ExpenseRequest jsonRequest,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "expenseType", required = false) Expense.ExpenseType expenseType,
            @RequestParam(value = "amount", required = false) BigDecimal amount,
            @RequestParam(value = "paidById", required = false) Long paidById,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "receipt", required = false) MultipartFile receipt
    ) {
        // If JSON request is null, create a new DTO
        ExpenseRequest requestDto = jsonRequest != null ? jsonRequest : new ExpenseRequest();

        // Override with form data if provided
        if (date != null) requestDto.setDate(date);
        if (expenseType != null) requestDto.setExpenseType(expenseType);
        if (amount != null) requestDto.setAmount(amount);
        if (paidById != null) requestDto.setPaidById(paidById);
        if (description != null) requestDto.setDescription(description);
        if (receipt != null) requestDto.setReceipt(receipt);

        // Update expense
        ExpenseResponse updatedExpense = expenseService.updateExpense(id, requestDto);
        return ResponseEntity.ok(updatedExpense);
    }

    // Get expense by ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        ExpenseResponse expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }

    // Get expense by Expense ID
    @GetMapping("/by-expense-id/{expenseId}")
    public ResponseEntity<ExpenseResponse> getExpenseByExpenseId(@PathVariable String expenseId) {
        ExpenseResponse expense = expenseService.getExpenseByExpenseId(expenseId);
        return ResponseEntity.ok(expense);
    }

    // List all expenses
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    // Delete an expense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    // Filtering endpoints

    // Get expenses by type
    @GetMapping("/type/{expenseType}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByType(@PathVariable Expense.ExpenseType expenseType) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByType(expenseType);
        return ResponseEntity.ok(expenses);
    }

    // Get expenses by paid user
    @GetMapping("/paid-by/{userId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByPaidBy(@PathVariable Long userId) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByPaidBy(userId);
        return ResponseEntity.ok(expenses);
    }

    // Get expenses by approval status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByApprovalStatus(@PathVariable Expense.ApprovalStatus status) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByApprovalStatus(status);
        return ResponseEntity.ok(expenses);
    }

    // Get expenses by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
        return ResponseEntity.ok(expenses);
    }

    // Advanced filtering
    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByFilters(
            @RequestParam(required = false) Expense.ExpenseType expenseType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long paidById,
            @RequestParam(required = false) Expense.ApprovalStatus approvalStatus,
            @RequestParam(required = false) Boolean isReimbursed
    ) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByFilters(
            expenseType, startDate, endDate, paidById, approvalStatus, isReimbursed
        );
        return ResponseEntity.ok(expenses);
    }

    // Expense Management Endpoints

    // Approve an expense
    @PutMapping("/{id}/approve")
    public ResponseEntity<ExpenseResponse> approveExpense(
            @PathVariable Long id,
            @RequestParam Long approverId
    ) {
        ExpenseResponse approvedExpense = expenseService.approveExpense(id, approverId);
        return ResponseEntity.ok(approvedExpense);
    }

    // Reject an expense
    @PutMapping("/{id}/reject")
    public ResponseEntity<ExpenseResponse> rejectExpense(
            @PathVariable Long id,
            @RequestParam Long approverId
    ) {
        ExpenseResponse rejectedExpense = expenseService.rejectExpense(id, approverId);
        return ResponseEntity.ok(rejectedExpense);
    }

    // Mark expense as reimbursed
    @PutMapping("/{id}/reimburse")
    public ResponseEntity<ExpenseResponse> markAsReimbursed(@PathVariable Long id) {
        ExpenseResponse reimbursedExpense = expenseService.markAsReimbursed(id);
        return ResponseEntity.ok(reimbursedExpense);
    }

    // Upload receipt for an expense
    @PostMapping(value = "/{id}/receipt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadReceipt(
            @PathVariable Long id,
            @RequestParam("receipt") MultipartFile receipt
    ) {
        String receiptUrl = expenseService.uploadReceipt(id, receipt);
        return ResponseEntity.ok(receiptUrl);
    }

    // Aggregation and Summary Endpoints

    // Get total expenses by type
    @GetMapping("/total-by-type/{expenseType}")
    public ResponseEntity<BigDecimal> getTotalExpensesByType(@PathVariable Expense.ExpenseType expenseType) {
        BigDecimal total = expenseService.getTotalExpensesByType(expenseType);
        return ResponseEntity.ok(total);
    }

    // Get total expenses by date range
    @GetMapping("/total-by-date-range")
    public ResponseEntity<BigDecimal> getTotalExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        BigDecimal total = expenseService.getTotalExpensesByDateRange(startDate, endDate);
        return ResponseEntity.ok(total);
    }

    // Get expense summary by type
    @GetMapping("/summary-by-type")
    public ResponseEntity<Map<Expense.ExpenseType, BigDecimal>> getExpenseSummaryByType() {
        Map<Expense.ExpenseType, BigDecimal> summary = expenseService.getExpenseSummaryByType();
        return ResponseEntity.ok(summary);
    }
}