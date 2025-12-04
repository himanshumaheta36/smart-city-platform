package com.smartcity.orchestration.model;

import java.time.LocalDateTime;

public class ChatResponse {
    private String message;
    private String sessionId;
    private DayPlan suggestedPlan;
    private LocalDateTime timestamp;
    private boolean success;
    private String error;
    
    public ChatResponse() {
        this.timestamp = LocalDateTime.now();
        this.success = true;
    }
    
    public static ChatResponse success(String message) {
        ChatResponse response = new ChatResponse();
        response.setMessage(message);
        return response;
    }
    
    public static ChatResponse error(String error) {
        ChatResponse response = new ChatResponse();
        response.setSuccess(false);
        response.setError(error);
        return response;
    }
    
    // Getters et Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public DayPlan getSuggestedPlan() { return suggestedPlan; }
    public void setSuggestedPlan(DayPlan suggestedPlan) { this.suggestedPlan = suggestedPlan; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}