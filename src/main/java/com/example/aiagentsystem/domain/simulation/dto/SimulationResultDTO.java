package com.example.aiagentsystem.dto;

import java.util.List;
import java.util.UUID;

public record SimulationResultDTO(UUID id, String agentId, double pnl, List<TradeActionDTO> actions) {}