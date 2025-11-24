package com.smartcity.orchestration.controller;

import com.smartcity.orchestration.model.JourneyPlan;
import com.smartcity.orchestration.model.TransportOption;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/orchestration")
@CrossOrigin(origins = "http://localhost:3000")
public class OrchestrationController {

    private final WebClient webClient;

    public OrchestrationController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api").build();
    }

    @PostMapping("/plan-journey")
    public Mono<JourneyPlan> planJourney(@RequestParam String startLocation, 
                                       @RequestParam String endLocation) {
        
        JourneyPlan plan = new JourneyPlan(startLocation, endLocation);
        
        // Étape 1: Vérifier la qualité de l'air (SOAP via REST adapter)
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/air-quality/air-quality")
                        .queryParam("location", endLocation)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(airQualityResponse -> {
                    // Simuler l'analyse de la qualité de l'air
                    boolean isGoodAirQuality = !endLocation.toLowerCase().contains("industrial");
                    plan.setAirQualityGood(isGoodAirQuality);
                    
                    if (!isGoodAirQuality) {
                        plan.setRecommendation("Qualité d'air médiocre. Recommandation: Utiliser les transports en commun ou vélo.");
                    } else {
                        plan.setRecommendation("Qualité d'air bonne. Tous les modes de transport recommandés.");
                    }
                    
                    // Étape 2: Obtenir les options de transport (REST)
                    return webClient.get()
                            .uri("/mobility/transport-options")
                            .retrieve()
                            .bodyToMono(String.class)
                            .map(transportResponse -> {
                                // Simuler les options de transport
                                plan.setTransportOptions(List.of(
                                    createTransportOption("Bus", "Ligne 72", "10min", 15, "Actif"),
                                    createTransportOption("Métro", "Ligne B", "5min", 8, "Actif"),
                                    createTransportOption("Vélo", "Vélib", "2min", 12, "Disponible")
                                ));
                                return plan;
                            });
                });
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
    public String health() {
        return "Orchestration Service is healthy";
    }

    @GetMapping("/test")
    public JourneyPlan testJourneyPlan() {
        JourneyPlan plan = new JourneyPlan("Centre-ville", "Quartier Nord");
        plan.setAirQualityGood(true);
        plan.setRecommendation("Qualité d'air bonne. Tous les modes de transport recommandés.");
        plan.setTransportOptions(List.of(
            new TransportOption("Bus", "Ligne 72", "10min", 15, "Actif"),
            new TransportOption("Métro", "Ligne B", "5min", 8, "Actif")
        ));
        return plan;
    }
}