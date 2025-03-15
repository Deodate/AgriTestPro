package com.AgriTest.repository;

import com.AgriTest.model.QualityIncidentReport;
import com.AgriTest.model.QualityIncidentReport.IncidentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QualityIncidentReportRepository extends JpaRepository<QualityIncidentReport, Long> {
    
    Optional<QualityIncidentReport> findByIncidentId(String incidentId);
    
    List<QualityIncidentReport> findByProductId(Long productId);
    
    List<QualityIncidentReport> findByStatus(IncidentStatus status);
    
    List<QualityIncidentReport> findByIncidentDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT qir FROM QualityIncidentReport qir WHERE " +
           "LOWER(qir.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(qir.correctiveActions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<QualityIncidentReport> searchByKeyword(@Param("keyword") String keyword);
    
    Page<QualityIncidentReport> findAll(Pageable pageable);
    
    boolean existsByIncidentId(String incidentId);
}