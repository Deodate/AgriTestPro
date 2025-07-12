package com.AgriTest.service;

import com.AgriTest.dto.HistoricalDataRequest;
import com.AgriTest.model.HistoricalData;
import com.AgriTest.repository.HistoricalDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
public class HistoricalDataService {

    @Autowired
    private HistoricalDataRepository historicalDataRepository;

    @Transactional
    public HistoricalData saveHistoricalData(HistoricalDataRequest request) {
        HistoricalData historicalData = HistoricalData.builder()
                .testName(request.getTestName())
                .trialPhase(request.getTrialPhase())
                .productType(request.getProductType())
                .resultStatus(request.getResultStatus())
                .keywords(request.getKeywords())
                // createdBy will be set by authentication context in controller
                .build();

        // Parse date range (assuming format "YYYY-MM-DD to YYYY-MM-DD")
        if (request.getDateRange() != null && !request.getDateRange().isEmpty()) {
            try {
                String[] dates = request.getDateRange().split(" to ");
                if (dates.length == 2) {
                    historicalData.setStartDate(LocalDate.parse(dates[0].trim()));
                    historicalData.setEndDate(LocalDate.parse(dates[1].trim()));
                } else if (dates.length == 1) {
                     historicalData.setStartDate(LocalDate.parse(dates[0].trim()));
                     historicalData.setEndDate(LocalDate.parse(dates[0].trim())); // Assume single date is start and end
                }
            } catch (DateTimeParseException e) {
                // Handle parsing error, maybe log or throw a specific exception
                throw new IllegalArgumentException("Invalid date range format. Expected 'YYYY-MM-DD to YYYY-MM-DD' or 'YYYY-MM-DD'.");
            }
        }

        return historicalDataRepository.save(historicalData);
    }
} 