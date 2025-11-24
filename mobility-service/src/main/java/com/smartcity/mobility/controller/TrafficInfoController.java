package com.smartcity.mobility.controller;

import com.smartcity.mobility.model.TrafficInfo;
import com.smartcity.mobility.service.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traffic-info")
@Tag(name = "Traffic Information", description = "APIs for managing traffic incidents and information")
public class TrafficInfoController {
    
    @Autowired
    private TransportService transportService;
    
    @GetMapping("/active")
    @Operation(summary = "Get all active incidents")
    public ResponseEntity<List<TrafficInfo>> getActiveIncidents() {
        List<TrafficInfo> incidents = transportService.getAllActiveIncidents();
        return ResponseEntity.ok(incidents);
    }
    
    @GetMapping("/severity/{severity}")
    @Operation(summary = "Get incidents by severity")
    public ResponseEntity<List<TrafficInfo>> getIncidentsBySeverity(@PathVariable String severity) {
        List<TrafficInfo> incidents = transportService.getIncidentsBySeverity(severity);
        return ResponseEntity.ok(incidents);
    }
    
    @GetMapping("/location/{location}")
    @Operation(summary = "Get incidents by location")
    public ResponseEntity<List<TrafficInfo>> getIncidentsByLocation(@PathVariable String location) {
        List<TrafficInfo> incidents = transportService.getIncidentsByLocation(location);
        return ResponseEntity.ok(incidents);
    }
    
    @GetMapping("/type/{incidentType}")
    @Operation(summary = "Get incidents by type")
    public ResponseEntity<List<TrafficInfo>> getIncidentsByType(@PathVariable String incidentType) {
        List<TrafficInfo> incidents = transportService.getIncidentsByType(incidentType);
        return ResponseEntity.ok(incidents);
    }
    
    @PostMapping
    @Operation(summary = "Report a new incident")
    public ResponseEntity<TrafficInfo> reportIncident(@Valid @RequestBody TrafficInfo trafficInfo) {
        TrafficInfo reportedIncident = transportService.reportIncident(trafficInfo);
        return ResponseEntity.ok(reportedIncident);
    }
    
    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolve an incident")
    public ResponseEntity<TrafficInfo> resolveIncident(@PathVariable Long id) {
        TrafficInfo resolvedIncident = transportService.resolveIncident(id);
        if (resolvedIncident != null) {
            return ResponseEntity.ok(resolvedIncident);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/recent")
    @Operation(summary = "Get recent incidents")
    public ResponseEntity<List<TrafficInfo>> getRecentIncidents() {
        List<TrafficInfo> incidents = transportService.getRecentIncidents();
        return ResponseEntity.ok(incidents);
    }
}