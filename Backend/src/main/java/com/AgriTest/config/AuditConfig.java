// File: src/main/java/com/AgriTest/config/AuditConfig.java
package com.AgriTest.config;

import com.AgriTest.util.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> SecurityUtils.getCurrentUsername().or(() -> Optional.of("system"));
    }
}