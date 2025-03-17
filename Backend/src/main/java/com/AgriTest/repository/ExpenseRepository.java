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
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findByExpenseId(String expenseId);
    
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
    
    // Alternative approach using explicit type checks to avoid PostgreSQL null parameter type issues
    @Query(value = "SELECT e FROM Expense e WHERE " +
           "(:expenseType IS NULL OR e.expenseType = cast(:expenseType as com.AgriTest.model.Expense$ExpenseType)) AND " +
           "(:startDate IS NULL OR e.date >= cast(:startDate as java.time.LocalDate)) AND " +
           "(:endDate IS NULL OR e.date <= cast(:endDate as java.time.LocalDate)) AND " +
           "(:paidById IS NULL OR e.paidBy.id = cast(:paidById as long)) AND " +
           "(:approvalStatus IS NULL OR e.approvalStatus = cast(:approvalStatus as com.AgriTest.model.Expense$ApprovalStatus)) AND " +
           "(:isReimbursed IS NULL OR e.isReimbursed = cast(:isReimbursed as boolean))")
    List<Expense> findByFilters(
            @Param("expenseType") Expense.ExpenseType expenseType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("paidById") Long paidById,
            @Param("approvalStatus") Expense.ApprovalStatus approvalStatus,
            @Param("isReimbursed") Boolean isReimbursed);
}