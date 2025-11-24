package com.smartcity.emergency.controller;

import com.smartcity.emergency.model.EmergencyAlert;
import com.smartcity.emergency.model.ResourceAssignment;
import com.smartcity.emergency.service.EmergencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergencies")
@CrossOrigin(origins = "*")
public class EmergencyRestController {

    @Autowired
    private EmergencyService emergencyService;

    @PostMapping
    public ResponseEntity<?> createEmergency(@RequestBody CreateEmergencyRequest request) {
        try {
            EmergencyAlert alert = emergencyService.createEmergencyAlert(
                request.getReporterId(),
                EmergencyAlert.EmergencyType.valueOf(request.getEmergencyType()),
                EmergencyAlert.SeverityLevel.valueOf(request.getSeverityLevel()),
                request.getLocation(),
                request.getLatitude(),
                request.getLongitude(),
                request.getDescription(),
                request.getAffectedPeople(),
                request.getTags()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("emergencyId", alert.getEmergencyId());
            response.put("status", "created");
            response.put("message", "Emergency alert created successfully");
            response.put("alert", alert);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to create emergency: " + e.getMessage())
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> getActiveEmergencies() {
        try {
            List<EmergencyAlert> emergencies = emergencyService.getActiveEmergencies();
            return ResponseEntity.ok(emergencies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to get emergencies: " + e.getMessage())
            );
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(defaultValue = "24") int hoursBack) {
        try {
            Map<String, Object> stats = emergencyService.getEmergencyStats(hoursBack, null);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to get stats: " + e.getMessage())
            );
        }
    }

    @GetMapping("/{emergencyId}")
    public ResponseEntity<?> getEmergency(@PathVariable String emergencyId) {
        try {
            var alert = emergencyService.getEmergencyAlert(emergencyId);
            if (alert.isPresent()) {
                return ResponseEntity.ok(alert.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to get emergency: " + e.getMessage())
            );
        }
    }

    @PutMapping("/{emergencyId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String emergencyId,
            @RequestBody UpdateStatusRequest request) {
        try {
            EmergencyAlert.EmergencyStatus status = 
                EmergencyAlert.EmergencyStatus.valueOf(request.getStatus());
            
            var result = emergencyService.updateEmergencyStatus(
                emergencyId, status, request.getResponderId(), request.getUpdateNotes()
            );

            if (result.isPresent()) {
                return ResponseEntity.ok(Map.of("status", "updated", "alert", result.get()));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to update status: " + e.getMessage())
            );
        }
    }

    @PostMapping("/{emergencyId}/resources")
    public ResponseEntity<?> assignResources(
            @PathVariable String emergencyId,
            @RequestBody List<ResourceAssignment> resources) {
        try {
            var result = emergencyService.assignResources(emergencyId, resources);
            if (result.isPresent()) {
                return ResponseEntity.ok(Map.of("status", "resources_assigned", "alert", result.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to assign resources: " + e.getMessage())
            );
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Emergency Service");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        response.put("gRPC", "active");
        response.put("REST", "active");
        return ResponseEntity.ok(response);
    }

    // Classes internes pour les requêtes
    public static class CreateEmergencyRequest {
        private String reporterId;
        private String emergencyType;
        private String severityLevel;
        private String location;
        private double latitude;
        private double longitude;
        private String description;
        private int affectedPeople;
        private List<String> tags;

        // Getters et Setters
        public String getReporterId() { return reporterId; }
        public void setReporterId(String reporterId) { this.reporterId = reporterId; }
        
        public String getEmergencyType() { return emergencyType; }
        public void setEmergencyType(String emergencyType) { this.emergencyType = emergencyType; }
        
        public String getSeverityLevel() { return severityLevel; }
        public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public double getLatitude() { return latitude; }
        public void setLatitude(double latitude) { this.latitude = latitude; }
        
        public double getLongitude() { return longitude; }
        public void setLongitude(double longitude) { this.longitude = longitude; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public int getAffectedPeople() { return affectedPeople; }
        public void setAffectedPeople(int affectedPeople) { this.affectedPeople = affectedPeople; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
    }

    public static class UpdateStatusRequest {
        private String status;
        private String responderId;
        private String updateNotes;

        // Getters et Setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getResponderId() { return responderId; }
        public void setResponderId(String responderId) { this.responderId = responderId; }
        
        public String getUpdateNotes() { return updateNotes; }
        public void setUpdateNotes(String updateNotes) { this.updateNotes = updateNotes; }
    }
}