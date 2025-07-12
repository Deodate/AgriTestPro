package com.AgriTest.service;

import com.AgriTest.dto.TrendAnalysisRequest;
import com.AgriTest.dto.TrendAnalysisResult;
import com.AgriTest.model.AnalyticsData;
import com.AgriTest.repository.AnalyticsDataRepository;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TrendAnalysisService {

    @Autowired
    private AnalyticsDataRepository analyticsDataRepository;
    
    public TrendAnalysisResult analyzeTrend(TrendAnalysisRequest request) {
        // Fetch data for analysis
        List<AnalyticsData> dataPoints = analyticsDataRepository.findDataForTrendAnalysis(
                request.getDataSource(),
                request.getEntityType(),
                request.getEntityId(),
                request.getStartDate(),
                request.getEndDate()
        );
        
        if (dataPoints.isEmpty()) {
            throw new IllegalArgumentException("No data points found for the specified criteria");
        }
        
        // Convert to array for statistical analysis
        double[] values = dataPoints.stream()
                .mapToDouble(AnalyticsData::getValue)
                .toArray();
        
        // Create x values (days from start)
        LocalDate firstDate = dataPoints.get(0).getDataDate();
        double[] xValues = dataPoints.stream()
                .mapToDouble(data -> ChronoUnit.DAYS.between(firstDate, data.getDataDate()))
                .toArray();
        
        // Basic statistics
        DescriptiveStatistics stats = new DescriptiveStatistics(values);
        
        // Linear regression
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < xValues.length; i++) {
            regression.addData(xValues[i], values[i]);
        }
        
        // Calculate trend values
        List<TrendAnalysisResult.DataPoint> timeSeriesData = IntStream.range(0, dataPoints.size())
                .mapToObj(i -> {
                    AnalyticsData data = dataPoints.get(i);
                    double trendValue = regression.predict(xValues[i]);
                    return TrendAnalysisResult.DataPoint.builder()
                            .date(data.getDataDate())
                            .value(data.getValue())
                            .trendValue(trendValue)
                            .build();
                })
                .collect(Collectors.toList());
        
        // Calculate growth rate (compound annual growth rate)
        double firstValue = values[0];
        double lastValue = values[values.length - 1];
        double years = ChronoUnit.DAYS.between(firstDate, dataPoints.get(dataPoints.size() - 1).getDataDate()) / 365.0;
        double growthRate = (Math.pow(lastValue / firstValue, 1 / years) - 1) * 100; // in percentage
        
        // Check for seasonality (simple approach)
        boolean hasSeasonality = false;
        int seasonalPeriod = 0;
        if (values.length >= 12) { // Need enough data to detect seasonality
            // Simple autocorrelation for seasonality detection
            hasSeasonality = detectSeasonality(values);
            seasonalPeriod = estimateSeasonalPeriod(values);
        }
        
        // Build the result
        return TrendAnalysisResult.builder()
                .dataSource(request.getDataSource())
                .entityName(request.getEntityType() + "-" + request.getEntityId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .timeSeriesData(timeSeriesData)
                .growthRate(growthRate)
                .averageValue(stats.getMean())
                .minValue(stats.getMin())
                .maxValue(stats.getMax())
                .standardDeviation(stats.getStandardDeviation())
                .slopeCoefficient(regression.getSlope())
                .rSquared(regression.getRSquare())
                .hasSeasonality(hasSeasonality)
                .seasonalPeriod(hasSeasonality ? seasonalPeriod : null)
                .additionalMetrics(calculateAdditionalMetrics(values, xValues))
                .build();
    }
    
    private boolean detectSeasonality(double[] values) {
        // Simple approach using autocorrelation
        // More sophisticated approaches would use FFT or other seasonal decomposition
        double[] autocorr = calculateAutocorrelation(values, values.length / 3);
        
        // Check if any autocorrelation value (except for lag 0) is above threshold
        for (int i = 1; i < autocorr.length; i++) {
            if (autocorr[i] > 0.5) { // Threshold for significant autocorrelation
                return true;
            }
        }
        return false;
    }
    
    private int estimateSeasonalPeriod(double[] values) {
        // Find peak in autocorrelation to estimate period
        double[] autocorr = calculateAutocorrelation(values, values.length / 3);
        int maxLag = 0;
        double maxAutocorr = 0;
        
        // Start from lag 2 to avoid high autocorrelation at small lags
        for (int i = 2; i < autocorr.length; i++) {
            if (autocorr[i] > maxAutocorr) {
                maxAutocorr = autocorr[i];
                maxLag = i;
            }
        }
        
        return maxLag > 0 ? maxLag : 0;
    }
    
    private double[] calculateAutocorrelation(double[] values, int maxLag) {
        double mean = Arrays.stream(values).average().orElse(0);
        double[] centeredValues = Arrays.stream(values).map(v -> v - mean).toArray();
        
        double[] autocorr = new double[maxLag + 1];
        double variance = Arrays.stream(centeredValues).map(v -> v * v).sum();
        
        for (int lag = 0; lag <= maxLag; lag++) {
            double sum = 0;
            for (int i = 0; i < values.length - lag; i++) {
                sum += centeredValues[i] * centeredValues[i + lag];
            }
            autocorr[lag] = sum / variance;
        }
        
        return autocorr;
    }
    
    private Map<String, Object> calculateAdditionalMetrics(double[] values, double[] xValues) {
        Map<String, Object> metrics = new HashMap<>();
        
        // Add any additional metrics here
        DescriptiveStatistics stats = new DescriptiveStatistics(values);
        metrics.put("median", stats.getPercentile(50));
        metrics.put("quartile1", stats.getPercentile(25));
        metrics.put("quartile3", stats.getPercentile(75));
        metrics.put("skewness", stats.getSkewness());
        metrics.put("kurtosis", stats.getKurtosis());
        
        // Calculate coefficient of variation
        double cv = stats.getStandardDeviation() / stats.getMean() * 100; // in percentage
        metrics.put("coefficientOfVariation", cv);
        
        return metrics;
    }
}