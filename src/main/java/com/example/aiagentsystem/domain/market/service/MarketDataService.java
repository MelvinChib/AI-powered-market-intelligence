package com.example.aiagentsystem.domain.market.service;

import com.example.aiagentsystem.domain.market.dto.MarketSummaryDTO;
import com.example.aiagentsystem.domain.market.dto.MarketRelationshipDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketDataService {
    Mono<MarketSummaryDTO> getMarketSummary();
    Flux<MarketRelationshipDTO> getMarketRelationships();
    Flux<MarketSummaryDTO> streamMarketData();
}