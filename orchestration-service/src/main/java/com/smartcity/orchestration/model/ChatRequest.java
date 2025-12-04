package com.smartcity.orchestration.model;

import java.util.List;

public class ChatRequest {
    private String message;
    private String sessionId;
    private List<String> preferences;
    private String location;
    
    // Getters et Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public List<String> getPreferences() { return preferences; }
    public void setPreferences(List<String> preferences) { this.preferences = preferences; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}