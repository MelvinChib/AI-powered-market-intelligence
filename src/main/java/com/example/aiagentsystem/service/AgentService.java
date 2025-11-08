package com.example.aiagentsystem.service;

import com.example.aiagentsystem.dto.AgentCommunicationDTO;
import com.example.aiagentsystem.dto.AgentDTO;
import com.example.aiagentsystem.dto.AgentInsightDTO;
import com.example.aiagentsystem.model.AgentInsight;
import com.example.aiagentsystem.repository.AgentInsightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
public class AgentService {

    private final AgentInsightRepository insightRepository;

    private final Random random = new Random();

    @Autowired(required = false)
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    public AgentService(AgentInsightRepository insightRepository) {
        this.insightRepository = insightRepository;
    }

    public Flux<AgentDTO> getAgentPerformance() {
        return Flux.fromIterable(List.of(
            new AgentDTO("agent-1", "Market Analyzer", "ANALYST", 0.85 + random.nextGaussian() * 0.05, 
                        15000 + random.nextGaussian() * 1000, 0.98),
            new AgentDTO("agent-2", "Risk Monitor", "RISK_MANAGER", 0.92 + random.nextGaussian() * 0.03, 
                        8000 + random.nextGaussian() * 500, 0.99),
            new AgentDTO("agent-3", "Trade Executor", "TRADER", 0.78 + random.nextGaussian() * 0.07, 
                        22000 + random.nextGaussian() * 2000, 0.95)
        ));
    }

    public Mono<AgentDTO> getAgentDetails(String id) {
        return getAgentPerformance()
            .filter(agent -> agent.id().equals(id))
            .next();
    }

    public Flux<AgentCommunicationDTO> getAgentCommunications(String id) {
        return Flux.fromIterable(List.of(
            new AgentCommunicationDTO("agent-1", "agent-2", "Risk threshold exceeded for TSLA", Instant.now().minusSeconds(300)),
            new AgentCommunicationDTO("agent-2", "agent-3", "Reduce position size by 20%", Instant.now().minusSeconds(180)),
            new AgentCommunicationDTO("agent-3", "agent-1", "Trade executed successfully", Instant.now().minusSeconds(60))
        ));
    }

    public Flux<AgentInsightDTO> getLastInsights() {
        return insightRepository.findTop100ByOrderByTimestampDesc()
            .map(insight -> new AgentInsightDTO(insight.agentName(), insight.message(), 
                                              insight.sentiment(), insight.timestamp()));
    }

    @Scheduled(fixedRate = 5000)
    public void generateInsights() {
        String[] agents = {"Market Analyzer", "Risk Monitor", "Trade Executor"};
        String[] messages = {
            "Bullish sentiment detected in tech sector",
            "High volatility warning for energy stocks",
            "Correlation breakdown between AAPL and MSFT",
            "Strong buy signal for defensive stocks"
        };
        String[] sentiments = {"POSITIVE", "NEGATIVE", "NEUTRAL"};

        String agent = agents[random.nextInt(agents.length)];
        String message = messages[random.nextInt(messages.length)];
        String sentiment = sentiments[random.nextInt(sentiments.length)];
        
        AgentInsightDTO insight = new AgentInsightDTO(agent, message, sentiment, Instant.now());
        
        // Save to database
        insightRepository.save(new AgentInsight(null, agent, message, sentiment, Instant.now()))
            .subscribe();
        
        // Stream via Redis (if available)
        if (redisTemplate != null) {
            redisTemplate.convertAndSend("ai-insights", insight).subscribe();
        }
    }
}