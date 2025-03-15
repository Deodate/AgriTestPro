package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "stock_monitoring")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMonitoring {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "current_stock_level", nullable = false)
    private Integer currentStockLevel;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "stock_alerts", nullable = false)
    private Boolean stockAlerts;
    
    @Column(name = "last_updated_date", nullable = false)
    private LocalDate lastUpdatedDate;
    
    @Column(name = "responsible_officer", nullable = false)
    private String responsibleOfficer;
    
    @PrePersist
    protected void onCreate() {
        this.lastUpdatedDate = LocalDate.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdatedDate = LocalDate.now();
    }
}