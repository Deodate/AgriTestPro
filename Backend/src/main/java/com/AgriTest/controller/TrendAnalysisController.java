package com.AgriTest.controller;

import com.AgriTest.dto.TrendAnalysisRequest;
import com.AgriTest.dto.TrendAnalysisResult;
import com.AgriTest.service.TrendAnalysisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics/trends")
public class TrendAnalysisController {

    @Autowired
    private TrendAnalysisService trendAnalysisService;

    @PostMapping("/analyze")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST') or hasRole('MANAGER')")
    public ResponseEntity<TrendAnalysisResult> analyzeTrend(@Valid @RequestBody TrendAnalysisRequest request) {
        TrendAnalysisResult result = trendAnalysisService.analyzeTrend(request);
        return ResponseEntity.ok(result);
    }
}