package com.smartcity.orchestration.service;

import com.smartcity.orchestration.model.EventInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EventsClient {
    
    private static final Logger log = LoggerFactory.getLogger(EventsClient.class);
    
    private final WebClient webClient;
    private final String eventsUrl;
    
    // Type reference pour éviter les problèmes de generics
    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE = 
            new ParameterizedTypeReference<Map<String, Object>>() {};
    
    public EventsClient(WebClient webClient, 
                       @Value("${services.events.url}") String eventsUrl) {
        this.webClient = webClient;
        this.eventsUrl = eventsUrl;
    }
    
    public Mono<List<EventInfo>> getUpcomingEvents() {
        String query = """
            {
                "query": "{ getUpcomingEvents { id title description location startDateTime endDateTime eventType category capacity availableSpots isFree price organizer tags } }"
            }
            """;
        
        log.info("📡 Appel GraphQL Events Service");
        
        return webClient.post()
                .uri(eventsUrl + "/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .retrieve()
                .bodyToMono(MAP_TYPE)
                .map(response -> parseEventsFromData(response, "getUpcomingEvents"))
                .doOnSuccess(list -> log.info("✅ {} événements reçus", list.size()))
                .doOnError(e -> log.error("❌ Erreur Events: {}", e.getMessage()))
                .onErrorReturn(getDefaultEvents());
    }
    
    public Mono<List<EventInfo>> searchEvents(String keyword) {
        String query = String.format("""
            {
                "query": "{ searchEvents(keyword: \\"%s\\") { id title description location startDateTime eventType category isFree availableSpots } }"
            }
            """, keyword);
        
        return webClient.post()
                .uri(eventsUrl + "/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .retrieve()
                .bodyToMono(MAP_TYPE)
                .map(response -> parseEventsFromData(response, "searchEvents"))
                .doOnError(e -> log.error("❌ Erreur search: {}", e.getMessage()))
                .onErrorReturn(new ArrayList<>());
    }
    
    public Mono<List<EventInfo>> filterEvents(String type, String category, Boolean freeOnly) {
        StringBuilder args = new StringBuilder();
        if (type != null) args.append(String.format("type: \\\"%s\\\" ", type));
        if (category != null) args.append(String.format("category: \\\"%s\\\" ", category));
        if (freeOnly != null) args.append(String.format("freeOnly: %s", freeOnly));
        
        String query = String.format("""
            {
                "query": "{ filterEvents(%s) { id title location startDateTime eventType category isFree availableSpots } }"
            }
            """, args.toString().trim());
        
        return webClient.post()
                .uri(eventsUrl + "/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(query)
                .retrieve()
                .bodyToMono(MAP_TYPE)
                .map(response -> parseEventsFromData(response, "filterEvents"))
                .onErrorReturn(new ArrayList<>());
    }
    
    @SuppressWarnings("unchecked")
    private List<EventInfo> parseEventsFromData(Map<String, Object> response, String queryName) {
        List<EventInfo> events = new ArrayList<>();
        try {
            if (response == null) {
                return events;
            }
            
            Object dataObj = response.get("data");
            if (dataObj instanceof Map) {
                Map<String, Object> data = (Map<String, Object>) dataObj;
                Object eventsObj = data.get(queryName);
                
                if (eventsObj instanceof List) {
                    List<Map<String, Object>> eventsList = (List<Map<String, Object>>) eventsObj;
                    for (Map<String, Object> item : eventsList) {
                        events.add(mapToEventInfo(item));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erreur parsing events: {}", e.getMessage());
        }
        return events;
    }
    
    private EventInfo mapToEventInfo(Map<String, Object> item) {
        EventInfo event = new EventInfo();
        event.setId(getLong(item, "id"));
        event.setTitle(getString(item, "title"));
        event.setDescription(getString(item, "description"));
        event.setLocation(getString(item, "location"));
        event.setEventType(getString(item, "eventType"));
        event.setCategory(getString(item, "category"));
        event.setIsFree(getBoolean(item, "isFree"));
        event.setAvailableSpots(getInt(item, "availableSpots"));
        event.setOrganizer(getString(item, "organizer"));
        
        String startDateTime = getString(item, "startDateTime");
        if (startDateTime != null && !startDateTime.isEmpty()) {
            event.setStartDateTime(parseDateTime(startDateTime));
        }
        
        String endDateTime = getString(item, "endDateTime");
        if (endDateTime != null && !endDateTime.isEmpty()) {
            event.setEndDateTime(parseDateTime(endDateTime));
        }
        
        return event;
    }
    
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null) return null;
        
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e1) {
            try {
                return LocalDateTime.parse(dateTimeStr);
            } catch (Exception e2) {
                try {
                    // Format sans secondes
                    return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                } catch (Exception e3) {
                    log.debug("Impossible de parser la date: {}", dateTimeStr);
                    return null;
                }
            }
        }
    }
    
    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }
    
    private Long getLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    private Integer getInt(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    private Boolean getBoolean(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return null;
    }
    
    private List<EventInfo> getDefaultEvents() {
        List<EventInfo> events = new ArrayList<>();
        
        EventInfo event1 = new EventInfo();
        event1.setId(1L);
        event1.setTitle("Festival de Musique");
        event1.setDescription("Grand festival de musique en plein air");
        event1.setLocation("Parc Central");
        event1.setEventType("CONCERT");
        event1.setCategory("MUSIC");
        event1.setIsFree(true);
        event1.setAvailableSpots(500);
        event1.setStartDateTime(LocalDateTime.now().plusHours(2));
        events.add(event1);
        
        EventInfo event2 = new EventInfo();
        event2.setId(2L);
        event2.setTitle("Exposition d'Art Moderne");
        event2.setDescription("Découvrez les œuvres d'artistes contemporains");
        event2.setLocation("Musée d'Art");
        event2.setEventType("EXHIBITION");
        event2.setCategory("ART");
        event2.setIsFree(false);
        event2.setAvailableSpots(100);
        event2.setStartDateTime(LocalDateTime.now().plusHours(4));
        events.add(event2);
        
        return events;
    }
}