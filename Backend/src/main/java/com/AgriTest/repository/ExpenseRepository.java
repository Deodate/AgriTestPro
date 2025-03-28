package com.AgriTest.repository;

import com.AgriTest.model.Expense;
import com.AgriTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByExpenseId(String expenseId);
    
    List<Expense> findByExpenseType(Expense.ExpenseType expenseType);
    
    List<Expense> findByPaidBy(User paidBy);
    
    List<Expense> findByApprovalStatus(Expense.ApprovalStatus approvalStatus);
    
    List<Expense> findByIsReimbursed(Boolean isReimbursed);
    
    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Expense> findByApprovedBy(User approvedBy);
    
    List<Expense> findByCreatedBy(User createdBy);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.expenseType = ?1")
    BigDecimal sumAmountByExpenseType(Expense.ExpenseType expenseType);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.date BETWEEN ?1 AND ?2")
    BigDecimal sumAmountByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Native SQL query to avoid parameter type determination issues
    @Query(value = "SELECT * FROM expenses e WHERE " +
           "(:expenseType IS NULL OR e.expense_type = CAST(:expenseType AS VARCHAR)) AND " +
           "(:startDate IS NULL OR e.date >= CAST(:startDate AS DATE)) AND " +
           "(:endDate IS NULL OR e.date <= CAST(:endDate AS DATE)) AND " +
           "(:paidById IS NULL OR e.paid_by = CAST(:paidById AS BIGINT)) AND " +
           "(:approvalStatus IS NULL OR e.approval_status = CAST(:approvalStatus AS VARCHAR)) AND " +
           "(:isReimbursed IS NULL OR e.is_reimbursed = CAST(:isReimbursed AS BOOLEAN))", 
           nativeQuery = true)
    List<Expense> findByFilters(
            @Param("expenseType") String expenseType,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("paidById") Long paidById,
            @Param("approvalStatus") String approvalStatus,
            @Param("isReimbursed") Boolean isReimbursed);
}