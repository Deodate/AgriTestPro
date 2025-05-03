package com.AgriTest.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableCaching
public class RateLimiterConfig {
    
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    
    @Bean
    public Map<String, Bucket> buckets() {
        return buckets;
    }
    
    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, this::newBucket);
    }
    
    private Bucket newBucket(String key) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(30, Refill.intervally(30, Duration.ofMinutes(1))))
                .addLimit(Bandwidth.classic(300, Refill.intervally(300, Duration.ofHours(1))))
                .build();
    }
} 