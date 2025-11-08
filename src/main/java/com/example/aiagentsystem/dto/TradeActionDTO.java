package com.example.aiagentsystem.dto;

import java.time.Instant;

public record TradeActionDTO(String symbol, String type, double price, Instant timestamp) {}