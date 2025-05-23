package com.AgriTest.repository;

import com.AgriTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
    
    Boolean existsByPhoneNumber(String phoneNumber);
    
    List<User> findByRole(String role);
    
    Optional<User> findByResetToken(String resetToken);
    
    Optional<User> findByEmail(String email);
}