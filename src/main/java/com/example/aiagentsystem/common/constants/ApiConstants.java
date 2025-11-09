package com.example.aiagentsystem.common.constants;

public final class ApiConstants {
    public static final String API_V1 = "/api/v1";
    public static final String AUTH_ENDPOINT = API_V1 + "/auth";
    public static final String AGENTS_ENDPOINT = API_V1 + "/agents";
    public static final String MARKET_ENDPOINT = API_V1 + "/market";
    public static final String RISK_ENDPOINT = API_V1 + "/risk";
    public static final String SIMULATION_ENDPOINT = API_V1 + "/simulations";
    public static final String NOTIFICATION_ENDPOINT = API_V1 + "/notifications";
    
    public static final String WEBSOCKET_ENDPOINT = "/ws";
    
    private ApiConstants() {}
}