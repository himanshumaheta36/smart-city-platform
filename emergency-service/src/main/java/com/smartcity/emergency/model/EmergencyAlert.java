package com.smartcity.emergency.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "emergency_alerts")
public class EmergencyAlert {
    
    @Id
    private String emergencyId;
    
    @NotBlank
    @Column(nullable = false)
    private String reporterId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmergencyType emergencyType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeverityLevel severityLevel;
    
    @NotBlank
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private double latitude;
    
    @Column(nullable = false)
    private double longitude;
    
    @NotBlank
    @Column(nullable = false, length = 1000)
    private String description;
    
    @Min(0)
    @Column(nullable = false)
    private int affectedPeople;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmergencyStatus status;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    private String responderId;
    
    @ElementCollection
    @CollectionTable(name = "emergency_tags", joinColumns = @JoinColumn(name = "emergency_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_id")
    private List<ResourceAssignment> assignedResources = new ArrayList<>();
    
    @Column(length = 2000)
    private String updateNotes;
    
    // Constructors
    public EmergencyAlert() {
        this.emergencyId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = EmergencyStatus.REPORTED;
    }
    
    public EmergencyAlert(String reporterId, EmergencyType emergencyType, SeverityLevel severityLevel,
                        String location, double latitude, double longitude, String description, 
                        int affectedPeople, List<String> tags) {
        this();
        this.reporterId = reporterId;
        this.emergencyType = emergencyType;
        this.severityLevel = severityLevel;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.affectedPeople = affectedPeople;
        this.tags = tags != null ? tags : new ArrayList<>();
    }
    
    // Enums
    public enum EmergencyType {
        UNKNOWN, ACCIDENT, FIRE, MEDICAL, SECURITY, NATURAL_DISASTER, TECHNICAL
    }
    
    public enum SeverityLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    public enum EmergencyStatus {
        REPORTED, CONFIRMED, IN_PROGRESS, RESOLVED, CANCELLED
    }
    
    // Getters and Setters
    public String getEmergencyId() { return emergencyId; }
    public void setEmergencyId(String emergencyId) { this.emergencyId = emergencyId; }
    
    public String getReporterId() { return reporterId; }
    public void setReporterId(String reporterId) { this.reporterId = reporterId; }
    
    public EmergencyType getEmergencyType() { return emergencyType; }
    public void setEmergencyType(EmergencyType emergencyType) { this.emergencyType = emergencyType; }
    
    public SeverityLevel getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(SeverityLevel severityLevel) { this.severityLevel = severityLevel; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getAffectedPeople() { return affectedPeople; }
    public void setAffectedPeople(int affectedPeople) { this.affectedPeople = affectedPeople; }
    
    public EmergencyStatus getStatus() { return status; }
    public void setStatus(EmergencyStatus status) { 
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getResponderId() { return responderId; }
    public void setResponderId(String responderId) { this.responderId = responderId; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public List<ResourceAssignment> getAssignedResources() { return assignedResources; }
    public void setAssignedResources(List<ResourceAssignment> assignedResources) { 
        this.assignedResources = assignedResources;
    }
    
    public String getUpdateNotes() { return updateNotes; }
    public void setUpdateNotes(String updateNotes) { this.updateNotes = updateNotes; }
    
    // Helper methods
    public void addResourceAssignment(ResourceAssignment resource) {
        this.assignedResources.add(resource);
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isActive() {
        return status == EmergencyStatus.REPORTED || 
               status == EmergencyStatus.CONFIRMED || 
               status == EmergencyStatus.IN_PROGRESS;
    }
}