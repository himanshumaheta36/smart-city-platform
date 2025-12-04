package com.smartcity.orchestration.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DayPlan {
    private LocalDate date;
    private String userPreferences;
    private List<PlanActivity> activities;
    private AirQualityInfo airQuality;
    private List<TransportOption> transportOptions;
    private String aiSummary;
    private String aiRecommendations;
    private List<String> warnings;
    private LocalDateTime generatedAt;
    
    public DayPlan() {
        this.activities = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.generatedAt = LocalDateTime.now();
    }
    
    // Getters et Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getUserPreferences() { return userPreferences; }
    public void setUserPreferences(String userPreferences) { this.userPreferences = userPreferences; }
    
    public List<PlanActivity> getActivities() { return activities; }
    public void setActivities(List<PlanActivity> activities) { this.activities = activities; }
    
    public AirQualityInfo getAirQuality() { return airQuality; }
    public void setAirQuality(AirQualityInfo airQuality) { this.airQuality = airQuality; }
    
    public List<TransportOption> getTransportOptions() { return transportOptions; }
    public void setTransportOptions(List<TransportOption> transportOptions) { this.transportOptions = transportOptions; }
    
    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }
    
    public String getAiRecommendations() { return aiRecommendations; }
    public void setAiRecommendations(String aiRecommendations) { this.aiRecommendations = aiRecommendations; }
    
    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    
    public void addActivity(PlanActivity activity) {
        this.activities.add(activity);
    }
    
    public void addWarning(String warning) {
        this.warnings.add(warning);
    }
    
    // Classe interne pour les activités
    public static class PlanActivity {
        private String time;
        private String title;
        private String description;
        private String location;
        private String type;
        private TransportOption transport;
        private EventInfo relatedEvent;
        
        public PlanActivity() {}
        
        public PlanActivity(String time, String title, String description, String location, String type) {
            this.time = time;
            this.title = title;
            this.description = description;
            this.location = location;
            this.type = type;
        }
        
        // Getters et Setters
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public TransportOption getTransport() { return transport; }
        public void setTransport(TransportOption transport) { this.transport = transport; }
        
        public EventInfo getRelatedEvent() { return relatedEvent; }
        public void setRelatedEvent(EventInfo relatedEvent) { this.relatedEvent = relatedEvent; }
    }
}