package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "two_factor_codes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false, length = 6)
    private String code;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    @Builder.Default
    private boolean used = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "temporary_code", length = 6)
    private String temporaryCode;
    
    // Constructor for SMS-based verification
    public TwoFactorCode(User user, String code, String phoneNumber, LocalDateTime expiresAt, String temporaryCode) {
        this.user = user;
        this.userId = user.getId();
        this.code = code;
        this.phoneNumber = phoneNumber;
        this.expiresAt = expiresAt;
        this.temporaryCode = temporaryCode;
        this.used = false;
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}