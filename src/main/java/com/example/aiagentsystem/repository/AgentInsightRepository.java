package com.example.aiagentsystem.repository;

import com.example.aiagentsystem.model.AgentInsight;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface AgentInsightRepository extends R2dbcRepository<AgentInsight, Long> {
    Flux<AgentInsight> findTop100ByOrderByTimestampDesc();
}