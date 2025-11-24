package com.smartcity.mobility.controller;

import com.smartcity.mobility.model.Schedule;
import com.smartcity.mobility.service.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Schedules", description = "APIs for managing transport schedules")
public class ScheduleController {
    
    @Autowired
    private TransportService transportService;
    
    @GetMapping("/line/{lineNumber}")
    @Operation(summary = "Get schedules by line number")
    public ResponseEntity<List<Schedule>> getSchedulesByLine(@PathVariable String lineNumber) {
        List<Schedule> schedules = transportService.getSchedulesByLine(lineNumber);
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/station/{station}")
    @Operation(summary = "Get schedules by station and day type")
    public ResponseEntity<List<Schedule>> getSchedulesByStation(
            @PathVariable String station,
            @RequestParam(defaultValue = "WEEKDAY") String dayType) {
        
        List<Schedule> schedules = transportService.getSchedulesByStationAndDayType(station, dayType);
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search schedules by line, station, and time range")
    public ResponseEntity<List<Schedule>> searchSchedules(
            @RequestParam String lineNumber,
            @RequestParam String station,
            @RequestParam(defaultValue = "WEEKDAY") String dayType,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        
        List<Schedule> schedules = transportService.getSchedulesByLineAndStationAndTime(
            lineNumber, station, dayType, start, end);
        
        return ResponseEntity.ok(schedules);
    }
    
    @PostMapping
    @Operation(summary = "Create a new schedule")
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody Schedule schedule) {
        Schedule createdSchedule = transportService.createSchedule(schedule);
        return ResponseEntity.ok(createdSchedule);
    }
}