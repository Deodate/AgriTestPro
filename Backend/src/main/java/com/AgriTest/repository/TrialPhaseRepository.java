package com.AgriTest.repository;

import com.AgriTest.model.TrialPhase;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
=======
import org.springframework.stereotype.Repository;
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
import java.util.List;

@Repository
public interface TrialPhaseRepository extends JpaRepository<TrialPhase, Long> {
    List<TrialPhase> findByTestCaseId(Long testCaseId);
<<<<<<< HEAD
    List<TrialPhase> findByStatus(String status);
    
    @Query("SELECT tp FROM TrialPhase tp WHERE tp.dateOfPhase BETWEEN :startDate AND :endDate")
    List<TrialPhase> findByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT tp FROM TrialPhase tp WHERE tp.testCase.id = :testCaseId AND tp.status = :status")
    List<TrialPhase> findByTestCaseIdAndStatus(
        @Param("testCaseId") Long testCaseId,
        @Param("status") String status
    );
    
    List<TrialPhase> findByCreatedBy(String username);
    
    @Query("SELECT COUNT(tp) FROM TrialPhase tp WHERE tp.status = :status")
    long countByStatus(@Param("status") String status);
=======
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
} 