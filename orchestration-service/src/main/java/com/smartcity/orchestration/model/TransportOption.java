package com.smartcity.orchestration.model;

public class TransportOption {
    private Long id;
    private String type;
    private String lineNumber;
    private String lineName;
    private String startStation;
    private String endStation;
    private String schedule;
    private Integer duration;
    private String status;
    private String frequency;
    
    // Constructeurs
    public TransportOption() {}
    
    public TransportOption(String type, String lineNumber, String schedule, int duration, String status) {
        this.type = type;
        this.lineNumber = lineNumber;
        this.schedule = schedule;
        this.duration = duration;
        this.status = status;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getLineNumber() { return lineNumber; }
    public void setLineNumber(String lineNumber) { this.lineNumber = lineNumber; }
    
    public String getLineName() { return lineName; }
    public void setLineName(String lineName) { this.lineName = lineName; }
    
    public String getStartStation() { return startStation; }
    public void setStartStation(String startStation) { this.startStation = startStation; }
    
    public String getEndStation() { return endStation; }
    public void setEndStation(String endStation) { this.endStation = endStation; }
    
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
}