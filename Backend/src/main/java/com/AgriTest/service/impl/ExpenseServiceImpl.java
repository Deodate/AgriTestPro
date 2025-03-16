// package com.AgriTest.service.impl;

// import com.AgriTest.dto.ExpenseRequest;
// import com.AgriTest.dto.ExpenseResponse;
// import com.AgriTest.exception.ResourceNotFoundException;
// import com.AgriTest.model.Expense;
// import com.AgriTest.model.User;
// import com.AgriTest.repository.ExpenseRepository;
// import com.AgriTest.repository.UserRepository;
// import com.AgriTest.service.ExpenseService;
// import com.AgriTest.service.FileStorageService;
// import com.AgriTest.util.SecurityUtils;
// import jakarta.transaction.Transactional;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

// @Service
// public class ExpenseServiceImpl implements ExpenseService {
//     private static final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);

//     @Autowired
//     private ExpenseRepository expenseRepository;

//     @Autowired
//     private UserRepository userRepository;
    
//     @Autowired
//     private FileStorageService fileStorageService;

//     @Override
//     @Transactional
//     public ExpenseResponse createExpense(ExpenseRequest request) {
//         log.info("Creating new expense for amount: {}", request.getAmount());
        
//         // Get current user
//         User currentUser = userRepository.findById(SecurityUtils.getCurrentUserId())
//                 .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        
//         // Get paid by user
//         User paidByUser = userRepository.findById(request.getPaidById())
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getPaidById()));
        
//         // Create expense entity
//         Expense expense = new Expense();
//         expense.setExpenseId(request.getExpenseId()); // Will be generated if null or empty
//         expense.setDate(request.getDate());
//         expense.setExpenseType(request.getExpenseType());
//         expense.setAmount(request.getAmount());
//         expense.setPaidBy(paidByUser);
//         expense.setDescription(request.getDescription());
//         expense.setApprovalStatus(Expense.ApprovalStatus.PENDING); // Always start as pending
//         expense.setIsReimbursed(false); // Always start as not reimbursed
//         expense.setCreatedBy(currentUser);
        
//         // Handle receipt upload if provided
//         if (request.getReceipt() != null && !request.getReceipt().isEmpty()) {
//             try {
//                 String receiptUrl = uploadReceipt(null, request.getReceipt());
//                 expense.setReceiptUrl(receiptUrl);
//             } catch (Exception e) {
//                 log.error("Failed to upload receipt", e);
//                 // Continue without receipt if upload fails
//             }
//         }
        
//         // Save expense
//         Expense savedExpense = expenseRepository.save(expense);
//         log.info("Expense created with ID: {} and expenseId: {}", savedExpense.getId(), savedExpense.getExpenseId());
        
//         // Return response
//         return mapToExpenseResponse(savedExpense);
//     }

//     @Override
//     @Transactional
//     public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
//         log.info("Updating expense with ID: {}", id);
        
//         // Find existing expense
//         Expense existingExpense = expenseRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        
//         // Check if expense is already approved - cannot modify approved expenses
//         if (Expense.ApprovalStatus.APPROVED.equals(existingExpense.getApprovalStatus())) {
//             throw new IllegalStateException("Cannot modify an approved expense");
//         }
        
//         // Get paid by user if changing
//         User paidByUser = null;
//         if (request.getPaidById() != null && !request.getPaidById().equals(existingExpense.getPaidBy().getId())) {
//             paidByUser = userRepository.findById(request.getPaidById())
//                     .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getPaidById()));
//         }
        
//         // Update expense fields
//         if (request.getDate() != null) {
//             existingExpense.setDate(request.getDate());
//         }
//         if (request.getExpenseType() != null) {
//             existingExpense.setExpenseType(request.getExpenseType());
//         }
//         if (request.getAmount() != null) {
//             existingExpense.setAmount(request.getAmount());
//         }
//         if (paidByUser != null) {
//             existingExpense.setPaidBy(paidByUser);
//         }
//         if (request.getDescription() != null) {
//             existingExpense.setDescription(request.getDescription());
//         }
        
//         // Handle receipt upload if provided
//         if (request.getReceipt() != null && !request.getReceipt().isEmpty()) {
//             try {
//                 String receiptUrl = uploadReceipt(id, request.getReceipt());
//                 existingExpense.setReceiptUrl(receiptUrl);
//             } catch (Exception e) {
//                 log.error("Failed to upload receipt", e);
//                 // Continue without updating receipt if upload fails
//             }
//         }
        
//         // Save updated expense
//         Expense updatedExpense = expenseRepository.save(existingExpense);
//         log.info("Expense updated with ID: {}", updatedExpense.getId());
        
//         // Return response
//         return mapToExpenseResponse(updatedExpense);
//     }

//     @Override
//     public ExpenseResponse getExpenseById(Long id) {
//         log.info("Fetching expense with ID: {}", id);
        
//         Expense expense = expenseRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
                
//         return mapToExpenseResponse(expense);
//     }
    
//     @Override
//     public ExpenseResponse getExpenseByExpenseId(String expenseId) {
//         log.info("Fetching expense with expense ID: {}", expenseId);
        
