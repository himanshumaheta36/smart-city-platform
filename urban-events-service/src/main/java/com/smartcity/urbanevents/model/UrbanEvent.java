package com.smartcity.urbanevents.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "urban_events")
public class UrbanEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String title;
    
    @NotBlank
    @Column(nullable = false, length = 2000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventCategory category;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;
    
    @NotBlank
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private double latitude;
    
    @Column(nullable = false)
    private double longitude;
    
    @NotNull
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    
    @NotNull
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    
    @Min(0)
    @Column(nullable = false)
    private int capacity;
    
    @Min(0)
    @Column(nullable = false)
    private int registeredAttendees;
    
    @DecimalMin("0.0")
    @Column
    private Double price;
    
    @NotBlank
    @Column(nullable = false)
    private String organizer;
    
    @ElementCollection
    @CollectionTable(name = "event_tags", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();
    
    @Column(length = 1000)
    private String imageUrl;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public UrbanEvent() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.registeredAttendees = 0;
        this.status = EventStatus.SCHEDULED;
    }
    
    public UrbanEvent(String title, String description, EventType eventType, 
                     EventCategory category, String location, double latitude, 
                     double longitude, LocalDateTime startDateTime, 
                     LocalDateTime endDateTime, int capacity, String organizer) {
        this();
        this.title = title;
        this.description = description;
        this.eventType = eventType;
        this.category = category;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.capacity = capacity;
        this.organizer = organizer;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }
    
    public EventCategory getCategory() { return category; }
    public void setCategory(EventCategory category) { this.category = category; }
    
    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { 
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public int getRegisteredAttendees() { return registeredAttendees; }
    public void setRegisteredAttendees(int registeredAttendees) { 
        this.registeredAttendees = registeredAttendees;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Helper methods
    public void addTag(String tag) {
        this.tags.add(tag);
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isFull() {
        return registeredAttendees >= capacity;
    }
    
    public int getAvailableSpots() {
        return capacity - registeredAttendees;
    }
    
    public boolean isFree() {
        return category == EventCategory.FREE || (price != null && price == 0.0);
    }
    
    public boolean isUpcoming() {
        return startDateTime.isAfter(LocalDateTime.now()) && status == EventStatus.SCHEDULED;
    }
    
    public boolean isOngoing() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDateTime) && now.isBefore(endDateTime) && status == EventStatus.ONGOING;
    }
}