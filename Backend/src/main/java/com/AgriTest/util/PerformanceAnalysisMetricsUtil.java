package com.AgriTest.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerformanceAnalysisMetricsUtil {
    
    /**
     * Extracts numerical metrics from trial results and seasonal performance metrics
     * 
     * @param trialResults String containing trial results
     * @param seasonalPerformanceMetrics String containing seasonal performance metrics
     * @return Map of extracted metrics
     */
    public static Map<String, Double> extractPerformanceMetrics(
            String trialResults, 
            String seasonalPerformanceMetrics) {
        
        Map<String, Double> metrics = new HashMap<>();
        
        // Extract yield increase percentage
        metrics.putAll(extractPercentageMetrics(trialResults, "Yield", "increased"));
        
        // Extract drought resistance metrics
        metrics.putAll(extractQualitativeMetrics(seasonalPerformanceMetrics, "Drought resistance"));
        
        // Extract water usage reduction
        metrics.putAll(extractPercentageMetrics(seasonalPerformanceMetrics, "Water usage", "Reduced"));
        
        return metrics;
    }
    
    /**
     * Extracts percentage-based metrics using regex
     * 
     * @param text Text to parse
     * @param metricName Name of the metric to extract
     * @param triggerWord Word indicating the metric type
     * @return Map of extracted percentage metrics
     */
    public static Map<String, Double> extractPercentageMetrics(
            String text, 
            String metricName, 
            String triggerWord) {
        
        Map<String, Double> percentageMetrics = new HashMap<>();
        
        if (text == null) return percentageMetrics;
        
        // Regex pattern to match percentage values
        Pattern pattern = Pattern.compile(metricName + "\\s+" + triggerWord + "\\s+by\\s+(\\d+(\\.\\d+)?)%");
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            try {
                double percentage = Double.parseDouble(matcher.group(1));
                percentageMetrics.put(metricName + "Increase", percentage);
            } catch (NumberFormatException e) {
                // Silently handle parsing errors
            }
        }
        
        return percentageMetrics;
    }
    
    /**
     * Extracts qualitative metrics with ratings
     * 
     * @param text Text to parse
     * @param metricName Name of the metric to extract
     * @return Map of extracted qualitative metrics
     */
    public static Map<String, Double> extractQualitativeMetrics(
            String text, 
            String metricName) {
        
        Map<String, Double> qualitativeMetrics = new HashMap<>();
        
        if (text == null) return qualitativeMetrics;
        
        // Mapping qualitative ratings to numerical values
        Map<String, Double> qualitativeRatings = new HashMap<>() {{
            put("Low", 1.0);
            put("Medium", 5.0);
            put("High", 9.0);
            put("Excellent", 10.0);
        }};
        
        Pattern pattern = Pattern.compile(metricName + ":\\s+(\\w+)");
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            String rating = matcher.group(1);
            Double ratingValue = qualitativeRatings.getOrDefault(rating, 5.0);
            qualitativeMetrics.put(metricName + "Rating", ratingValue);
        }
        
        return qualitativeMetrics;
    }
    
    /**
     * Calculates overall performance score
     * 
     * @param effectivenessRating Base effectiveness rating
     * @param metrics Extracted metrics
     * @return Calculated performance score
     */
    public static double calculatePerformanceScore(
            Double effectivenessRating, 
            Map<String, Double> metrics) {
        
        if (effectivenessRating == null) effectivenessRating = 5.0;
        
        double score = effectivenessRating;
        
        // Adjust score based on additional metrics
        for (Double metricValue : metrics.values()) {
            score += metricValue * 0.1;
        }
        
        // Normalize score
        return Math.min(Math.max(score, 0), 10);
    }
}