package com.smartcity.orchestration.controller;

import com.smartcity.orchestration.model.ChatRequest;
import com.smartcity.orchestration.model.ChatResponse;
import com.smartcity.orchestration.service.DayPlannerService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orchestration/assistant")
@CrossOrigin(origins = "*")
public class ChatController {

    private final DayPlannerService dayPlannerService;

    public ChatController(DayPlannerService dayPlannerService) {
        this.dayPlannerService = dayPlannerService;
    }

    @PostMapping("/chat")
    public Mono<ChatResponse> chat(@RequestBody ChatRequest request) {
        return dayPlannerService.processChat(request);
    }

    @GetMapping("/welcome")
    public ChatResponse welcome() {
        ChatResponse response = ChatResponse.success(
            "👋 Bonjour! Je suis votre assistant Smart City.\n\n" +
            "Je peux vous aider à:\n" +
            "• 📋 Planifier votre journée\n" +
            "• 🚌 Trouver les meilleurs transports\n" +
            "• 🌫️ Vérifier la qualité de l'air\n" +
            "• 📅 Découvrir les événements\n\n" +
            "Exemple: \"Planifie-moi une journée sportive au Parc Central\""
        );
        return response;
    }
}