//         Expense expense = expenseRepository.findByExpenseId(expenseId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Expense not found with expense ID: " + expenseId));
                
//         return mapToExpenseResponse(expense);
//     }

//     @Override
//     public List<ExpenseResponse> getAllExpenses() {
//         log.info("Fetching all expenses");
        
//         return expenseRepository.findAll().stream()
//                 .map(this::mapToExpenseResponse)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public List<ExpenseResponse> getExpensesByType(Expense.ExpenseType expenseType) {
//         log.info("Fetching expenses with type: {}", expenseType);
        
//         return expenseRepository.findByExpenseType(expenseType).stream()
//                 .map(this::mapToExpenseResponse)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public List<ExpenseResponse> getExpensesByPaidBy(Long userId) {
//         log.info("Fetching expenses paid by user with ID: {}", userId);
        
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                
//         return expenseRepository.findByPaidBy(user).stream()
//                 .map(this::mapToExpenseResponse)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public List<ExpenseResponse> getExpensesByApprovalStatus(Expense.ApprovalStatus approvalStatus) {
//         log.info("Fetching expenses with approval status: {}", approvalStatus);
        
//         return expenseRepository.findByApprovalStatus(approvalStatus).stream()
//                 .map(this::mapToExpenseResponse)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public List<ExpenseResponse> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
//         log.info("Fetching expenses between dates: {} and {}", startDate, endDate);
        
//         return expenseRepository.findByDateBetween(startDate, endDate).stream()
//                 .map(this::mapToExpenseResponse)
//                 .collect(Collectors.toList());
//     }
    
//     @Override
//     public List<ExpenseResponse> getExpensesByFilters(
//             Expense.ExpenseType expenseType,
//             LocalDate startDate,
//             LocalDate endDate,
//             Long paidById,
//             Expense.ApprovalStatus approvalStatus,
//             Boolean isReimbursed) {
        
//         log.info("Fetching expenses with filters - expenseType: {}, dateRange: {} to {}, paidById: {}, status: {}, reimbursed: {}", 
//                 expenseType, startDate, endDate, paidById, approvalStatus, isReimbursed);
        
//         return expenseRepository.findByFilters(expenseType, startDate, endDate, paidById, approvalStatus, isReimbursed)
//                 .stream()
//                 .map(this::mapToExpenseResponse)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     @Transactional
//     public ExpenseResponse approveExpense(Long id, Long approverId) {
//         log.info("Approving expense with ID: {} by user ID: {}", id, approverId);
        
//         Expense expense = expenseRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
                
//         User approver = userRepository.findById(approverId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Approver not found with id: " + approverId));
                
//         // Cannot approve already approved or rejected expense
//         if (Expense.ApprovalStatus.APPROVED.equals(expense.getApprovalStatus())) {
//             throw new IllegalStateException("Expense is already approved");
//         }
//         if (Expense.ApprovalStatus.REJECTED.equals(expense.getApprovalStatus())) {
//             throw new IllegalStateException("Cannot approve a rejected expense");
//         }
        
//         // Update approval information
//         expense.setApprovalStatus(Expense.ApprovalStatus.APPROVED);
//         expense.setApprovedBy(approver);
//         expense.setApprovedAt(LocalDateTime.now());
        
//         Expense updatedExpense = expenseRepository.save(expense);
//         log.info("Expense with ID: {} approved successfully", id);
        
//         return mapToExpenseResponse(updatedExpense);
//     }

//     @Override
//     @Transactional
//     public ExpenseResponse rejectExpense(Long id, Long approverId) {
//         log.info("Rejecting expense with ID: {} by user ID: {}", id, approverId);
        
//         Expense expense = expenseRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
                
//         User approver = userRepository.findById(approverId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Approver not found with id: " + approverId));
                
//         // Cannot reject already approved or rejected expense
//         if (Expense.ApprovalStatus.APPROVED.equals(expense.getApprovalStatus())) {
//             throw new IllegalStateException("Cannot reject an already approved expense");
//         }
//         if (Expense.ApprovalStatus.REJECTED.equals(expense.getApprovalStatus())) {
//             throw new IllegalStateException("Expense is already rejected");
//         }
        
//         // Update approval information
//         expense.setApprovalStatus(Expense.ApprovalStatus.REJECTED);
//         expense.setApprovedBy(approver);
//         expense.setApprovedAt(LocalDateTime.now());
        
//         Expense updatedExpense = expenseRepository.save(expense);
//         log.info("Expense with ID: {} rejected successfully", id);
        
//         return mapToExpenseResponse(updatedExpense);
//     }

//     @Override
//     @Transactional
//     public ExpenseResponse markAsReimbursed(Long id) {
//         log.info("Marking expense with ID: {} as reimbursed", id);
        
//         Expense expense = expenseRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
                
//         // Cannot reimburse if not approved
//         if (!Expense.ApprovalStatus.APPROVED.equals(expense.getApprovalStatus())) {
//             throw new IllegalStateException("Cannot reimburse an expense that is not approved");
//         }
        
