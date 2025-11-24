package com.smartcity.orchestration.controller;

import com.smartcity.orchestration.model.JourneyPlan;
import com.smartcity.orchestration.model.TransportOption;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orchestration")
@CrossOrigin(origins = "*")
public class OrchestrationController {

    private final WebClient webClient;

    public OrchestrationController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PostMapping("/plan-journey")
    public Mono<JourneyPlan> planJourney(@RequestParam String startLocation,
            @RequestParam String endLocation) {

        JourneyPlan plan = new JourneyPlan(startLocation, endLocation);

        // Simuler la vérification de la qualité de l'air
        boolean isGoodAirQuality = !endLocation.toLowerCase().contains("industrial");
        plan.setAirQualityGood(isGoodAirQuality);

        if (!isGoodAirQuality) {
            plan.setRecommendation(
                    "Qualité d'air médiocre. Recommandation: Utiliser les transports en commun ou vélo.");
        } else {
            plan.setRecommendation("Qualité d'air bonne. Tous les modes de transport recommandés.");
        }

        // Simuler les options de transport
        plan.setTransportOptions(List.of(
                createTransportOption("Bus", "Ligne 72", "10min", 15, "Actif"),
                createTransportOption("Métro", "Ligne B", "5min", 8, "Actif"),
                createTransportOption("Vélo", "Vélib", "2min", 12, "Disponible")));

        return Mono.just(plan);
    }

    private TransportOption createTransportOption(
            String type, String line, String schedule, int duration, String status) {
        TransportOption option = new TransportOption();
        option.setType(type);
        option.setLine(line);
        option.setSchedule(schedule);
        option.setDuration(duration);
        option.setStatus(status);
        return option;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Orchestration Service");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }
}