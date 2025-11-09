package com.example.aiagentsystem.domain.market.service;

import com.example.aiagentsystem.domain.market.dto.MarketRelationshipDTO;
import com.example.aiagentsystem.domain.market.dto.MarketSummaryDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    @Override
    @CircuitBreaker(name = "marketData", fallbackMethod = "getMarketSummaryFallback")
    @Retry(name = "default")
    public Mono<MarketSummaryDTO> getMarketSummary() {
        return Mono.just(new MarketSummaryDTO(
                "S&P 500",
                new BigDecimal("4500.00"),
                new BigDecimal("2.5"),
                LocalDateTime.now()
        ));
    }

    @Override
    @CircuitBreaker(name = "marketData")
    public Flux<MarketRelationshipDTO> getMarketRelationships() {
        return Flux.just(
                new MarketRelationshipDTO("AAPL", "MSFT", 0.85),
                new MarketRelationshipDTO("GOOGL", "AMZN", 0.72)
        );
    }

    @Override
    public Flux<MarketSummaryDTO> streamMarketData() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(i -> new MarketSummaryDTO(
                        "Live Market",
                        new BigDecimal("4500.00").add(new BigDecimal(Math.random() * 100)),
                        new BigDecimal(Math.random() * 5),
                        LocalDateTime.now()
                ));
    }

    public Mono<MarketSummaryDTO> getMarketSummaryFallback(Exception ex) {
        return Mono.just(new MarketSummaryDTO(
                "Market Unavailable",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                LocalDateTime.now()
        ));
    }
}