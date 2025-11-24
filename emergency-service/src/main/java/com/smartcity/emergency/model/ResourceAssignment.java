package com.smartcity.emergency.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "resource_assignments")
public class ResourceAssignment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType resourceType;
    
    @NotBlank
    @Column(nullable = false)
    private String resourceId;
    
    @Min(1)
    @Column(nullable = false)
    private int quantity;
    
    @Min(0)
    @Column(nullable = false)
    private int estimatedArrivalMinutes;
    
    // Constructors
    public ResourceAssignment() {}
    
    public ResourceAssignment(ResourceType resourceType, String resourceId, 
                            int quantity, int estimatedArrivalMinutes) {
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.quantity = quantity;
        this.estimatedArrivalMinutes = estimatedArrivalMinutes;
    }
    
    // Enum
    public enum ResourceType {
        AMBULANCE, FIRE_TRUCK, POLICE, RESCUE_TEAM, HELICOPTER, DOCTOR, NURSE
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public ResourceType getResourceType() { return resourceType; }
    public void setResourceType(ResourceType resourceType) { this.resourceType = resourceType; }
    
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public int getEstimatedArrivalMinutes() { return estimatedArrivalMinutes; }
    public void setEstimatedArrivalMinutes(int estimatedArrivalMinutes) { 
        this.estimatedArrivalMinutes = estimatedArrivalMinutes; 
    }
}