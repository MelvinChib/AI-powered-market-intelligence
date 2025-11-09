package com.example.aiagentsystem.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class ObservabilityConfig {

    @Bean
    public WebFilter metricsWebFilter(MeterRegistry meterRegistry) {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            Timer.Sample sample = Timer.start(meterRegistry);
            return chain.filter(exchange)
                    .doFinally(signalType -> {
                        Timer.Builder timerBuilder = Timer.builder("http.server.requests")
                                .tag("method", exchange.getRequest().getMethod().name())
                                .tag("uri", exchange.getRequest().getPath().value())
                                .tag("status", String.valueOf(exchange.getResponse().getStatusCode().value()));
                        sample.stop(timerBuilder.register(meterRegistry));
                    });
        };
    }
}