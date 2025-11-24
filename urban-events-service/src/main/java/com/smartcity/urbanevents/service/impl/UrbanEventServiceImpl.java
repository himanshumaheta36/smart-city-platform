package com.smartcity.urbanevents.service.impl;

import com.smartcity.urbanevents.model.*;
import com.smartcity.urbanevents.repository.UrbanEventRepository;
import com.smartcity.urbanevents.service.UrbanEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UrbanEventServiceImpl implements UrbanEventService {
    
    @Autowired
    private UrbanEventRepository eventRepository;
    
    @Override
    public UrbanEvent createEvent(UrbanEvent event) {
        validateEventDates(event);
        return eventRepository.save(event);
    }
    
    @Override
    public UrbanEvent updateEvent(Long id, UrbanEvent eventDetails) {
        UrbanEvent event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        validateEventDates(eventDetails);
        
        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setEventType(eventDetails.getEventType());
        event.setCategory(eventDetails.getCategory());
        event.setLocation(eventDetails.getLocation());
        event.setLatitude(eventDetails.getLatitude());
        event.setLongitude(eventDetails.getLongitude());
        event.setStartDateTime(eventDetails.getStartDateTime());
        event.setEndDateTime(eventDetails.getEndDateTime());
        event.setCapacity(eventDetails.getCapacity());
        event.setPrice(eventDetails.getPrice());
        event.setOrganizer(eventDetails.getOrganizer());
        event.setImageUrl(eventDetails.getImageUrl());
        event.setTags(eventDetails.getTags());
        
        return eventRepository.save(event);
    }
    
    @Override
    public void deleteEvent(Long id) {
        UrbanEvent event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        eventRepository.delete(event);
    }
    
    @Override
    public UrbanEvent getEventById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }
    
    @Override
    public List<UrbanEvent> getAllEvents() {
        return eventRepository.findAll();
    }
    
    @Override
    public List<UrbanEvent> getEventsByType(EventType type) {
        return eventRepository.findByEventType(type);
    }
    
    @Override
    public List<UrbanEvent> getEventsByCategory(EventCategory category) {
        return eventRepository.findByCategory(category);
    }
    
    @Override
    public List<UrbanEvent> getEventsByStatus(EventStatus status) {
        return eventRepository.findByStatus(status);
    }
    
    @Override
    public List<UrbanEvent> getUpcomingEvents() {
        return eventRepository.findByStartDateTimeAfter(LocalDateTime.now());
    }
    
    @Override
    public List<UrbanEvent> getOngoingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
            .filter(event -> event.isOngoing())
            .collect(Collectors.toList());
    }
    
    @Override
    public List<UrbanEvent> getEventsNearLocation(double latitude, double longitude, double radiusKm) {
        return eventRepository.findEventsNearLocation(latitude, longitude, radiusKm);
    }
    
    @Override
    public List<UrbanEvent> searchEvents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllEvents();
        }
        
        String searchTerm = keyword.toLowerCase();
        return eventRepository.findAll().stream()
            .filter(event -> 
                event.getTitle().toLowerCase().contains(searchTerm) ||
                event.getDescription().toLowerCase().contains(searchTerm) ||
                event.getLocation().toLowerCase().contains(searchTerm) ||
                event.getOrganizer().toLowerCase().contains(searchTerm) ||
                event.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(searchTerm))
            )
            .collect(Collectors.toList());
    }
    
    @Override
    public List<UrbanEvent> filterEvents(EventType type, EventCategory category, 
                                       EventStatus status, String location, Boolean freeOnly) {
        return eventRepository.findWithFilters(type, category, status, location, freeOnly);
    }
    
    @Override
    public UrbanEvent registerAttendee(Long eventId) {
        UrbanEvent event = getEventById(eventId);
        
        if (event.isFull()) {
            throw new RuntimeException("Event is already full");
        }
        
        if (!event.isUpcoming()) {
            throw new RuntimeException("Cannot register for past or ongoing event");
        }
        
        event.setRegisteredAttendees(event.getRegisteredAttendees() + 1);
        return eventRepository.save(event);
    }
    
    @Override
    public UrbanEvent cancelRegistration(Long eventId) {
        UrbanEvent event = getEventById(eventId);
        
        if (event.getRegisteredAttendees() > 0) {
            event.setRegisteredAttendees(event.getRegisteredAttendees() - 1);
        }
        
        return eventRepository.save(event);
    }
    
    @Override
    public List<UrbanEvent> getEventsInDateRange(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findEventsInDateRange(start, end);
    }
    
    @Override
    public Map<String, Object> getEventStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalEvents", eventRepository.count());
        stats.put("scheduledEvents", eventRepository.countByStatus(EventStatus.SCHEDULED));
        stats.put("ongoingEvents", eventRepository.countByStatus(EventStatus.ONGOING));
        stats.put("completedEvents", eventRepository.countByStatus(EventStatus.COMPLETED));
        
        // Count by type
        Map<String, Long> eventsByType = Arrays.stream(EventType.values())
            .collect(Collectors.toMap(
                Enum::name,
                type -> eventRepository.findByEventType(type).stream().count()
            ));
        stats.put("eventsByType", eventsByType);
        
        // Count by category
        Map<String, Long> eventsByCategory = Arrays.stream(EventCategory.values())
            .collect(Collectors.toMap(
                Enum::name,
                category -> eventRepository.findByCategory(category).stream().count()
            ));
        stats.put("eventsByCategory", eventsByCategory);
        
        // Popular tags
        List<String> popularTags = eventRepository.findAll().stream()
            .flatMap(event -> event.getTags().stream())
            .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        stats.put("popularTags", popularTags);
        
        return stats;
    }
    
    @Override
    public List<UrbanEvent> getEventsByOrganizer(String organizer) {
        return eventRepository.findByOrganizerContainingIgnoreCase(organizer);
    }
    
    @Override
    public List<UrbanEvent> getEventsWithTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return getAllEvents();
        }
        
        return tags.stream()
            .flatMap(tag -> eventRepository.findByTagsContaining(tag).stream())
            .distinct()
            .collect(Collectors.toList());
    }
    
    private void validateEventDates(UrbanEvent event) {
        if (event.getStartDateTime().isAfter(event.getEndDateTime())) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        
        if (event.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Start date cannot be in the past");
        }
    }
}