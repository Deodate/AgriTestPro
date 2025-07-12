// File: src/main/java/com/AgriTest/config/SchedulerConfig.java
package com.AgriTest.config;

import com.AgriTest.service.TestScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private TestScheduleService testScheduleService;
    
    // Run every day at 6:00 AM
    @Scheduled(cron = "0 0 6 * * ?")
    public void executeScheduledTests() {
        testScheduleService.executeAllDueSchedules();
    }
}