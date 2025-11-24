package com.smartcity.urbanevents.service;

import com.smartcity.urbanevents.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface UrbanEventService {
    
    UrbanEvent createEvent(UrbanEvent event);
    
    UrbanEvent updateEvent(Long id, UrbanEvent event);
    
    void deleteEvent(Long id);
    
    UrbanEvent getEventById(Long id);
    
    List<UrbanEvent> getAllEvents();
    
    List<UrbanEvent> getEventsByType(EventType type);
    
    List<UrbanEvent> getEventsByCategory(EventCategory category);
    
    List<UrbanEvent> getEventsByStatus(EventStatus status);
    
    List<UrbanEvent> getUpcomingEvents();
    
    List<UrbanEvent> getOngoingEvents();
    
    List<UrbanEvent> getEventsNearLocation(double latitude, double longitude, double radiusKm);
    
    List<UrbanEvent> searchEvents(String keyword);
    
    List<UrbanEvent> filterEvents(EventType type, EventCategory category, 
                                EventStatus status, String location, Boolean freeOnly);
    
    UrbanEvent registerAttendee(Long eventId);
    
    UrbanEvent cancelRegistration(Long eventId);
    
    List<UrbanEvent> getEventsInDateRange(LocalDateTime start, LocalDateTime end);
    
    Map<String, Object> getEventStatistics();
    
    List<UrbanEvent> getEventsByOrganizer(String organizer);
    
    List<UrbanEvent> getEventsWithTags(List<String> tags);
}