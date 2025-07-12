// File: src/main/java/com/AgriTest/repository/WeatherDataRepository.java
package com.AgriTest.repository;

import com.AgriTest.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    List<WeatherData> findByLocation(String location);
    
    List<WeatherData> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    Optional<WeatherData> findByLocationAndDate(String location, LocalDate date);
}