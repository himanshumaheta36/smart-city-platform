package com.smartcity.orchestration.service;

import com.smartcity.orchestration.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {
    
    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);
    
    private final WebClient webClient;
    private final String apiKey;
    private final String model;
    private final String baseUrl;
    private final boolean enabled;
    
    public GeminiService(
            WebClient.Builder webClientBuilder,
            @Value("${gemini.api-key:}") String apiKey,
            @Value("${gemini.model:gemini-2.0-flash}") String model,
            @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta}") String baseUrl,
            @Value("${gemini.enabled:true}") boolean enabled) {
        
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.apiKey = apiKey;
        this.model = model;
        this.baseUrl = baseUrl;
        this.enabled = enabled && apiKey != null && !apiKey.isEmpty();
        
        log.info("🤖 Gemini Service initialisé - Enabled: {}, Model: {}", this.enabled, model);
    }
    
    /**
     * Génère un plan de journée intelligent avec Gemini
     */
    public Mono<String> generateDayPlan(
            String userRequest,
            AirQualityInfo airQuality,
            List<TransportOption> transports,
            List<EventInfo> events) {
        
        if (!enabled) {
            log.info("⚠️ Gemini désactivé, utilisation du mode règles");
            return Mono.just(generateRuleBasedPlan(userRequest, airQuality, transports, events));
        }
        
        String prompt = buildDayPlanPrompt(userRequest, airQuality, transports, events);
        return callGemini(prompt);
    }
    
    /**
     * Chat interactif avec Gemini
     */
    public Mono<String> chat(String userMessage, String context) {
        if (!enabled) {
            return Mono.just(generateSimpleResponse(userMessage));
        }
        
        String prompt = buildChatPrompt(userMessage, context);
        return callGemini(prompt);
    }
    
    /**
     * Appel à l'API Gemini
     */
    private Mono<String> callGemini(String prompt) {
        log.info("🤖 Appel Gemini API...");
        
        // Structure de la requête Gemini
        Map<String, Object> request = Map.of(
            "contents", List.of(
                Map.of(
                    "parts", List.of(
                        Map.of("text", prompt)
                    )
                )
            ),
            "generationConfig", Map.of(
                "temperature", 0.7,
                "topK", 40,
                "topP", 0.95,
                "maxOutputTokens", 2048
            ),
            "safetySettings", List.of(
                Map.of("category", "HARM_CATEGORY_HARASSMENT", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                Map.of("category", "HARM_CATEGORY_HATE_SPEECH", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                Map.of("category", "HARM_CATEGORY_SEXUALLY_EXPLICIT", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                Map.of("category", "HARM_CATEGORY_DANGEROUS_CONTENT", "threshold", "BLOCK_MEDIUM_AND_ABOVE")
            )
        );
        
        String endpoint = String.format("/models/%s:generateContent?key=%s", model, apiKey);
        
        return webClient.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(30))
                .map(this::extractGeminiResponse)
                .doOnSuccess(response -> log.info("✅ Réponse Gemini reçue ({} caractères)", response.length()))
                .doOnError(e -> log.error("❌ Erreur Gemini: {}", e.getMessage()))
                .onErrorResume(e -> {
                    log.warn("⚠️ Fallback vers réponse par défaut");
                    return Mono.just(generateFallbackResponse(prompt));
                });
    }
    
    /**
     * Extraction de la réponse Gemini
     */
    @SuppressWarnings("unchecked")
    private String extractGeminiResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                if (content != null) {
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if (parts != null && !parts.isEmpty()) {
                        return (String) parts.get(0).get("text");
                    }
                }
            }
            
            // Vérifier les erreurs de blocage
            if (response.containsKey("promptFeedback")) {
                Map<String, Object> feedback = (Map<String, Object>) response.get("promptFeedback");
                if ("BLOCKED".equals(feedback.get("blockReason"))) {
                    return "Je ne peux pas répondre à cette demande. Veuillez reformuler.";
                }
            }
        } catch (Exception e) {
            log.error("Erreur extraction réponse Gemini: {}", e.getMessage());
        }
        return "Désolé, je n'ai pas pu générer une réponse.";
    }
    
    /**
     * Construction du prompt pour le plan de journée
     */
    private String buildDayPlanPrompt(String userRequest, AirQualityInfo airQuality,
                                      List<TransportOption> transports, List<EventInfo> events) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("""
            Tu es un assistant de planification urbaine intelligent pour une Smart City française.
            Tu dois créer un plan de journée personnalisé et optimisé.
            
            RÈGLES:
            - Réponds TOUJOURS en français
            - Propose des horaires réalistes
            - Tiens compte de la qualité de l'air pour les activités extérieures
            - Suggère les meilleurs transports
            - Intègre les événements disponibles si pertinents
            - Sois concis mais complet
            - Utilise des emojis pour rendre le plan visuel
            
            """);
        
        sb.append("DEMANDE DE L'UTILISATEUR:\n");
        sb.append(userRequest != null ? userRequest : "Planifier une journée agréable");
        sb.append("\n\n");
        
        sb.append("DONNÉES EN TEMPS RÉEL:\n\n");
        
        // Qualité de l'air
        sb.append("🌫️ QUALITÉ DE L'AIR:\n");
        if (airQuality != null) {
            sb.append(String.format("• Zone: %s\n", airQuality.getZoneName()));
            sb.append(String.format("• Indice AQI: %.0f (%s)\n", 
                airQuality.getAqiValue(), airQuality.getAqiCategory()));
            sb.append(String.format("• PM2.5: %.1f µg/m³\n", airQuality.getPm25()));
            if (airQuality.getAqiValue() > 100) {
                sb.append("⚠️ ATTENTION: Qualité de l'air dégradée!\n");
            }
        }
        
        // Transports
        sb.append("\n🚌 TRANSPORTS DISPONIBLES:\n");
        if (transports != null && !transports.isEmpty()) {
            transports.stream().limit(6).forEach(t -> {
                sb.append(String.format("• %s %s: %s → %s (%s)\n", 
                    t.getType(), t.getLineNumber(), 
                    t.getStartStation() != null ? t.getStartStation() : "Départ",
                    t.getEndStation() != null ? t.getEndStation() : "Arrivée",
                    t.getStatus()));
            });
        }
        
        // Événements
        sb.append("\n📅 ÉVÉNEMENTS AUJOURD'HUI:\n");
        if (events != null && !events.isEmpty()) {
            events.stream().limit(5).forEach(e -> {
                sb.append(String.format("• %s à %s (%s) - %s - %s\n", 
                    e.getTitle(), 
                    e.getLocation(),
                    e.getEventType(),
                    e.getStartDateTime() != null ? e.getStartDateTime().toLocalTime().toString() : "Horaire flexible",
                    Boolean.TRUE.equals(e.getIsFree()) ? "Gratuit" : "Payant"));
            });
        } else {
            sb.append("• Aucun événement spécial aujourd'hui\n");
        }
        
        sb.append("""
            
            GÉNÈRE UN PLAN DE JOURNÉE avec:
            1. Un titre accrocheur
            2. Un planning horaire détaillé (matin, midi, après-midi, soir)
            3. Les transports suggérés pour chaque déplacement
            4. Des conseils basés sur la qualité de l'air
            5. Des recommandations personnalisées
            
            Format le plan de manière claire et agréable à lire.
            """);
        
        return sb.toString();
    }
    
    /**
     * Construction du prompt pour le chat
     */
    private String buildChatPrompt(String userMessage, String context) {
        return String.format("""
            Tu es un assistant virtuel pour une Smart City française.
            Tu aides les citoyens à naviguer dans leur ville.
            
            RÈGLES:
            - Réponds TOUJOURS en français
            - Sois amical et utile
            - Donne des informations pratiques
            - Utilise des emojis appropriés
            - Sois concis (max 3-4 phrases pour les questions simples)
            
            CONTEXTE ACTUEL DE LA VILLE:
            %s
            
            QUESTION DE L'UTILISATEUR:
            %s
            
            Réponds de manière naturelle et utile.
            """, context, userMessage);
    }
    
    /**
     * Génération de plan basé sur des règles (fallback)
     */
    private String generateRuleBasedPlan(String userRequest, AirQualityInfo airQuality,
                                         List<TransportOption> transports, List<EventInfo> events) {
        StringBuilder plan = new StringBuilder();
        
        plan.append("# 📋 VOTRE PLAN DE JOURNÉE\n\n");
        
        // Alerte qualité d'air si nécessaire
        if (airQuality != null && airQuality.getAqiValue() != null) {
            if (airQuality.getAqiValue() > 100) {
                plan.append("## ⚠️ Alerte Qualité de l'Air\n");
                plan.append(String.format("L'indice AQI est de **%.0f** (%s). ", 
                    airQuality.getAqiValue(), airQuality.getAqiCategory()));
                plan.append("Privilégiez les activités en intérieur.\n\n");
            } else if (airQuality.getAqiValue() <= 50) {
                plan.append("## ✅ Excellente Qualité de l'Air\n");
                plan.append("Parfait pour les activités en extérieur!\n\n");
            }
        }
        
        // Planning
        plan.append("## ⏰ Planning Suggéré\n\n");
        
        plan.append("### 🌅 Matin (8h - 12h)\n");
        plan.append("**08:00** - Petit-déjeuner\n");
        plan.append("**09:00** - ");
        if (userRequest != null && userRequest.toLowerCase().contains("sport")) {
            plan.append("Séance de sport matinale (jogging, vélo)\n");
        } else {
            plan.append("Démarrage de la journée\n");
        }
        
        if (events != null && !events.isEmpty()) {
            EventInfo morningEvent = events.stream()
                .filter(e -> e.getStartDateTime() != null && 
                            e.getStartDateTime().getHour() < 12)
                .findFirst()
                .orElse(null);
            if (morningEvent != null) {
                plan.append(String.format("**10:00** - 📅 %s à %s\n", 
                    morningEvent.getTitle(), morningEvent.getLocation()));
            }
        }
        
        plan.append("\n### 🍽️ Midi (12h - 14h)\n");
        plan.append("**12:30** - Déjeuner\n\n");
        
        plan.append("### ☀️ Après-midi (14h - 18h)\n");
        if (transports != null && !transports.isEmpty()) {
            TransportOption t = transports.get(0);
            plan.append(String.format("**14:00** - Déplacement en %s (ligne %s)\n", 
                t.getType(), t.getLineNumber()));
        }
        plan.append("**14:30** - Activités de l'après-midi\n");
        
        if (events != null && !events.isEmpty()) {
            EventInfo afternoonEvent = events.stream()
                .filter(e -> e.getStartDateTime() != null && 
                            e.getStartDateTime().getHour() >= 14 &&
                            e.getStartDateTime().getHour() < 18)
                .findFirst()
                .orElse(null);
            if (afternoonEvent != null) {
                plan.append(String.format("**16:00** - 📅 %s à %s\n", 
                    afternoonEvent.getTitle(), afternoonEvent.getLocation()));
            }
        }
        
        plan.append("\n### 🌙 Soir (18h+)\n");
        plan.append("**19:00** - Dîner\n");
        plan.append("**21:00** - Soirée libre ou retour\n\n");
        
        // Recommandations
        plan.append("## 💡 Recommandations\n\n");
        if (airQuality != null && airQuality.getAqiValue() != null && airQuality.getAqiValue() <= 50) {
            plan.append("- 🌳 Profitez des parcs et espaces verts\n");
        }
        if (transports != null && !transports.isEmpty()) {
            plan.append("- 🚌 Utilisez les transports en commun pour vos déplacements\n");
        }
        if (events != null && !events.isEmpty()) {
            long freeCount = events.stream().filter(e -> Boolean.TRUE.equals(e.getIsFree())).count();
            plan.append(String.format("- 🎉 %d événements disponibles dont %d gratuits\n", 
                events.size(), freeCount));
        }
        
        return plan.toString();
    }
    
    /**
     * Réponse simple pour le chat (fallback)
     */
    private String generateSimpleResponse(String userMessage) {
        String lower = userMessage.toLowerCase();
        
        if (lower.contains("bonjour") || lower.contains("salut") || lower.contains("hello")) {
            return "👋 Bonjour! Je suis votre assistant Smart City. Comment puis-je vous aider aujourd'hui?\n\n" +
                   "Je peux vous aider avec:\n" +
                   "• 📋 Planifier votre journée\n" +
                   "• 🚌 Les transports\n" +
                   "• 🌫️ La qualité de l'air\n" +
                   "• 📅 Les événements";
        }
        
        if (lower.contains("transport") || lower.contains("bus") || lower.contains("metro") || lower.contains("métro")) {
            return "🚌 **Transports en commun**\n\n" +
                   "Je peux vous aider à trouver les meilleures lignes de transport.\n" +
                   "Demandez-moi de planifier un trajet ou consultez les lignes disponibles!";
        }
        
        if (lower.contains("air") || lower.contains("pollution") || lower.contains("qualité")) {
            return "🌫️ **Qualité de l'air**\n\n" +
                   "Je surveille la qualité de l'air en temps réel.\n" +
                   "Je vous recommande les meilleures zones et horaires pour vos activités extérieures.";
        }
        
        if (lower.contains("événement") || lower.contains("event") || lower.contains("sortir") || lower.contains("activité")) {
            return "📅 **Événements**\n\n" +
                   "Plusieurs événements sont disponibles dans la ville!\n" +
                   "Demandez-moi de vous montrer les événements du jour ou de rechercher par thème.";
        }
        
        if (lower.contains("plan") || lower.contains("journée") || lower.contains("programme")) {
            return "📋 **Planification**\n\n" +
                   "Je peux créer un plan de journée personnalisé pour vous!\n" +
                   "Dites-moi vos préférences (sport, culture, détente...) et votre localisation.";
        }
        
        if (lower.contains("merci") || lower.contains("thanks")) {
            return "😊 Avec plaisir! N'hésitez pas si vous avez d'autres questions.";
        }
        
        return "🤔 Je suis votre assistant Smart City. Je peux vous aider avec:\n\n" +
               "• **\"Planifie ma journée\"** - Plan personnalisé\n" +
               "• **\"Qualité de l'air\"** - État de l'air\n" +
               "• **\"Transports\"** - Lignes disponibles\n" +
               "• **\"Événements\"** - Activités du jour\n\n" +
               "Que souhaitez-vous faire?";
    }
    
    /**
     * Réponse de fallback en cas d'erreur Gemini
     */
    private String generateFallbackResponse(String prompt) {
        return "⚠️ Je rencontre des difficultés temporaires avec mon service d'IA.\n\n" +
               "Voici ce que je peux vous proposer:\n" +
               "• Consultez les données en temps réel via les endpoints dédiés\n" +
               "• Réessayez dans quelques instants\n\n" +
               "En attendant, voici un résumé des services disponibles:\n" +
               "- 🌫️ /orchestration/air-quality/{zone}\n" +
               "- 🚌 /orchestration/transports\n" +
               "- 📅 /orchestration/events";
    }
    
    /**
     * Vérifie si Gemini est disponible
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Test de connexion à Gemini
     */
    public Mono<Boolean> testConnection() {
        if (!enabled) {
            return Mono.just(false);
        }
        
        return callGemini("Dis simplement 'OK' si tu fonctionnes.")
                .map(response -> response != null && !response.isEmpty())
                .onErrorReturn(false);
    }
}