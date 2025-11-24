package com.smartcity.urbanevents.repository;

import com.smartcity.urbanevents.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UrbanEventRepository extends JpaRepository<UrbanEvent, Long> {
    
    List<UrbanEvent> findByEventType(EventType eventType);
    
    List<UrbanEvent> findByCategory(EventCategory category);
    
    List<UrbanEvent> findByStatus(EventStatus status);
    
    List<UrbanEvent> findByLocationContainingIgnoreCase(String location);
    
    List<UrbanEvent> findByOrganizerContainingIgnoreCase(String organizer);
    
    List<UrbanEvent> findByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<UrbanEvent> findByStartDateTimeAfter(LocalDateTime dateTime);
    
    List<UrbanEvent> findByEndDateTimeBefore(LocalDateTime dateTime);
    
    @Query("SELECT e FROM UrbanEvent e WHERE " +
           "(:type IS NULL OR e.eventType = :type) AND " +
           "(:category IS NULL OR e.category = :category) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:location IS NULL OR e.location LIKE %:location%) AND " +
           "(:freeOnly IS NULL OR (:freeOnly = true AND (e.category = 'FREE' OR e.price = 0)) OR :freeOnly = false)")
    List<UrbanEvent> findWithFilters(
        @Param("type") EventType type,
        @Param("category") EventCategory category,
        @Param("status") EventStatus status,
        @Param("location") String location,
        @Param("freeOnly") Boolean freeOnly
    );
    
    @Query("SELECT e FROM UrbanEvent e WHERE e.startDateTime >= :startDate AND e.startDateTime <= :endDate")
    List<UrbanEvent> findEventsInDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT e FROM UrbanEvent e WHERE " +
           "(6371 * acos(cos(radians(:lat)) * cos(radians(e.latitude)) * " +
           "cos(radians(e.longitude) - radians(:lng)) + sin(radians(:lat)) * " +
           "sin(radians(e.latitude)))) < :radius")
    List<UrbanEvent> findEventsNearLocation(@Param("lat") double latitude, 
                                          @Param("lng") double longitude, 
                                          @Param("radius") double radiusKm);
    
    List<UrbanEvent> findByTagsContaining(String tag);
    
    @Query("SELECT e FROM UrbanEvent e WHERE e.capacity > e.registeredAttendees AND e.status = 'SCHEDULED'")
    List<UrbanEvent> findAvailableEvents();
    
    long countByStatus(EventStatus status);
}