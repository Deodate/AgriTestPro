package com.AgriTest.repository;

import com.AgriTest.model.Expense;
import com.AgriTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    
    @Query("SELECT e FROM Expense e WHERE " +
           "(:expenseType IS NULL OR e.expenseType = :expenseType) AND " +
           "(:startDate IS NULL OR e.date >= :startDate) AND " +
           "(:endDate IS NULL OR e.date <= :endDate) AND " +
           "(:paidById IS NULL OR e.paidBy.id = :paidById) AND " +
           "(:approvalStatus IS NULL OR e.approvalStatus = :approvalStatus) AND " +
           "(:isReimbursed IS NULL OR e.isReimbursed = :isReimbursed)")
    List<Expense> findByFilters(
            Expense.ExpenseType expenseType,
            LocalDate startDate,
            LocalDate endDate,
            Long paidById,
            Expense.ApprovalStatus approvalStatus,
            Boolean isReimbursed);
}