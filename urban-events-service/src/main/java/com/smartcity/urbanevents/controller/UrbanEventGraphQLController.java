package com.smartcity.urbanevents.controller;

import com.smartcity.urbanevents.model.*;
import com.smartcity.urbanevents.service.UrbanEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class UrbanEventGraphQLController {
    
    @Autowired
    private UrbanEventService eventService;
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    // Query Mappings
    @QueryMapping
    public List<UrbanEvent> getAllEvents() {
        return eventService.getAllEvents();
    }
    
    @QueryMapping
    public UrbanEvent getEventById(@Argument Long id) {
        return eventService.getEventById(id);
    }
    
    @QueryMapping
    public List<UrbanEvent> filterEvents(
        @Argument EventType type,
        @Argument EventCategory category,
        @Argument EventStatus status,
        @Argument String location,
        @Argument Boolean freeOnly) {
        
        return eventService.filterEvents(type, category, status, location, freeOnly);
    }
    
    @QueryMapping
    public List<UrbanEvent> searchEvents(@Argument String keyword) {
        return eventService.searchEvents(keyword);
    }
    
    @QueryMapping
    public List<UrbanEvent> getUpcomingEvents() {
        return eventService.getUpcomingEvents();
    }
    
    @QueryMapping
    public List<UrbanEvent> getOngoingEvents() {
        return eventService.getOngoingEvents();
    }
    
    @QueryMapping
    public List<UrbanEvent> getEventsNearLocation(
        @Argument double latitude,
        @Argument double longitude,
        @Argument double radiusKm) {
        
        return eventService.getEventsNearLocation(latitude, longitude, radiusKm);
    }
    
    @QueryMapping
    public List<UrbanEvent> getEventsByOrganizer(@Argument String organizer) {
        return eventService.getEventsByOrganizer(organizer);
    }
    
    @QueryMapping
    public List<UrbanEvent> getEventsWithTags(@Argument List<String> tags) {
        return eventService.getEventsWithTags(tags);
    }
    
    @QueryMapping
    public List<UrbanEvent> getEventsInDateRange(
        @Argument String startDate,
        @Argument String endDate) {
        
        LocalDateTime start = LocalDateTime.parse(startDate, DATE_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endDate, DATE_FORMATTER);
        return eventService.getEventsInDateRange(start, end);
    }
    
    @QueryMapping
    public Map<String, Object> getEventStatistics() {
        return eventService.getEventStatistics();
    }
    
    // Mutation Mappings
    @MutationMapping
    public UrbanEvent createEvent(@Argument("input") EventInput input) {
        UrbanEvent event = new UrbanEvent();
        event.setTitle(input.getTitle());
        event.setDescription(input.getDescription());
        event.setEventType(input.getEventType());
        event.setCategory(input.getCategory());
        event.setLocation(input.getLocation());
        event.setLatitude(input.getLatitude());
        event.setLongitude(input.getLongitude());
        event.setStartDateTime(LocalDateTime.parse(input.getStartDateTime(), DATE_FORMATTER));
        event.setEndDateTime(LocalDateTime.parse(input.getEndDateTime(), DATE_FORMATTER));
        event.setCapacity(input.getCapacity());
        event.setPrice(input.getPrice());
        event.setOrganizer(input.getOrganizer());
        event.setTags(input.getTags());
        event.setImageUrl(input.getImageUrl());
        
        return eventService.createEvent(event);
    }
    
    @MutationMapping
    public UrbanEvent updateEvent(@Argument Long id, @Argument("input") EventInput input) {
        UrbanEvent event = new UrbanEvent();
        event.setTitle(input.getTitle());
        event.setDescription(input.getDescription());
        event.setEventType(input.getEventType());
        event.setCategory(input.getCategory());
        event.setLocation(input.getLocation());
        event.setLatitude(input.getLatitude());
        event.setLongitude(input.getLongitude());
        event.setStartDateTime(LocalDateTime.parse(input.getStartDateTime(), DATE_FORMATTER));
        event.setEndDateTime(LocalDateTime.parse(input.getEndDateTime(), DATE_FORMATTER));
        event.setCapacity(input.getCapacity());
        event.setPrice(input.getPrice());
        event.setOrganizer(input.getOrganizer());
        event.setTags(input.getTags());
        event.setImageUrl(input.getImageUrl());
        
        return eventService.updateEvent(id, event);
    }
    
    @MutationMapping
    public Boolean deleteEvent(@Argument Long id) {
        eventService.deleteEvent(id);
        return true;
    }
    
    @MutationMapping
    public UrbanEvent registerAttendee(@Argument Long eventId) {
        return eventService.registerAttendee(eventId);
    }
    
    @MutationMapping
    public UrbanEvent cancelRegistration(@Argument Long eventId) {
        return eventService.cancelRegistration(eventId);
    }
    
    // Input class for GraphQL mutations
    public static class EventInput {
        private String title;
        private String description;
        private EventType eventType;
        private EventCategory category;
        private String location;
        private double latitude;
        private double longitude;
        private String startDateTime;
        private String endDateTime;
        private int capacity;
        private Double price;
        private String organizer;
        private List<String> tags;
        private String imageUrl;
        
        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public EventType getEventType() { return eventType; }
        public void setEventType(EventType eventType) { this.eventType = eventType; }
        
        public EventCategory getCategory() { return category; }
        public void setCategory(EventCategory category) { this.category = category; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public double getLatitude() { return latitude; }
        public void setLatitude(double latitude) { this.latitude = latitude; }
        
        public double getLongitude() { return longitude; }
        public void setLongitude(double longitude) { this.longitude = longitude; }
        
        public String getStartDateTime() { return startDateTime; }
        public void setStartDateTime(String startDateTime) { this.startDateTime = startDateTime; }
        
        public String getEndDateTime() { return endDateTime; }
        public void setEndDateTime(String endDateTime) { this.endDateTime = endDateTime; }
        
        public int getCapacity() { return capacity; }
        public void setCapacity(int capacity) { this.capacity = capacity; }
        
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        
        public String getOrganizer() { return organizer; }
        public void setOrganizer(String organizer) { this.organizer = organizer; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }
}