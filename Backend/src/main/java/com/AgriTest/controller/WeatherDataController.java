// File: src/main/java/com/AgriTest/controller/WeatherDataController.java
package com.AgriTest.controller;

import com.AgriTest.dto.WeatherDataRequest;
import com.AgriTest.dto.WeatherDataResponse;
import com.AgriTest.service.WeatherDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherDataController {

    @Autowired
    private WeatherDataService weatherDataService;

    @GetMapping
    public List<WeatherDataResponse> getAllWeatherData() {
        return weatherDataService.getAllWeatherData();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherDataResponse> getWeatherDataById(@PathVariable Long id) {
        return weatherDataService.getWeatherDataById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public WeatherDataResponse createWeatherData(@Valid @RequestBody WeatherDataRequest weatherDataRequest) {
        return weatherDataService.createWeatherData(weatherDataRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<WeatherDataResponse> updateWeatherData(@PathVariable Long id, @Valid @RequestBody WeatherDataRequest weatherDataRequest) {
        return ResponseEntity.ok(weatherDataService.updateWeatherData(id, weatherDataRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWeatherData(@PathVariable Long id) {
        weatherDataService.deleteWeatherData(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/location/{location}")
    public List<WeatherDataResponse> getWeatherDataByLocation(@PathVariable String location) {
        return weatherDataService.getWeatherDataByLocation(location);
    }

    @GetMapping("/date-range")
    public List<WeatherDataResponse> getWeatherDataByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return weatherDataService.getWeatherDataByDateRange(startDate, endDate);
    }

    @GetMapping("/location-and-date")
    public ResponseEntity<WeatherDataResponse> getWeatherDataByLocationAndDate(
            @RequestParam String location,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return weatherDataService.getWeatherDataByLocationAndDate(location, date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}