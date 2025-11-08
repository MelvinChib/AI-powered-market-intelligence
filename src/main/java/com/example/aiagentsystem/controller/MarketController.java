package com.example.aiagentsystem.controller;

import com.example.aiagentsystem.dto.MarketRelationshipDTO;
import com.example.aiagentsystem.dto.MarketSummaryDTO;
import com.example.aiagentsystem.service.MarketDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketDataService marketDataService;

    public MarketController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/summary")
    public Flux<MarketSummaryDTO> getMarketSummary() {
        return marketDataService.getMarketSummary();
    }

    @GetMapping("/relationships")
    public Flux<MarketRelationshipDTO> getMarketRelationships() {
        return marketDataService.getMarketRelationships();
    }
}