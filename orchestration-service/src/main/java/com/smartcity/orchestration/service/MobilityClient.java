package com.smartcity.orchestration.service;

import com.smartcity.orchestration.model.TransportOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MobilityClient {
    
    private static final Logger log = LoggerFactory.getLogger(MobilityClient.class);
    
    private final WebClient webClient;
    private final String mobilityUrl;
    
    public MobilityClient(WebClient webClient, 
                         @Value("${services.mobility.url}") String mobilityUrl) {
        this.webClient = webClient;
        this.mobilityUrl = mobilityUrl;
    }
    
    public Mono<List<TransportOption>> getTransportLines() {
        log.info("📡 Appel REST Mobility Service");
        
        return webClient.get()
                .uri(mobilityUrl + "/api/transport-lines")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .map(this::mapToTransportOptions)
                .doOnSuccess(list -> log.info("✅ {} lignes de transport reçues", list.size()))
                .doOnError(e -> log.error("❌ Erreur Mobility: {}", e.getMessage()))
                .onErrorReturn(getDefaultTransportOptions());
    }
    
    public Mono<List<TransportOption>> getTransportByType(String type) {
        log.info("📡 Recherche transport type: {}", type);
        
        return webClient.get()
                .uri(mobilityUrl + "/api/transport-lines/type/" + type)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .map(this::mapToTransportOptions)
                .doOnError(e -> log.error("❌ Erreur: {}", e.getMessage()))
                .onErrorReturn(List.of());
    }
    
    public Mono<List<Map<String, Object>>> getTrafficInfo() {
        return webClient.get()
                .uri(mobilityUrl + "/api/traffic-info/active")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .doOnError(e -> log.error("❌ Erreur traffic: {}", e.getMessage()))
                .onErrorReturn(List.of());
    }
    
    private List<TransportOption> mapToTransportOptions(List<Map<String, Object>> data) {
        List<TransportOption> options = new ArrayList<>();
        for (Map<String, Object> item : data) {
            TransportOption option = new TransportOption();
            option.setId(getLong(item, "id"));
            option.setType((String) item.get("type"));
            option.setLineNumber((String) item.get("lineNumber"));
            option.setLineName((String) item.get("name"));
            option.setStartStation((String) item.get("startStation"));
            option.setEndStation((String) item.get("endStation"));
            option.setStatus((String) item.getOrDefault("status", "ACTIVE"));
            option.setFrequency((String) item.get("frequency"));
            options.add(option);
        }
        return options;
    }
    
    private Long getLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }
    
    private List<TransportOption> getDefaultTransportOptions() {
        return List.of(
            new TransportOption("BUS", "72", "Toutes les 10min", 15, "ACTIVE"),
            new TransportOption("METRO", "B", "Toutes les 5min", 8, "ACTIVE"),
            new TransportOption("TRAM", "T3", "Toutes les 7min", 12, "ACTIVE")
        );
    }
}