//         // Already reimbursed
//         if (Boolean.TRUE.equals(expense.getIsReimbursed())) {
//             throw new IllegalStateException("Expense is already reimbursed");
//         }
        
//         expense.setIsReimbursed(true);
        
//         Expense updatedExpense = expenseRepository.save(expense);
//         log.info("Expense with ID: {} marked as reimbursed", id);
        
//         return mapToExpenseResponse(updatedExpense);
//     }

//     @Override
//     @Transactional
//     public void deleteExpense(Long id) {
//         log.info("Deleting expense with ID: {}", id);
        
//         Expense expense = expenseRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        
//         // Cannot delete approved and reimbursed expenses
//         if (Expense.ApprovalStatus.APPROVED.equals(expense.getApprovalStatus()) && 
//             Boolean.TRUE.equals(expense.getIsReimbursed())) {
//             throw new IllegalStateException("Cannot delete an approved and reimbursed expense");
//         }
        
//         expenseRepository.delete(expense);
//         log.info("Expense with ID: {} deleted successfully", id);
//     }

//     @Override
//     @Transactional
//     public String uploadReceipt(Long id, MultipartFile receipt) {
//         if (receipt == null || receipt.isEmpty()) {
//             throw new IllegalArgumentException("Receipt file cannot be empty");
//         }
        
//         try {
//             // If ID is provided, associate with existing expense
//             if (id != null) {
//                 Expense expense = expenseRepository.findById(id)
//                         .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
                
//                 // Upload receipt and get URL
//                 String receiptUrl = fileStorageService.storeFile(
//                         receipt, 
//                         id, 
//                         SecurityUtils.getCurrentUserId(), 
//                         "EXPENSE_RECEIPT").getFileDownloadUri();
                
//                 // Update expense with receipt URL
//                 expense.setReceiptUrl(receiptUrl);
//                 expenseRepository.save(expense);
                
//                 return receiptUrl;
//             } else {
//                 // Just upload and return URL for new expense creation
//                 return fileStorageService.storeFile(
//                         receipt, 
//                         0L, // Temporary ID for new expenses
//                         SecurityUtils.getCurrentUserId(), 
//                         "EXPENSE_RECEIPT").getFileDownloadUri();
//             }
//         } catch (Exception e) {
//             log.error("Failed to upload receipt", e);
//             throw new RuntimeException("Failed to upload receipt", e);
//         }
//     }

//     @Override
//     public BigDecimal getTotalExpensesByType(Expense.ExpenseType expenseType) {
//         log.info("Calculating total expenses for type: {}", expenseType);
        
//         BigDecimal total = expenseRepository.sumAmountByExpenseType(expenseType);
//         return total != null ? total : BigDecimal.ZERO;
//     }

//     @Override
//     public BigDecimal getTotalExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
//         log.info("Calculating total expenses between dates: {} and {}", startDate, endDate);
        
//         BigDecimal total = expenseRepository.sumAmountByDateRange(startDate, endDate);
//         return total != null ? total : BigDecimal.ZERO;
//     }

//     @Override
//     public Map<Expense.ExpenseType, BigDecimal> getExpenseSummaryByType() {
//         log.info("Generating expense summary by type");
        
//         Map<Expense.ExpenseType, BigDecimal> summary = new HashMap<>();
        
//         // Initialize all expense types with zero
//         Arrays.stream(Expense.ExpenseType.values())
//                 .forEach(type -> summary.put(type, BigDecimal.ZERO));
        
//         // Get actual totals
//         List<Expense> expenses = expenseRepository.findAll();
//         for (Expense expense : expenses) {
//             Expense.ExpenseType type = expense.getExpenseType();
//             BigDecimal currentTotal = summary.get(type);
//             summary.put(type, currentTotal.add(expense.getAmount()));
//         }
        
//         return summary;
//     }
    
//     // Helper method to map Expense to ExpenseResponse
//     private ExpenseResponse mapToExpenseResponse(Expense expense) {
//         ExpenseResponse response = new ExpenseResponse();
//         response.setId(expense.getId());
//         response.setExpenseId(expense.getExpenseId());
//         response.setDate(expense.getDate());
//         response.setExpenseType(expense.getExpenseType());
//         response.setAmount(expense.getAmount());
//         response.setDescription(expense.getDescription());
//         response.setReceiptUrl(expense.getReceiptUrl());
//         response.setApprovalStatus(expense.getApprovalStatus());
//         response.setApprovedAt(expense.getApprovedAt());
//         response.setCreatedAt(expense.getCreatedAt());
//         response.setUpdatedAt(expense.getUpdatedAt());
//         response.setIsReimbursed(expense.getIsReimbursed());
        
//         // Map paid by user
//         if (expense.getPaidBy() != null) {
//             ExpenseResponse.UserDto paidBy = new ExpenseResponse.UserDto();
//             paidBy.setId(expense.getPaidBy().getId());
//             paidBy.setUsername(expense.getPaidBy().getUsername());
//             paidBy.setFullName(expense.getPaidBy().getFullName());
//             response.setPaidBy(paidBy);
//         }
        
//         // Map approved by user if available
//         if (expense.getApprovedBy() != null) {