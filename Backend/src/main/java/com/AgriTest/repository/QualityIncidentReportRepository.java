package com.AgriTest.repository;

import com.AgriTest.model.QualityIncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualityIncidentReportRepository extends JpaRepository<QualityIncidentReport, Long> {
    // Find by incident ID
    Optional<QualityIncidentReport> findByIncidentId(String incidentId);

    // Find all reports by product ID
    List<QualityIncidentReport> findByProductId(Long productId);

    // Find reports by status
    List<QualityIncidentReport> findByStatus(QualityIncidentReport.IncidentStatus status);

    // Custom query to find recent incidents
    @Query("SELECT qir FROM QualityIncidentReport qir ORDER BY qir.incidentDate DESC")
    List<QualityIncidentReport> findRecentIncidents();
}