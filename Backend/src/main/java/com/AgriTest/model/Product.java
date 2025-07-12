package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column
    private String manufacturer;
    
    @Column(name = "product_type")
    private String productType;
    
    @Column(name = "active_ingredients")
    private String activeIngredients;
    
    @Column(name = "date_of_registration")
    private LocalDate dateOfRegistration;
    
    @Column(name = "intended_use", columnDefinition = "TEXT")
    private String intendedUse;
    
    @Column(name = "crop_target")
    private String cropTarget;
    
    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "batch_number", unique = true)
    private String batchNumber;
    
    @Column
    private String status = "ACTIVE";
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "tested")
    private boolean tested = false;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "product")
    private List<TestCase> testCases = new ArrayList<>();
}