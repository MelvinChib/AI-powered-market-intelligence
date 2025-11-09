package com.example.aiagentsystem.api.v1;

import com.example.aiagentsystem.common.constants.ApiConstants;
import com.example.aiagentsystem.domain.market.dto.MarketSummaryDTO;
import com.example.aiagentsystem.domain.market.dto.MarketRelationshipDTO;
import com.example.aiagentsystem.domain.market.service.MarketDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ApiConstants.MARKET_ENDPOINT)
@Tag(name = "Market Data", description = "Financial market data operations")
public class MarketControllerV1 {

    private final MarketDataService marketDataService;

    public MarketControllerV1(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/summary")
    @Operation(summary = "Get market summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public Mono<MarketSummaryDTO> getMarketSummary() {
        return marketDataService.getMarketSummary();
    }

    @GetMapping("/relationships")
    @Operation(summary = "Get market relationships")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public Flux<MarketRelationshipDTO> getMarketRelationships() {
        return marketDataService.getMarketRelationships();
    }
}