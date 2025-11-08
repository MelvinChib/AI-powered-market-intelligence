package com.example.aiagentsystem.service;

import com.example.aiagentsystem.dto.MarketRelationshipDTO;
import com.example.aiagentsystem.dto.MarketSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

@Service
public class MarketDataService {

    @Autowired(required = false)
    private ReactiveRedisTemplate<String, Object> redisTemplate;
    private final Random random = new Random();

    public MarketDataService() {}

    public Flux<MarketSummaryDTO> getMarketSummary() {
        return Flux.fromIterable(List.of(
            new MarketSummaryDTO("AAPL", 150.0 + random.nextGaussian() * 5, random.nextGaussian() * 2, random.nextGaussian()),
            new MarketSummaryDTO("GOOGL", 2800.0 + random.nextGaussian() * 50, random.nextGaussian() * 10, random.nextGaussian()),
            new MarketSummaryDTO("MSFT", 300.0 + random.nextGaussian() * 10, random.nextGaussian() * 3, random.nextGaussian()),
            new MarketSummaryDTO("TSLA", 200.0 + random.nextGaussian() * 20, random.nextGaussian() * 5, random.nextGaussian())
        ));
    }

    public Flux<MarketRelationshipDTO> getMarketRelationships() {
        return Flux.fromIterable(List.of(
            new MarketRelationshipDTO("AAPL", "MSFT", 0.7 + random.nextGaussian() * 0.1),
            new MarketRelationshipDTO("GOOGL", "MSFT", 0.6 + random.nextGaussian() * 0.1),
            new MarketRelationshipDTO("TSLA", "AAPL", 0.3 + random.nextGaussian() * 0.1)
        ));
    }

    @Scheduled(fixedRate = 1000)
    public void streamMarketData() {
        if (redisTemplate != null) {
            getMarketSummary()
                .collectList()
                .flatMap(data -> redisTemplate.convertAndSend("market-data", data))
                .subscribe();
        }
    }

    public Mono<Void> cacheMarketData(String key, Object data) {
        if (redisTemplate != null) {
            return redisTemplate.opsForValue().set(key, data).then();
        }
        return Mono.empty();
    }
}