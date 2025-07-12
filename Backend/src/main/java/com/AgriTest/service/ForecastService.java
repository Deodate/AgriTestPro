package com.AgriTest.service;

import com.AgriTest.dto.ForecastModelRequest;
import com.AgriTest.dto.ForecastModelResponse;
import com.AgriTest.dto.ForecastResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.*;
import com.AgriTest.repository.AnalyticsDataRepository;
import com.AgriTest.repository.ForecastModelRepository;
import com.AgriTest.repository.ForecastResultRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ForecastService {

    @Autowired
    private ForecastModelRepository forecastModelRepository;
    
    @Autowired
    private ForecastResultRepository forecastResultRepository;
    
    @Autowired
    private AnalyticsDataRepository analyticsDataRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Transactional
    public ForecastModelResponse createForecastModel(ForecastModelRequest request, Long userId) {
        ForecastModel model = ForecastModel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .dataSource(request.getDataSource())
                .forecastType(request.getForecastType())
                .forecastHorizon(request.getForecastHorizon())
                .isActive(true)
                .createdBy(userId)
                .status(ModelStatus.UNTRAINED)
                .build();
        
        // Convert parameters to JSON
        if (request.getModelParameters() != null && !request.getModelParameters().isEmpty()) {
            try {
                model.setModelParameters(objectMapper.writeValueAsString(request.getModelParameters()));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid model parameters");
            }
        }
        
        ForecastModel savedModel = forecastModelRepository.save(model);
        return mapToResponse(savedModel);
    }
    
    @Transactional
    public ForecastModelResponse updateForecastModel(Long id, ForecastModelRequest request) {
        ForecastModel model = forecastModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast model not found with id: " + id));
        
        model.setName(request.getName());
        model.setDescription(request.getDescription());
        model.setDataSource(request.getDataSource());
        model.setForecastType(request.getForecastType());
        model.setForecastHorizon(request.getForecastHorizon());
        
        // Set status to untrained if key parameters changed
        if (!model.getDataSource().equals(request.getDataSource()) || 
            model.getForecastType() != request.getForecastType()) {
            model.setStatus(ModelStatus.UNTRAINED);
        }
        
        // Update parameters
        if (request.getModelParameters() != null && !request.getModelParameters().isEmpty()) {
            try {
                model.setModelParameters(objectMapper.writeValueAsString(request.getModelParameters()));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid model parameters");
            }
        }
        
        ForecastModel updatedModel = forecastModelRepository.save(model);
        return mapToResponse(updatedModel);
    }
    
    public List<ForecastModelResponse> getAllForecastModels() {
        return forecastModelRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public Optional<ForecastModelResponse> getForecastModelById(Long id) {
        return forecastModelRepository.findById(id)
                .map(this::mapToResponse);
    }
    
    public List<ForecastModelResponse> getForecastModelsByUser(Long userId) {
        return forecastModelRepository.findByCreatedBy(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteForecastModel(Long id) {
        // Also delete all forecast results
        forecastResultRepository.deleteAll(
                forecastResultRepository.findByForecastModelIdOrderByForecastDate(id)
        );
        forecastModelRepository.deleteById(id);
    }
    
    @Transactional
    public ForecastModelResponse activateForecastModel(Long id) {
        ForecastModel model = forecastModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast model not found with id: " + id));
        
        model.setIsActive(true);
        return mapToResponse(forecastModelRepository.save(model));
    }
    
    @Transactional
    public ForecastModelResponse deactivateForecastModel(Long id) {
        ForecastModel model = forecastModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast model not found with id: " + id));
        
        model.setIsActive(false);
        return mapToResponse(forecastModelRepository.save(model));
    }
    
    @Async
    @Transactional
    public void trainModel(Long modelId) {
        ForecastModel model = forecastModelRepository.findById(modelId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast model not found with id: " + modelId));
        
        // Set status to training
        model.setStatus(ModelStatus.TRAINING);
        forecastModelRepository.save(model);
        
        try {
            // Fetch historical data for training
            List<AnalyticsData> historicalData = analyticsDataRepository.findByDataSourceAndDataDateBetween(
                    model.getDataSource(),
                    LocalDate.now().minusYears(2), // Use last 2 years of data for training
                    LocalDate.now()
            );
            
            if (historicalData.isEmpty()) {
                throw new IllegalStateException("No historical data available for training");
            }
            
            // Generate forecast based on model type
            List<ForecastResult> forecastResults = generateForecast(model, historicalData);
            
            // Save forecast results
            forecastResultRepository.saveAll(forecastResults);
            
            // Update model status
            model.setStatus(ModelStatus.TRAINED);
            model.setLastTrainedAt(LocalDateTime.now());
            forecastModelRepository.save(model);
            
        } catch (Exception e) {
            // Update model status to failed
            model.setStatus(ModelStatus.FAILED);
            forecastModelRepository.save(model);
            throw e;
        }
    }
    
    public ForecastResponse getForecast(Long modelId) {
        ForecastModel model = forecastModelRepository.findById(modelId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast model not found with id: " + modelId));
        
        if (model.getStatus() != ModelStatus.TRAINED) {
            throw new IllegalStateException("Forecast model is not trained yet");
        }
        
        // Get forecast results
        List<ForecastResult> forecastResults = forecastResultRepository.findByForecastModelIdOrderByForecastDate(modelId);
        
        if (forecastResults.isEmpty()) {
            throw new IllegalStateException("No forecast data available");
        }
        
        // Get historical data for comparison
        LocalDate lastForecastDate = forecastResults.get(forecastResults.size() - 1).getForecastDate();
        LocalDate firstForecastDate = forecastResults.get(0).getForecastDate();
        
        // Get historical data for same time period length before forecast
        long daysBetween = ChronoUnit.DAYS.between(firstForecastDate, lastForecastDate) + 1;
        LocalDate historyStartDate = firstForecastDate.minusDays(daysBetween);
        
        List<AnalyticsData> historicalData = analyticsDataRepository.findByDataSourceAndDataDateBetween(
                model.getDataSource(),
                historyStartDate,
                firstForecastDate.minusDays(1)
        );
        
        // Build response
        List<ForecastResponse.ForecastPoint> forecastPoints = forecastResults.stream()
                .map(this::mapToForecastPoint)
                .collect(Collectors.toList());
                
        List<ForecastResponse.ForecastPoint> historyPoints = historicalData.stream()
                .map(this::mapToHistoryPoint)
                .collect(Collectors.toList());
                
        // Calculate forecast accuracy metrics if we have actual data for some forecast periods
        double mape = 0.0;
        double rmse = 0.0;
        // Calculate MAPE and RMSE logic would go here if we have actual data
        
        return ForecastResponse.builder()
                .modelId(model.getId())
                .modelName(model.getName())
                .dataSource(model.getDataSource())
                .forecastData(forecastPoints)
                .historicalData(historyPoints)
                .mape(mape)
                .rmse(rmse)
                .lastHistoricalDate(historyPoints.isEmpty() ? null : 
                        historyPoints.get(historyPoints.size() - 1).getDate())
                .status(model.getStatus().toString())
                .build();
    }
    
    private List<ForecastResult> generateForecast(ForecastModel model, List<AnalyticsData> historicalData) {
        // Sort historical data by date
        List<AnalyticsData> sortedData = historicalData.stream()
                .sorted(Comparator.comparing(AnalyticsData::getDataDate))
                .collect(Collectors.toList());
                
        // Extract time series values
        double[] values = sortedData.stream()
                .mapToDouble(AnalyticsData::getValue)
                .toArray();
                
        // Generate forecast based on model type
        List<ForecastResult> results = new ArrayList<>();
        LocalDate lastDate = sortedData.get(sortedData.size() - 1).getDataDate();
        
        switch (model.getForecastType()) {
            case LINEAR_REGRESSION:
                results = generateLinearRegressionForecast(model, sortedData, lastDate);
                break;
            case SIMPLE_MOVING_AVERAGE:
                results = generateMovingAverageForecast(model, sortedData, lastDate);
                break;
            case EXPONENTIAL_SMOOTHING:
                results = generateExponentialSmoothingForecast(model, sortedData, lastDate);
                break;
            case ARIMA:
                // Simplified ARIMA implementation (this would be more complex in reality)
                results = generateArimaForecast(model, sortedData, lastDate);
                break;
            case SEASONAL_DECOMPOSITION:
                results = generateSeasonalForecast(model, sortedData, lastDate);
                break;
            default:
                throw new IllegalArgumentException("Unsupported forecast type: " + model.getForecastType());
        }
        
        return results;
    }
    
    private List<ForecastResult> generateLinearRegressionForecast(
            ForecastModel model, List<AnalyticsData> data, LocalDate lastDate) {
        
        // Create simple linear regression model
        SimpleRegression regression = new SimpleRegression();
        
        // Days from first observation as x values
        LocalDate firstDate = data.get(0).getDataDate();
        
        for (int i = 0; i < data.size(); i++) {
            AnalyticsData point = data.get(i);
            long daysFromStart = ChronoUnit.DAYS.between(firstDate, point.getDataDate());
            regression.addData(daysFromStart, point.getValue());
        }
        
        // Generate forecast
        List<ForecastResult> results = new ArrayList<>();
        
        for (int i = 1; i <= model.getForecastHorizon(); i++) {
            LocalDate forecastDate = lastDate.plusDays(i);
            
            long daysFromStart = ChronoUnit.DAYS.between(firstDate, forecastDate);
            double forecastValue = regression.predict(daysFromStart);
            
            // Calculate prediction intervals (95% confidence)
            double standardError = regression.getSlopeStdErr() * daysFromStart;
            double lowerBound = forecastValue - 1.96 * standardError;
            double upperBound = forecastValue + 1.96 * standardError;
            
            ForecastResult result = ForecastResult.builder()
                    .forecastModel(model)
                    .forecastDate(forecastDate)
                    .forecastValue(forecastValue)
                    .lowerBound(lowerBound)
                    .upperBound(upperBound)
                    .confidenceLevel(0.95)
                    .build();
                    
            results.add(result);
        }
        
        return results;
    }
    
    private List<ForecastResult> generateMovingAverageForecast(
            ForecastModel model, List<AnalyticsData> data, LocalDate lastDate) {
        
        // Default window size is 7 days (weekly)
        int windowSize = 7;
        
        // Check if custom window size is specified in model parameters
        if (model.getModelParameters() != null) {
            try {
                Map<String, Object> params = objectMapper.readValue(model.getModelParameters(), Map.class);
                if (params.containsKey("windowSize")) {
                    windowSize = Integer.parseInt(params.get("windowSize").toString());
                }
            } catch (Exception e) {
                // Use default window size if parsing fails
            }
        }
        
        // Ensure we have enough data
        if (data.size() < windowSize) {
            throw new IllegalStateException("Not enough data for moving average with window size " + windowSize);
        }
        
        // Calculate last moving average
        double[] lastValues = data.subList(data.size() - windowSize, data.size()).stream()
                .mapToDouble(AnalyticsData::getValue)
                .toArray();
                
        double lastAverage = Arrays.stream(lastValues).average().orElse(0);
        double stdDev = calculateStandardDeviation(lastValues);
        
        // Generate forecast (flat forecast using last average)
        List<ForecastResult> results = new ArrayList<>();
        
        for (int i = 1; i <= model.getForecastHorizon(); i++) {
            LocalDate forecastDate = lastDate.plusDays(i);
            
            ForecastResult result = ForecastResult.builder()
                    .forecastModel(model)
                    .forecastDate(forecastDate)
                    .forecastValue(lastAverage)
                    .lowerBound(lastAverage - 1.96 * stdDev)
                    .upperBound(lastAverage + 1.96 * stdDev)
                    .confidenceLevel(0.95)
                    .build();
                    
            results.add(result);
        }
        
        return results;
    }
    
    private List<ForecastResult> generateExponentialSmoothingForecast(
            ForecastModel model, List<AnalyticsData> data, LocalDate lastDate) {
        
        // Default alpha (smoothing factor)
        double alpha = 0.3;
        
        // Check if custom alpha is specified in model parameters
        if (model.getModelParameters() != null) {
            try {
                Map<String, Object> params = objectMapper.readValue(model.getModelParameters(), Map.class);
                if (params.containsKey("alpha")) {
                    alpha = Double.parseDouble(params.get("alpha").toString());
                }
            } catch (Exception e) {
                // Use default alpha if parsing fails
            }
        }
        
        // Calculate exponential smoothing
        double[] values = data.stream()
                .mapToDouble(AnalyticsData::getValue)
                .toArray();
                
        double lastSmoothed = values[0];
        for (int i = 1; i < values.length; i++) {
            lastSmoothed = alpha * values[i] + (1 - alpha) * lastSmoothed;
        }
        
        // Calculate variance for confidence intervals
        double[] errors = new double[values.length - 1];
        double smoothed = values[0];
        
        for (int i = 1; i < values.length; i++) {
            double forecast = smoothed;
            smoothed = alpha * values[i] + (1 - alpha) * smoothed;
            errors[i-1] = values[i] - forecast;
        }
        
        double errorStdDev = calculateStandardDeviation(errors);
        
        // Generate forecast
        List<ForecastResult> results = new ArrayList<>();
        
        for (int i = 1; i <= model.getForecastHorizon(); i++) {
            LocalDate forecastDate = lastDate.plusDays(i);
            
            ForecastResult result = ForecastResult.builder()
                    .forecastModel(model)
                    .forecastDate(forecastDate)
                    .forecastValue(lastSmoothed)
                    .lowerBound(lastSmoothed - 1.96 * errorStdDev * Math.sqrt(i))
                    .upperBound(lastSmoothed + 1.96 * errorStdDev * Math.sqrt(i))
                    .confidenceLevel(0.95)
                    .build();
                    
            results.add(result);
        }
        
        return results;
    }
    
    private List<ForecastResult> generateArimaForecast(
            ForecastModel model, List<AnalyticsData> data, LocalDate lastDate) {
        // This is a simplified ARIMA implementation
        // A real implementation would use a specialized library
        
        // For simplicity, we'll use a naive approach based on 
        // the average of recent changes in the series
        
        double[] values = data.stream()
                .mapToDouble(AnalyticsData::getValue)
                .toArray();
                
        // Calculate recent changes (last 10 periods or less if not available)
        int periods = Math.min(10, values.length - 1);
        double[] changes = new double[periods];
        
        for (int i = 0; i < periods; i++) {
            changes[i] = values[values.length - 1 - i] - values[values.length - 2 - i];
        }
        
        double averageChange = Arrays.stream(changes).average().orElse(0);
        double changeStdDev = calculateStandardDeviation(changes);
        
        // Start with last value
        double lastValue = values[values.length - 1];
        
        // Generate forecast
        List<ForecastResult> results = new ArrayList<>();
        
        for (int i = 1; i <= model.getForecastHorizon(); i++) {
            LocalDate forecastDate = lastDate.plusDays(i);
            double forecastValue = lastValue + (i * averageChange);
            
            ForecastResult result = ForecastResult.builder()
                    .forecastModel(model)
                    .forecastDate(forecastDate)
                    .forecastValue(forecastValue)
                    .lowerBound(forecastValue - 1.96 * changeStdDev * Math.sqrt(i))
                    .upperBound(forecastValue + 1.96 * changeStdDev * Math.sqrt(i))
                    .confidenceLevel(0.95)
                    .build();
                    
            results.add(result);
        }
        
        return results;
    }
    
    private List<ForecastResult> generateSeasonalForecast(
            ForecastModel model, List<AnalyticsData> data, LocalDate lastDate) {
        
        // Default seasonal period (7 days for weekly seasonality)
        int seasonalPeriod = 7;
        
        // Check if custom seasonal period is specified in model parameters
        if (model.getModelParameters() != null) {
            try {
                Map<String, Object> params = objectMapper.readValue(model.getModelParameters(), Map.class);
                if (params.containsKey("seasonalPeriod")) {
                    seasonalPeriod = Integer.parseInt(params.get("seasonalPeriod").toString());
                }
            } catch (Exception e) {
                // Use default seasonal period if parsing fails
            }
        }
        
        // Need at least 2 full seasons of data
        if (data.size() < seasonalPeriod * 2) {
            throw new IllegalStateException("Not enough data for seasonal forecast. Need at least " + 
                                           (seasonalPeriod * 2) + " data points.");
        }
        
        // Calculate seasonal indices
        double[] values = data.stream()
                .mapToDouble(AnalyticsData::getValue)
                .toArray();
                
        // Calculate average for each season position
        double[] seasonalIndices = new double[seasonalPeriod];
        int[] counts = new int[seasonalPeriod];
        
        for (int i = 0; i < values.length; i++) {
            int seasonPosition = i % seasonalPeriod;
            seasonalIndices[seasonPosition] += values[i];
            counts[seasonPosition]++;
        }
        
        // Calculate average
        for (int i = 0; i < seasonalPeriod; i++) {
            seasonalIndices[i] = counts[i] > 0 ? seasonalIndices[i] / counts[i] : 1.0;
        }
        
        // Normalize seasonal indices
        double seasonalSum = Arrays.stream(seasonalIndices).sum();
        double seasonalAvg = seasonalSum / seasonalPeriod;
        
        for (int i = 0; i < seasonalPeriod; i++) {
            seasonalIndices[i] = seasonalIndices[i] / seasonalAvg;
        }
        
        // Calculate trend
        SimpleRegression regression = new SimpleRegression();
        
        for (int i = 0; i < values.length; i++) {
            // Deseasonalize the data
            int seasonPosition = i % seasonalPeriod;
            double deseasonalized = values[i] / seasonalIndices[seasonPosition];
            regression.addData(i, deseasonalized);
        }
        
        // Generate forecast
        List<ForecastResult> results = new ArrayList<>();
        
        for (int i = 1; i <= model.getForecastHorizon(); i++) {
            LocalDate forecastDate = lastDate.plusDays(i);
            int position = (values.length + i - 1) % seasonalPeriod;
            
            // Trend component
            double trend = regression.predict(values.length + i - 1);
            
            // Apply seasonal factor
            double forecastValue = trend * seasonalIndices[position];
            
            // Simplified confidence interval
            double stdError = regression.getSlopeStdErr() * (values.length + i - 1);
            double lowerBound = (trend - 1.96 * stdError) * seasonalIndices[position];
            double upperBound = (trend + 1.96 * stdError) * seasonalIndices[position];
            
            ForecastResult result = ForecastResult.builder()
                    .forecastModel(model)
                    .forecastDate(forecastDate)
                    .forecastValue(forecastValue)
                    .lowerBound(lowerBound)
                    .upperBound(upperBound)
                    .confidenceLevel(0.95)
                    .build();
                    
            results.add(result);
        }
        
        return results;
    }
    
    private double calculateStandardDeviation(double[] values) {
        DescriptiveStatistics stats = new DescriptiveStatistics(values);
        return stats.getStandardDeviation();
    }
    
    private ForecastResponse.ForecastPoint mapToForecastPoint(ForecastResult result) {
        return ForecastResponse.ForecastPoint.builder()
                .date(result.getForecastDate())
                .value(result.getForecastValue())
                .lowerBound(result.getLowerBound())
                .upperBound(result.getUpperBound())
                .isActual(false)
                .build();
    }
    
    private ForecastResponse.ForecastPoint mapToHistoryPoint(AnalyticsData data) {
        return ForecastResponse.ForecastPoint.builder()
                .date(data.getDataDate())
                .value(data.getValue())
                .lowerBound(null)
                .upperBound(null)
                .isActual(true)
                .build();
    }
    
    // Added the missing mapToResponse method
    private ForecastModelResponse mapToResponse(ForecastModel model) {
        Map<String, Object> parameters = null;
        
        if (model.getModelParameters() != null && !model.getModelParameters().isEmpty()) {
            try {
                parameters = objectMapper.readValue(model.getModelParameters(), Map.class);
            } catch (Exception e) {
                // Ignore parsing errors
            }
        }
        
        return ForecastModelResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .dataSource(model.getDataSource())
                .forecastType(model.getForecastType())
                .forecastHorizon(model.getForecastHorizon())
                .isActive(model.getIsActive())
                .status(model.getStatus())
                .lastTrainedAt(model.getLastTrainedAt())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .createdBy(model.getCreatedBy())
                .modelParameters(parameters)
                .build();
    }
}