package com.smartcity.urbanevents.resolver;

import com.smartcity.urbanevents.model.UrbanEvent;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;

@Controller
public class UrbanEventResolver {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @SchemaMapping
    public int availableSpots(UrbanEvent event) {
        return event.getAvailableSpots();
    }
    
    @SchemaMapping
    public boolean isFree(UrbanEvent event) {
        return event.isFree();
    }
    
    @SchemaMapping
    public boolean isUpcoming(UrbanEvent event) {
        return event.isUpcoming();
    }
    
    @SchemaMapping
    public boolean isOngoing(UrbanEvent event) {
        return event.isOngoing();
    }
    
    @SchemaMapping
    public boolean isFull(UrbanEvent event) {
        return event.isFull();
    }
    
    @SchemaMapping
    public String createdAt(UrbanEvent event) {
        return event.getCreatedAt().format(DATE_FORMATTER);
    }
    
    @SchemaMapping
    public String updatedAt(UrbanEvent event) {
        return event.getUpdatedAt().format(DATE_FORMATTER);
    }
    
    @SchemaMapping
    public String startDateTime(UrbanEvent event) {
        return event.getStartDateTime().format(DATE_FORMATTER);
    }
    
    @SchemaMapping
    public String endDateTime(UrbanEvent event) {
        return event.getEndDateTime().format(DATE_FORMATTER);
    }
}