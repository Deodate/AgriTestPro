// File: src/main/java/com/AgriTest/config/SchedulerConfig.java
package com.AgriTest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    // This enables scheduling features in the application
    // Actual scheduled tasks are defined in service classes
}