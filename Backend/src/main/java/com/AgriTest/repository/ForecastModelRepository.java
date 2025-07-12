package com.AgriTest.repository;

import com.AgriTest.model.ForecastModel;
import com.AgriTest.model.ModelStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastModelRepository extends JpaRepository<ForecastModel, Long> {

    List<ForecastModel> findByDataSourceAndIsActiveTrue(String dataSource);
    
    List<ForecastModel> findByCreatedBy(Long userId);
    
    List<ForecastModel> findByStatus(ModelStatus status);
}