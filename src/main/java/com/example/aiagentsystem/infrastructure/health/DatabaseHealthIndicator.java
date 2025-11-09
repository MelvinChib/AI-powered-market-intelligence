package com.example.aiagentsystem.health;

import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.ReactiveHealthIndicator;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DatabaseHealthIndicator implements ReactiveHealthIndicator {

    private final DatabaseClient databaseClient;

    public DatabaseHealthIndicator(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Health> health() {
        return databaseClient.sql("SELECT 1")
                .fetch()
                .first()
                .map(result -> Health.up()
                        .withDetail("database", "Available")
                        .build())
                .onErrorReturn(Health.down()
                        .withDetail("database", "Unavailable")
                        .build());
    }
}