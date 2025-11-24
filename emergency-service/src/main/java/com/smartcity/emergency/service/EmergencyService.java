package com.smartcity.emergency.service;

import com.smartcity.emergency.model.EmergencyAlert;
import com.smartcity.emergency.model.ResourceAssignment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmergencyService {
    
    EmergencyAlert createEmergencyAlert(String reporterId, 
                                      EmergencyAlert.EmergencyType emergencyType,
                                      EmergencyAlert.SeverityLevel severityLevel,
                                      String location, double latitude, double longitude,
                                      String description, int affectedPeople, 
                                      List<String> tags);
    
    Optional<EmergencyAlert> getEmergencyAlert(String emergencyId);
    
    Optional<EmergencyAlert> updateEmergencyStatus(String emergencyId,
                                                 EmergencyAlert.EmergencyStatus status,
                                                 String responderId, String updateNotes);
    
    List<EmergencyAlert> getActiveEmergencies();
    
    List<EmergencyAlert> filterEmergencies(EmergencyAlert.EmergencyType type,
                                         EmergencyAlert.SeverityLevel severity,
                                         EmergencyAlert.EmergencyStatus status,
                                         String location);
    
    Optional<EmergencyAlert> assignResources(String emergencyId, 
                                           List<ResourceAssignment> resources);
    
    Map<String, Object> getEmergencyStats(int hoursBack, String locationFilter);
}