// File: src/main/java/com/AgriTest/service/WeatherDataService.java
package com.AgriTest.service;

import com.AgriTest.dto.WeatherDataRequest;
import com.AgriTest.dto.WeatherDataResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeatherDataService {
    List<WeatherDataResponse> getAllWeatherData();
    Optional<WeatherDataResponse> getWeatherDataById(Long id);
    WeatherDataResponse createWeatherData(WeatherDataRequest weatherDataRequest);
    WeatherDataResponse updateWeatherData(Long id, WeatherDataRequest weatherDataRequest);
    void deleteWeatherData(Long id);
    List<WeatherDataResponse> getWeatherDataByLocation(String location);
    List<WeatherDataResponse> getWeatherDataByDateRange(LocalDate startDate, LocalDate endDate);
    Optional<WeatherDataResponse> getWeatherDataByLocationAndDate(String location, LocalDate date);
}