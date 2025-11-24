package com.smartcity.mobility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transport_lines")
public class TransportLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Line number is required")
    @Column(unique = true)
    private String lineNumber;
    
    @NotBlank(message = "Transport type is required")
    private String transportType; // BUS, METRO, TRAIN
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @ElementCollection
    private List<String> stations;
    
    @NotNull
    private String status; // ACTIVE, INACTIVE, DELAYED
    
    private Integer delayMinutes;
    
    private LocalDateTime lastUpdate;
    
    // Constructors
    public TransportLine() {}
    
    public TransportLine(String lineNumber, String transportType, String description, 
                        List<String> stations, String status) {
        this.lineNumber = lineNumber;
        this.transportType = transportType;
        this.description = description;
        this.stations = stations;
        this.status = status;
        this.lastUpdate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getLineNumber() { return lineNumber; }
    public void setLineNumber(String lineNumber) { this.lineNumber = lineNumber; }
    
    public String getTransportType() { return transportType; }
    public void setTransportType(String transportType) { this.transportType = transportType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<String> getStations() { return stations; }
    public void setStations(List<String> stations) { this.stations = stations; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        this.lastUpdate = LocalDateTime.now();
    }
    
    public Integer getDelayMinutes() { return delayMinutes; }
    public void setDelayMinutes(Integer delayMinutes) { this.delayMinutes = delayMinutes; }
    
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}