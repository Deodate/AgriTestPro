package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class RealTimeStockTrackingRequest {
    @NotBlank(message = "Product name cannot be blank")
    private String productName;

    @NotBlank(message = "Batch number cannot be blank")
    private String batchNumber;

    @NotBlank(message = "Transaction type cannot be blank")
    private String transactionType; // e.g., Add, Remove

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotBlank(message = "Unit of measure cannot be blank")
    private String unitOfMeasure;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotBlank(message = "Source or destination cannot be blank")
    private String sourceOrDestination;

    @NotBlank(message = "Filled by cannot be blank")
    private String filledBy;

    private String comment;

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSourceOrDestination() {
        return sourceOrDestination;
    }

    public void setSourceOrDestination(String sourceOrDestination) {
        this.sourceOrDestination = sourceOrDestination;
    }

    public String getFilledBy() {
        return filledBy;
    }

    public void setFilledBy(String filledBy) {
        this.filledBy = filledBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
} 