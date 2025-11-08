package com.example.aiagentsystem.controller;

import com.example.aiagentsystem.dto.SimulationResultDTO;
import com.example.aiagentsystem.service.SimulationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/simulations")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/run")
    public Mono<UUID> runSimulation() {
        return simulationService.runSimulation();
    }

    @GetMapping("/{id}/results")
    public Mono<SimulationResultDTO> getSimulationResults(@PathVariable UUID id) {
        return simulationService.getSimulationResults(id);
    }
}