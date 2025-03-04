package com.AgriTest.repository;

import com.AgriTest.model.AnalyticsData;
import com.AgriTest.model.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnalyticsDataRepository extends JpaRepository<AnalyticsData, Long> {

    List<AnalyticsData> findByDataSourceAndEntityTypeAndEntityIdOrderByDataDate(
            String dataSource, EntityType entityType, Long entityId);

    // Modified method name to match exactly what is being called in the service
    List<AnalyticsData> findByDataSourceAndDataDateBetween(
            String dataSource, LocalDate startDate, LocalDate endDate);
            
    @Query("SELECT a FROM AnalyticsData a WHERE a.dataSource = :dataSource " +
           "AND a.entityType = :entityType " +
           "AND a.entityId = :entityId " +
           "AND a.dataDate BETWEEN :startDate AND :endDate " +
           "ORDER BY a.dataDate")
    List<AnalyticsData> findDataForTrendAnalysis(
            @Param("dataSource") String dataSource,
            @Param("entityType") EntityType entityType,
            @Param("entityId") Long entityId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}