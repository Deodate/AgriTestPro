package com.AgriTest.service;

import com.AgriTest.dto.ResultsComparisonRequest;
import java.io.ByteArrayOutputStream;
 
public interface ResultsComparisonService {
    ByteArrayOutputStream generateReport(ResultsComparisonRequest request);
} 