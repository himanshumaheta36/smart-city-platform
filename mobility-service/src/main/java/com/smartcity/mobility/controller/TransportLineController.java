package com.smartcity.mobility.controller;

import com.smartcity.mobility.model.TransportLine;
import com.smartcity.mobility.service.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transport-lines")
@Tag(name = "Transport Lines", description = "APIs for managing transport lines")
public class TransportLineController {
    
    @Autowired
    private TransportService transportService;
    
    @GetMapping
    @Operation(summary = "Get all transport lines")
    public ResponseEntity<List<TransportLine>> getAllTransportLines() {
        List<TransportLine> lines = transportService.getAllTransportLines();
        return ResponseEntity.ok(lines);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get transport line by ID")
    public ResponseEntity<TransportLine> getTransportLineById(@PathVariable Long id) {
        Optional<TransportLine> line = transportService.getTransportLineById(id);
        return line.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/number/{lineNumber}")
    @Operation(summary = "Get transport line by line number")
    public ResponseEntity<TransportLine> getTransportLineByNumber(@PathVariable String lineNumber) {
        Optional<TransportLine> line = transportService.getTransportLineByNumber(lineNumber);
        return line.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/type/{transportType}")
    @Operation(summary = "Get transport lines by type")
    public ResponseEntity<List<TransportLine>> getTransportLinesByType(@PathVariable String transportType) {
        List<TransportLine> lines = transportService.getTransportLinesByType(transportType);
        return ResponseEntity.ok(lines);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get transport lines by status")
    public ResponseEntity<List<TransportLine>> getTransportLinesByStatus(@PathVariable String status) {
        List<TransportLine> lines = transportService.getTransportLinesByStatus(status);
        return ResponseEntity.ok(lines);
    }
    
    @GetMapping("/station/{station}")
    @Operation(summary = "Get transport lines by station")
    public ResponseEntity<List<TransportLine>> getTransportLinesByStation(@PathVariable String station) {
        List<TransportLine> lines = transportService.getTransportLinesByStation(station);
        return ResponseEntity.ok(lines);
    }
    
    @PostMapping
    @Operation(summary = "Create a new transport line")
    public ResponseEntity<TransportLine> createTransportLine(@Valid @RequestBody TransportLine transportLine) {
        TransportLine createdLine = transportService.createTransportLine(transportLine);
        return ResponseEntity.ok(createdLine);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update transport line status")
    public ResponseEntity<TransportLine> updateTransportLineStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Integer delayMinutes) {
        
        TransportLine updatedLine = transportService.updateTransportLineStatus(id, status, delayMinutes);
        if (updatedLine != null) {
            return ResponseEntity.ok(updatedLine);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/delayed")
    @Operation(summary = "Get all delayed transport lines")
    public ResponseEntity<List<TransportLine>> getDelayedLines() {
        List<TransportLine> delayedLines = transportService.getDelayedLines();
        return ResponseEntity.ok(delayedLines);
    }
}