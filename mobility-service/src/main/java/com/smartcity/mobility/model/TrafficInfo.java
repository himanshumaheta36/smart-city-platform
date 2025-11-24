package com.smartcity.mobility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "traffic_info")
public class TrafficInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String location;
    
    @NotBlank
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    
    @NotBlank
    private String description;
    
    @NotNull
    private LocalDateTime reportedAt;
    
    private LocalDateTime resolvedAt;
    
    private String incidentType; // ACCIDENT, CONSTRUCTION, WEATHER, CONGESTION
    
    // Constructors
    public TrafficInfo() {}
    
    public TrafficInfo(String location, String severity, String description, String incidentType) {
        this.location = location;
        this.severity = severity;
        this.description = description;
        this.incidentType = incidentType;
        this.reportedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
    
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    
    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }
}