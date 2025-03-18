package com.AgriTest.repository;

import com.AgriTest.model.TwoFactorCode;
import com.AgriTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Long> {
    
    Optional<TwoFactorCode> findByCodeAndUserAndUsedFalseAndExpiresAtAfter(
            String code, User user, LocalDateTime now);
    
    Optional<TwoFactorCode> findByCodeAndPhoneNumberAndUsedFalseAndExpiresAtAfter(
            String code, String phoneNumber, LocalDateTime now);
    
    List<TwoFactorCode> findByUserAndUsedFalseAndExpiresAtAfter(
            User user, LocalDateTime now);
    
    @Query("SELECT t FROM TwoFactorCode t WHERE t.phoneNumber = :phoneNumber AND t.expiresAt > :now AND t.used = false ORDER BY t.createdAt DESC")
    List<TwoFactorCode> findLatestActiveCodesByPhoneNumber(
            @Param("phoneNumber") String phoneNumber, 
            @Param("now") LocalDateTime now);
    
    Optional<TwoFactorCode> findByTemporaryCodeAndUsedFalseAndExpiresAtAfter(
            String temporaryCode, LocalDateTime now);
    
    void deleteByExpiresAtBefore(LocalDateTime time);
}