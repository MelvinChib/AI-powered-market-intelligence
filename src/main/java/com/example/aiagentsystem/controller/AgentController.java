package com.example.aiagentsystem.controller;

import com.example.aiagentsystem.dto.*;
import com.example.aiagentsystem.service.AgentService;
import com.example.aiagentsystem.service.ChatService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentService agentService;
    private final ChatService chatService;

    public AgentController(AgentService agentService, ChatService chatService) {
        this.agentService = agentService;
        this.chatService = chatService;
    }

    @GetMapping("/performance")
    public Flux<AgentDTO> getAgentPerformance() {
        return agentService.getAgentPerformance();
    }

    @GetMapping("/{id}/details")
    public Mono<AgentDTO> getAgentDetails(@PathVariable String id) {
        return agentService.getAgentDetails(id);
    }

    @GetMapping("/{id}/communications")
    public Flux<AgentCommunicationDTO> getAgentCommunications(@PathVariable String id) {
        return agentService.getAgentCommunications(id);
    }

    @GetMapping("/insights")
    public Flux<AgentInsightDTO> getLastInsights() {
        return agentService.getLastInsights();
    }

    @PostMapping("/chat")
    public Mono<AgentChatResponse> chat(@RequestBody AgentChatRequest request) {
        return chatService.processChat(request);
    }
}