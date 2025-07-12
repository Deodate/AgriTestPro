package com.AgriTest.repository;

import com.AgriTest.model.ForecastResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ForecastResultRepository extends JpaRepository<ForecastResult, Long> {

    List<ForecastResult> findByForecastModelIdOrderByForecastDate(Long forecastModelId);
    
    List<ForecastResult> findByForecastModelIdAndForecastDateBetweenOrderByForecastDate(
            Long forecastModelId, LocalDate startDate, LocalDate endDate);
}