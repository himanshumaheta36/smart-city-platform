package com.smartcity.emergency.service.impl;

import com.smartcity.emergency.model.EmergencyAlert;
import com.smartcity.emergency.model.ResourceAssignment;
import com.smartcity.emergency.repository.EmergencyAlertRepository;
import com.smartcity.emergency.service.EmergencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmergencyServiceImpl implements EmergencyService {
    
    @Autowired
    private EmergencyAlertRepository emergencyAlertRepository;
    
    @Override
    public EmergencyAlert createEmergencyAlert(String reporterId, 
                                            EmergencyAlert.EmergencyType emergencyType,
                                            EmergencyAlert.SeverityLevel severityLevel,
                                            String location, double latitude, double longitude,
                                            String description, int affectedPeople, 
                                            List<String> tags) {
        
        EmergencyAlert alert = new EmergencyAlert(reporterId, emergencyType, severityLevel,
                                                location, latitude, longitude, description,
                                                affectedPeople, tags);
        
        return emergencyAlertRepository.save(alert);
    }
    
    @Override
    public Optional<EmergencyAlert> getEmergencyAlert(String emergencyId) {
        return emergencyAlertRepository.findById(emergencyId);
    }
    
    @Override
    public Optional<EmergencyAlert> updateEmergencyStatus(String emergencyId,
                                                        EmergencyAlert.EmergencyStatus status,
                                                        String responderId, String updateNotes) {
        
        Optional<EmergencyAlert> optionalAlert = emergencyAlertRepository.findById(emergencyId);
        
        if (optionalAlert.isPresent()) {
            EmergencyAlert alert = optionalAlert.get();
            alert.setStatus(status);
            alert.setResponderId(responderId);
            alert.setUpdateNotes(updateNotes);
            
            return Optional.of(emergencyAlertRepository.save(alert));
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<EmergencyAlert> getActiveEmergencies() {
        List<EmergencyAlert.EmergencyStatus> activeStatuses = Arrays.asList(
            EmergencyAlert.EmergencyStatus.REPORTED,
            EmergencyAlert.EmergencyStatus.CONFIRMED,
            EmergencyAlert.EmergencyStatus.IN_PROGRESS
        );
        
        return emergencyAlertRepository.findByStatusIn(activeStatuses);
    }
    
    @Override
    public List<EmergencyAlert> filterEmergencies(EmergencyAlert.EmergencyType type,
                                                EmergencyAlert.SeverityLevel severity,
                                                EmergencyAlert.EmergencyStatus status,
                                                String location) {
        
        return emergencyAlertRepository.findWithFilters(type, severity, status, location);
    }
    
    @Override
    public Optional<EmergencyAlert> assignResources(String emergencyId, 
                                                  List<ResourceAssignment> resources) {
        
        Optional<EmergencyAlert> optionalAlert = emergencyAlertRepository.findById(emergencyId);
        
        if (optionalAlert.isPresent()) {
            EmergencyAlert alert = optionalAlert.get();
            
            for (ResourceAssignment resource : resources) {
                alert.addResourceAssignment(resource);
            }
            
            return Optional.of(emergencyAlertRepository.save(alert));
        }
        
        return Optional.empty();
    }
    
    @Override
    public Map<String, Object> getEmergencyStats(int hoursBack, String locationFilter) {
        LocalDateTime since = LocalDateTime.now().minusHours(hoursBack);
        
        List<EmergencyAlert> emergencies = emergencyAlertRepository.findSince(since);
        
        if (locationFilter != null && !locationFilter.isEmpty()) {
            emergencies = emergencies.stream()
                .filter(e -> e.getLocation().toLowerCase().contains(locationFilter.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        Map<String, Object> stats = new HashMap<>();
        
        // Basic counts
        stats.put("totalEmergencies", emergencies.size());
        
        long activeEmergencies = emergencies.stream()
            .filter(EmergencyAlert::isActive)
            .count();
        stats.put("activeEmergencies", activeEmergencies);
        
        // Count by type
        Map<String, Long> byType = emergencies.stream()
            .collect(Collectors.groupingBy(
                e -> e.getEmergencyType().name(),
                Collectors.counting()
            ));
        stats.put("emergenciesByType", byType);
        
        // Count by severity
        Map<String, Long> bySeverity = emergencies.stream()
            .collect(Collectors.groupingBy(
                e -> e.getSeverityLevel().name(),
                Collectors.counting()
            ));
        stats.put("emergenciesBySeverity", bySeverity);
        
        // Count by status
        Map<String, Long> byStatus = emergencies.stream()
            .collect(Collectors.groupingBy(
                e -> e.getStatus().name(),
                Collectors.counting()
            ));
        stats.put("emergenciesByStatus", byStatus);
        
        // Average response time (simplified)
        double avgResponseTime = emergencies.stream()
            .filter(e -> e.getStatus() == EmergencyAlert.EmergencyStatus.RESOLVED)
            .mapToLong(e -> java.time.Duration.between(e.getCreatedAt(), e.getUpdatedAt()).toMinutes())
            .average()
            .orElse(0.0);
        stats.put("averageResponseTimeMinutes", avgResponseTime);
        
        // Hotspots
        List<Object[]> hotspotData = emergencyAlertRepository.findHotspots(since);
        List<String> hotspots = hotspotData.stream()
            .limit(5)
            .map(obj -> obj[0] + " (" + obj[1] + " incidents)")
            .collect(Collectors.toList());
        stats.put("hotspots", hotspots);
        
        stats.put("periodCovered", "Last " + hoursBack + " hours");
        
        return stats;
    }
}