package com.smartcity.urbanevents.resolver;

import com.smartcity.urbanevents.model.EventCategory;
import com.smartcity.urbanevents.model.EventType;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class EventStatisticsResolver {
    
    @SchemaMapping(typeName = "EventStatistics")
    public int totalEvents(Map<String, Object> statistics) {
        return (int) statistics.get("totalEvents");
    }
    
    @SchemaMapping(typeName = "EventStatistics")
    public int scheduledEvents(Map<String, Object> statistics) {
        return (int) statistics.get("scheduledEvents");
    }
    
    @SchemaMapping(typeName = "EventStatistics")
    public int ongoingEvents(Map<String, Object> statistics) {
        return (int) statistics.get("ongoingEvents");
    }
    
    @SchemaMapping(typeName = "EventStatistics")
    public int completedEvents(Map<String, Object> statistics) {
        return (int) statistics.get("completedEvents");
    }
    
    @SchemaMapping(typeName = "EventStatistics")
    public List<TypeCount> eventsByType(Map<String, Object> statistics) {
        Map<String, Long> eventsByType = (Map<String, Long>) statistics.get("eventsByType");
        return eventsByType.entrySet().stream()
            .map(entry -> new TypeCount(EventType.valueOf(entry.getKey()), entry.getValue().intValue()))
            .toList();
    }
    
    @SchemaMapping(typeName = "EventStatistics")
    public List<CategoryCount> eventsByCategory(Map<String, Object> statistics) {
        Map<String, Long> eventsByCategory = (Map<String, Long>) statistics.get("eventsByCategory");
        return eventsByCategory.entrySet().stream()
            .map(entry -> new CategoryCount(EventCategory.valueOf(entry.getKey()), entry.getValue().intValue()))
            .toList();
    }
    
    @SchemaMapping(typeName = "EventStatistics")
    public List<String> popularTags(Map<String, Object> statistics) {
        return (List<String>) statistics.get("popularTags");
    }
    
    public static class TypeCount {
        private final EventType type;
        private final int count;
        
        public TypeCount(EventType type, int count) {
            this.type = type;
            this.count = count;
        }
        
        public EventType getType() { return type; }
        public int getCount() { return count; }
    }
    
    public static class CategoryCount {
        private final EventCategory category;
        private final int count;
        
        public CategoryCount(EventCategory category, int count) {
            this.category = category;
            this.count = count;
        }
        
        public EventCategory getCategory() { return category; }
        public int getCount() { return count; }
    }
}