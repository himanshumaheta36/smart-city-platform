package com.smartcity.orchestration.service;

import com.smartcity.orchestration.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class DayPlannerService {
    
    private static final Logger log = LoggerFactory.getLogger(DayPlannerService.class);
    
    private final AirQualityClient airQualityClient;
    private final MobilityClient mobilityClient;
    private final EventsClient eventsClient;
    private final GeminiService geminiService;
    
    public DayPlannerService(AirQualityClient airQualityClient,
                            MobilityClient mobilityClient,
                            EventsClient eventsClient,
                            GeminiService geminiService) {
        this.airQualityClient = airQualityClient;
        this.mobilityClient = mobilityClient;
        this.eventsClient = eventsClient;
        this.geminiService = geminiService;
    }
    
    /**
     * Planifie une journée complète avec l'aide de Gemini
     */
    public Mono<DayPlan> planDay(String userPreferences, String location) {
        log.info("📋 Planification de journée - Préférences: {}, Location: {}", userPreferences, location);
        
        String targetLocation = location != null ? location : "Centre-ville";
        
        // Collecter toutes les données en parallèle
        Mono<AirQualityInfo> airQualityMono = airQualityClient.getAirQualityByZone(targetLocation);
        Mono<List<TransportOption>> transportsMono = mobilityClient.getTransportLines();
        Mono<List<EventInfo>> eventsMono = eventsClient.getUpcomingEvents();
        
        return Mono.zip(airQualityMono, transportsMono, eventsMono)
            .flatMap(tuple -> {
                AirQualityInfo airQuality = tuple.getT1();
                List<TransportOption> transports = tuple.getT2();
                List<EventInfo> events = tuple.getT3();
                
                log.info("✅ Données collectées - AQI: {}, Transports: {}, Events: {}", 
                    airQuality.getAqiValue(), transports.size(), events.size());
                
                // Créer le plan de base
                DayPlan plan = createBasePlan(userPreferences, targetLocation, airQuality, transports, events);
                
                // Enrichir avec Gemini
                return geminiService.generateDayPlan(userPreferences, airQuality, transports, events)
                    .map(aiSummary -> {
                        plan.setAiSummary(aiSummary);
                        plan.setAiRecommendations(generateRecommendations(airQuality, events));
                        return plan;
                    });
            })
            .doOnSuccess(plan -> log.info("✅ Plan généré avec succès"))
            .doOnError(e -> log.error("❌ Erreur planification: {}", e.getMessage()));
    }
    
    /**
     * Traite un message de chat
     */
    public Mono<ChatResponse> processChat(ChatRequest request) {
        String message = request.getMessage();
        String location = request.getLocation() != null ? request.getLocation() : "Centre-ville";
        String sessionId = request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString();
        
        log.info("💬 Chat reçu: {} (session: {})", message, sessionId);
        
        // Si c'est une demande de planification
        if (isPlanningRequest(message)) {
            return planDay(message, location)
                .map(plan -> {
                    ChatResponse response = new ChatResponse();
                    response.setMessage(plan.getAiSummary());
                    response.setSuggestedPlan(plan);
                    response.setSessionId(sessionId);
                    response.setSuccess(true);
                    return response;
                })
                .onErrorResume(e -> {
                    log.error("Erreur planification chat: {}", e.getMessage());
                    return Mono.just(ChatResponse.error("Désolé, je n'ai pas pu créer votre plan."));
                });
        }
        
        // Sinon, répondre avec le chatbot
        return buildContext(location)
            .flatMap(context -> geminiService.chat(message, context))
            .map(aiResponse -> {
                ChatResponse response = new ChatResponse();
                response.setMessage(aiResponse);
                response.setSessionId(sessionId);
                response.setSuccess(true);
                return response;
            })
            .onErrorResume(e -> {
                log.error("Erreur chat: {}", e.getMessage());
                return Mono.just(ChatResponse.error("Désolé, une erreur s'est produite."));
            });
    }
    
    private boolean isPlanningRequest(String message) {
        String lower = message.toLowerCase();
        return lower.contains("plan") || 
               lower.contains("journée") || 
               lower.contains("programme") ||
               lower.contains("organise") ||
               lower.contains("prépare");
    }
    
    private DayPlan createBasePlan(String userPreferences, String location,
                                   AirQualityInfo airQuality, 
                                   List<TransportOption> transports,
                                   List<EventInfo> events) {
        DayPlan plan = new DayPlan();
        plan.setDate(LocalDate.now());
        plan.setUserPreferences(userPreferences);
        plan.setAirQuality(airQuality);
        plan.setTransportOptions(transports);
        
        // Ajouter des warnings si nécessaire
        if (airQuality != null && airQuality.getAqiValue() != null) {
            if (airQuality.getAqiValue() > 150) {
                plan.addWarning("🔴 Qualité de l'air très dégradée - Évitez les activités extérieures");
            } else if (airQuality.getAqiValue() > 100) {
                plan.addWarning("🟠 Qualité de l'air dégradée - Limitez les activités extérieures prolongées");
            }
        }
        
        // Créer les activités suggérées
        addActivities(plan, events, transports, userPreferences);
        
        return plan;
    }
    
    private void addActivities(DayPlan plan, List<EventInfo> events, 
                               List<TransportOption> transports, String preferences) {
        // Matin
        plan.addActivity(new DayPlan.PlanActivity(
            "08:00", "Petit-déjeuner", "Commencez la journée", "Domicile ou café", "MEAL"
        ));
        
        if (preferences != null && preferences.toLowerCase().contains("sport")) {
            plan.addActivity(new DayPlan.PlanActivity(
                "09:00", "Activité sportive", "Sport matinal", "Parc ou salle", "SPORT"
            ));
        }
        
        // Ajouter événements pertinents
        if (events != null) {
            events.stream().limit(2).forEach(event -> {
                String time = event.getStartDateTime() != null ? 
                    event.getStartDateTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "14:00";
                
                DayPlan.PlanActivity activity = new DayPlan.PlanActivity(
                    time, event.getTitle(), event.getDescription(), 
                    event.getLocation(), event.getEventType()
                );
                activity.setRelatedEvent(event);
                
                if (transports != null && !transports.isEmpty()) {
                    activity.setTransport(transports.get(0));
                }
                
                plan.addActivity(activity);
            });
        }
        
        // Repas
        plan.addActivity(new DayPlan.PlanActivity(
            "12:30", "Déjeuner", "Pause repas", "Restaurant", "MEAL"
        ));
        
        plan.addActivity(new DayPlan.PlanActivity(
            "19:30", "Dîner", "Repas du soir", "Restaurant ou domicile", "MEAL"
        ));
    }
    
    private Mono<String> buildContext(String location) {
        return Mono.zip(
            airQualityClient.getAirQualityByZone(location),
            mobilityClient.getTransportLines(),
            eventsClient.getUpcomingEvents()
        ).map(tuple -> {
            StringBuilder ctx = new StringBuilder();
            
            AirQualityInfo aq = tuple.getT1();
            ctx.append(String.format("Qualité de l'air à %s: AQI %.0f (%s)\n", 
                aq.getZoneName(), aq.getAqiValue(), aq.getAqiCategory()));
            
            ctx.append(String.format("Transports: %d lignes disponibles\n", tuple.getT2().size()));
            
            List<EventInfo> events = tuple.getT3();
            ctx.append(String.format("Événements: %d disponibles\n", events.size()));
            if (!events.isEmpty()) {
                ctx.append("Exemples: ");
                events.stream().limit(3).forEach(e -> 
                    ctx.append(e.getTitle()).append(", "));
            }
            
            return ctx.toString();
        }).onErrorReturn("Contexte non disponible");
    }
    
    private String generateRecommendations(AirQualityInfo airQuality, List<EventInfo> events) {
        StringBuilder recs = new StringBuilder();
        
        if (airQuality != null && airQuality.getAqiValue() != null) {
            if (airQuality.getAqiValue() <= 50) {
                recs.append("🌳 Qualité de l'air excellente - Profitez des activités en plein air!\n");
            } else if (airQuality.getAqiValue() <= 100) {
                recs.append("🌤️ Qualité de l'air acceptable - Activités extérieures modérées.\n");
            } else {
                recs.append("😷 Qualité de l'air dégradée - Privilégiez l'intérieur.\n");
            }
        }
        
        if (events != null && !events.isEmpty()) {
            long freeEvents = events.stream().filter(e -> Boolean.TRUE.equals(e.getIsFree())).count();
            recs.append(String.format("🎉 %d événements disponibles (%d gratuits)\n", events.size(), freeEvents));
        }
        
        return recs.toString();
    }
}