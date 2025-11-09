package com.example.aiagentsystem.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfig {

    @Bean
    public CircuitBreaker marketDataCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("marketData");
    }

    @Bean
    public CircuitBreaker aiServiceCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("aiService");
    }

    @Bean
    public Retry defaultRetry(RetryRegistry registry) {
        return registry.retry("default");
    }

    @Bean
    public RateLimiter apiRateLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter("api");
    }
}