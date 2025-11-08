package com.example.aiagentsystem.service;

import com.example.aiagentsystem.dto.AgentChatRequest;
import com.example.aiagentsystem.dto.AgentChatResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Random;

@Service
public class ChatService {

    private final Random random = new Random();

    public Mono<AgentChatResponse> processChat(AgentChatRequest request) {
        // TODO: Integrate with Spring AI or OpenAI API for real LLM responses
        
        String[] agents = {"Market Analyzer", "Risk Monitor", "Trade Executor"};
        String[] responses = {
            "Based on current market conditions, I recommend a cautious approach.",
            "The technical indicators suggest a potential breakout in the next session.",
            "Risk levels are within acceptable parameters for this strategy.",
            "I'm seeing unusual volume patterns that warrant further investigation."
        };

        String agentName = agents[random.nextInt(agents.length)];
        String response = responses[random.nextInt(responses.length)];

        return Mono.just(new AgentChatResponse(agentName, response, Instant.now()));
    }
}