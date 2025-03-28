package com.AgriTest.repository;

import com.AgriTest.model.TwoFactorCode;
import com.AgriTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Long> {

    /**
     * Find the most recent unused and non-expired code for a user
     */
    @Query("SELECT t FROM TwoFactorCode t WHERE t.userId = ?1 AND t.used = false AND t.expiresAt > ?2 ORDER BY t.createdAt DESC")
    Optional<TwoFactorCode> findValidCodeForUser(Long userId, LocalDateTime now);
    
    /**
     * Find all codes for a user that are not used and not expired
     */
    List<TwoFactorCode> findByUserIdAndUsedFalseAndExpiresAtGreaterThan(Long userId, LocalDateTime now);
    
    /**
     * Find all active codes for a user
     */
    List<TwoFactorCode> findByUserAndUsedFalseAndExpiresAtAfter(User user, LocalDateTime now);
    
    /**
     * Delete all codes that have expired
     */
    void deleteByExpiresAtBefore(LocalDateTime time);
}