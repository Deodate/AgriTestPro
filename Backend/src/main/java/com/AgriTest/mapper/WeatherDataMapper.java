// File: src/main/java/com/AgriTest/mapper/WeatherDataMapper.java
package com.AgriTest.mapper;

import com.AgriTest.dto.WeatherDataRequest;
import com.AgriTest.dto.WeatherDataResponse;
import com.AgriTest.model.WeatherData;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeatherDataMapper {
    
    public WeatherDataResponse toDto(WeatherData weatherData) {
        if (weatherData == null) {
            return null;
        }
        
        return WeatherDataResponse.builder()
                .id(weatherData.getId())
                .location(weatherData.getLocation())
                .date(weatherData.getDate())
                .temperature(weatherData.getTemperature())
                .humidity(weatherData.getHumidity())
                .rainfall(weatherData.getRainfall())
                .notes(weatherData.getNotes())
                .recordedAt(weatherData.getRecordedAt())
                .build();
    }
    
    public List<WeatherDataResponse> toDtoList(List<WeatherData> weatherDataList) {
        return weatherDataList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public WeatherData toEntity(WeatherDataRequest request) {
        if (request == null) {
            return null;
        }
        
        WeatherData weatherData = new WeatherData();
        weatherData.setLocation(request.getLocation());
        weatherData.setDate(request.getDate());
        weatherData.setTemperature(request.getTemperature());
        weatherData.setHumidity(request.getHumidity());
        weatherData.setRainfall(request.getRainfall());
        weatherData.setNotes(request.getNotes());
        weatherData.setRecordedAt(LocalDateTime.now());
        
        return weatherData;
    }
}