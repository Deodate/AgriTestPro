package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "two_factor_codes")
@Data
@NoArgsConstructor
public class TwoFactorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 10)
    private String code;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;
    
    @Column(nullable = false)
    private Boolean used = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    // New field to store the temporary generated code
    @Column(name = "temporary_code", length = 10)
    private String temporaryCode;
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public TwoFactorCode(User user, String code, String phoneNumber, LocalDateTime expiresAt, String temporaryCode) {
        this.user = user;
        this.code = code;
        this.phoneNumber = phoneNumber;
        this.expiresAt = expiresAt;
        this.temporaryCode = temporaryCode;
    }
}