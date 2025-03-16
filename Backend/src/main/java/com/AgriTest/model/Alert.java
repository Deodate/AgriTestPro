package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "automated_alerts")
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "alert_recipients",
        joinColumns = @JoinColumn(name = "alert_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> recipients = new HashSet<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "alert_notification_methods", joinColumns = @JoinColumn(name = "alert_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_method")
    private Set<NotificationMethod> notificationMethods = new HashSet<>();
    
    @Column(columnDefinition = "TEXT")
    private String customMessage;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    public enum AlertType {
        STOCK_EXPIRY,
        TESTING_DEADLINE,
        COMPLIANCE_REVIEW
    }
    
    public enum NotificationMethod {
        SMS,
        EMAIL,
        DASHBOARD
    }
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}