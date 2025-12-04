package com.smartcity.orchestration.controller;

import com.smartcity.orchestration.model.*;
import com.smartcity.orchestration.service.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orchestration")
@CrossOrigin(origins = "*")
public class OrchestrationController {

    private final DayPlannerService dayPlannerService;
    private final AirQualityClient airQualityClient;
    private final MobilityClient mobilityClient;
    private final EventsClient eventsClient;
    private final GeminiService geminiService;

    public OrchestrationController(DayPlannerService dayPlannerService,
                                  AirQualityClient airQualityClient,
                                  MobilityClient mobilityClient,
                                  EventsClient eventsClient,
                                  GeminiService geminiService) {
        this.dayPlannerService = dayPlannerService;
        this.airQualityClient = airQualityClient;
        this.mobilityClient = mobilityClient;
        this.eventsClient = eventsClient;
        this.geminiService = geminiService;
    }

    /**
     * 🤖 Planification intelligente avec Gemini
     */
    @PostMapping("/plan-day")
    public Mono<DayPlan> planDay(
            @RequestParam(required = false) String preferences,
            @RequestParam(required = false, defaultValue = "Centre-ville") String location) {
        return dayPlannerService.planDay(preferences, location);
    }

    /**
     * 💬 Chat avec l'assistant Gemini
     */
    @PostMapping("/chat")
    public Mono<ChatResponse> chat(@RequestBody ChatRequest request) {
        return dayPlannerService.processChat(request);
    }

    /**
     * 🗺️ Données complètes de la ville
     */
    @GetMapping("/city-data")
    public Mono<Map<String, Object>> getCityData(
            @RequestParam(required = false, defaultValue = "Centre-ville") String location) {
        
        return Mono.zip(
            airQualityClient.getAirQualityByZone(location),
            mobilityClient.getTransportLines(),
            eventsClient.getUpcomingEvents()
        ).map(tuple -> {
            Map<String, Object> data = new HashMap<>();
            data.put("airQuality", tuple.getT1());
            data.put("transports", tuple.getT2());
            data.put("events", tuple.getT3());
            data.put("timestamp", LocalDateTime.now());
            data.put("location", location);
            data.put("aiEnabled", geminiService.isEnabled());
            return data;
        });
    }

    /**
     * Ancien endpoint maintenu pour compatibilité
     */
    @PostMapping("/plan-journey")
    public Mono<JourneyPlan> planJourney(
            @RequestParam String startLocation,
            @RequestParam String endLocation) {
        
        return Mono.zip(
            airQualityClient.getAirQualityByZone(endLocation),
            mobilityClient.getTransportLines()
        ).map(tuple -> {
            JourneyPlan plan = new JourneyPlan(startLocation, endLocation);
            
            AirQualityInfo airQuality = tuple.getT1();
            plan.setAirQuality(airQuality);
            plan.setAirQualityGood(airQuality.isAcceptableForOutdoor());
            
            if (airQuality.isGoodQuality()) {
                plan.setRecommendation("✅ Qualité d'air excellente. Tous les modes de transport recommandés.");
            } else if (airQuality.isAcceptableForOutdoor()) {
                plan.setRecommendation("🟡 Qualité d'air acceptable. Transport en commun ou vélo recommandé.");
            } else {
                plan.setRecommendation("🔴 Qualité d'air médiocre. Privilégiez les transports en commun.");
            }
            
            plan.setTransportOptions(tuple.getT2());
            return plan;
        });
    }

    /**
     * 🌫️ Qualité de l'air par zone
     */
    @GetMapping("/air-quality/{zone}")
    public Mono<AirQualityInfo> getAirQuality(@PathVariable String zone) {
        return airQualityClient.getAirQualityByZone(zone);
    }

    @GetMapping("/air-quality")
    public Mono<List<AirQualityInfo>> getAllAirQuality() {
        return airQualityClient.getAllZones();
    }

    /**
     * 🚌 Transports
     */
    @GetMapping("/transports")
    public Mono<List<TransportOption>> getTransports() {
        return mobilityClient.getTransportLines();
    }

    /**
     * 📅 Événements
     */
    @GetMapping("/events")
    public Mono<List<EventInfo>> getEvents() {
        return eventsClient.getUpcomingEvents();
    }

    @GetMapping("/events/search")
    public Mono<List<EventInfo>> searchEvents(@RequestParam String keyword) {
        return eventsClient.searchEvents(keyword);
    }

    /**
     * ❤️ Health check
     */
    @GetMapping("/health")
    public Mono<Map<String, Object>> health() {
        return geminiService.testConnection()
            .map(geminiOk -> {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "UP");
                response.put("service", "Orchestration Service");
                response.put("timestamp", LocalDateTime.now());
                response.put("gemini", Map.of(
                    "enabled", geminiService.isEnabled(),
                    "connected", geminiOk
                ));
                response.put("features", List.of("day-planning", "chat", "real-time-data", "gemini-ai"));
                return response;
            });
    }

    /**
     * 👋 Message de bienvenue
     */
    @GetMapping("/welcome")
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "👋 Bienvenue sur Smart City Orchestration Service!");
        response.put("aiEnabled", geminiService.isEnabled());
        response.put("endpoints", Map.of(
            "planDay", "POST /orchestration/plan-day?preferences=...&location=...",
            "chat", "POST /orchestration/chat",
            "cityData", "GET /orchestration/city-data",
            "airQuality", "GET /orchestration/air-quality/{zone}",
            "transports", "GET /orchestration/transports",
            "events", "GET /orchestration/events"
        ));
        return response;
    }
}