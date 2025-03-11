package com.AgriTest.config;

import com.AgriTest.util.ReportFileNameContext;
import com.AgriTest.util.DefaultReportTypeFileNameStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportNamingConfig {
    @Bean
    public ReportFileNameContext reportFileNameContext() {
        return new ReportFileNameContext(new DefaultReportTypeFileNameStrategy());
    }
}