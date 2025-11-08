package com.example.aiagentsystem.service;

import com.example.aiagentsystem.dto.SimulationResultDTO;
import com.example.aiagentsystem.dto.TradeActionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class SimulationService {

    @Autowired(required = false)
    private ReactiveRedisTemplate<String, Object> redisTemplate;
    private final Random random = new Random();

    public SimulationService() {}

    public Mono<UUID> runSimulation() {
        UUID simulationId = UUID.randomUUID();
        
        // Simulate trading actions
        List<TradeActionDTO> actions = List.of(
            new TradeActionDTO("AAPL", "BUY", 150.0, Instant.now().minusSeconds(3600)),
            new TradeActionDTO("GOOGL", "SELL", 2800.0, Instant.now().minusSeconds(1800)),
            new TradeActionDTO("MSFT", "BUY", 300.0, Instant.now().minusSeconds(900))
        );
        
        // Stream simulation events (if Redis available)
        if (redisTemplate != null) {
            actions.forEach(action -> 
                redisTemplate.convertAndSend("simulation", action).subscribe()
            );
        }
        
        return Mono.just(simulationId);
    }

    public Mono<SimulationResultDTO> getSimulationResults(UUID id) {
        List<TradeActionDTO> actions = List.of(
            new TradeActionDTO("AAPL", "BUY", 150.0, Instant.now().minusSeconds(3600)),
            new TradeActionDTO("GOOGL", "SELL", 2800.0, Instant.now().minusSeconds(1800)),
            new TradeActionDTO("MSFT", "BUY", 300.0, Instant.now().minusSeconds(900))
        );
        
        double pnl = 5000 + random.nextGaussian() * 1000;
        
        return Mono.just(new SimulationResultDTO(id, "agent-1", pnl, actions));
    }
}