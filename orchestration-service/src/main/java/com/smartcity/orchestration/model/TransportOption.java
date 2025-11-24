package com.smartcity.orchestration.model;

public class TransportOption {
    private String type;
    private String line;
    private String schedule;
    private int duration;
    private String status;
    
    // Constructeurs
    public TransportOption() {}
    
    public TransportOption(String type, String line, String schedule, int duration, String status) {
        this.type = type;
        this.line = line;
        this.schedule = schedule;
        this.duration = duration;
        this.status = status;
    }
    
    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getLine() { return line; }
    public void setLine(String line) { this.line = line; }
    
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}