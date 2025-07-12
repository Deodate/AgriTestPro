// File: src/main/java/com/AgriTest/service/impl/WeatherDataServiceImpl.java
package com.AgriTest.service.impl;

import com.AgriTest.dto.WeatherDataRequest;
import com.AgriTest.dto.WeatherDataResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.WeatherDataMapper;
import com.AgriTest.model.WeatherData;
import com.AgriTest.repository.WeatherDataRepository;
import com.AgriTest.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherDataServiceImpl implements WeatherDataService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private WeatherDataMapper weatherDataMapper;

    @Override
    public List<WeatherDataResponse> getAllWeatherData() {
        List<WeatherData> weatherDataList = weatherDataRepository.findAll();
        return weatherDataMapper.toDtoList(weatherDataList);
    }

    @Override
    public Optional<WeatherDataResponse> getWeatherDataById(Long id) {
        return weatherDataRepository.findById(id)
                .map(weatherDataMapper::toDto);
    }

    @Override
    public WeatherDataResponse createWeatherData(WeatherDataRequest weatherDataRequest) {
        WeatherData weatherData = weatherDataMapper.toEntity(weatherDataRequest);
        WeatherData savedWeatherData = weatherDataRepository.save(weatherData);
        return weatherDataMapper.toDto(savedWeatherData);
    }

    @Override
    public WeatherDataResponse updateWeatherData(Long id, WeatherDataRequest weatherDataRequest) {
        WeatherData existingWeatherData = weatherDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weather data not found with id: " + id));
        
        // Update the fields
        existingWeatherData.setLocation(weatherDataRequest.getLocation());
        existingWeatherData.setDate(weatherDataRequest.getDate());
        existingWeatherData.setTemperature(weatherDataRequest.getTemperature());
        existingWeatherData.setHumidity(weatherDataRequest.getHumidity());
        existingWeatherData.setRainfall(weatherDataRequest.getRainfall());
        existingWeatherData.setNotes(weatherDataRequest.getNotes());
        
        WeatherData updatedWeatherData = weatherDataRepository.save(existingWeatherData);
        return weatherDataMapper.toDto(updatedWeatherData);
    }

    @Override
    public void deleteWeatherData(Long id) {
        weatherDataRepository.deleteById(id);
    }

    @Override
    public List<WeatherDataResponse> getWeatherDataByLocation(String location) {
        List<WeatherData> weatherDataList = weatherDataRepository.findByLocation(location);
        return weatherDataMapper.toDtoList(weatherDataList);
    }

    @Override
    public List<WeatherDataResponse> getWeatherDataByDateRange(LocalDate startDate, LocalDate endDate) {
        List<WeatherData> weatherDataList = weatherDataRepository.findByDateBetween(startDate, endDate);
        return weatherDataMapper.toDtoList(weatherDataList);
    }

    @Override
    public Optional<WeatherDataResponse> getWeatherDataByLocationAndDate(String location, LocalDate date) {
        return weatherDataRepository.findByLocationAndDate(location, date)
                .map(weatherDataMapper::toDto);
    }
}