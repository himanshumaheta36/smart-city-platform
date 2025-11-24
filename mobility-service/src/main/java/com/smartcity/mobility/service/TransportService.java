package com.smartcity.mobility.service;

import com.smartcity.mobility.model.Schedule;
import com.smartcity.mobility.model.TrafficInfo;
import com.smartcity.mobility.model.TransportLine;
import com.smartcity.mobility.repository.ScheduleRepository;
import com.smartcity.mobility.repository.TrafficInfoRepository;
import com.smartcity.mobility.repository.TransportLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransportService {
    
    @Autowired
    private TransportLineRepository transportLineRepository;
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private TrafficInfoRepository trafficInfoRepository;
    
    // Transport Line Operations
    public List<TransportLine> getAllTransportLines() {
        return transportLineRepository.findAll();
    }
    
    public Optional<TransportLine> getTransportLineById(Long id) {
        return transportLineRepository.findById(id);
    }
    
    public Optional<TransportLine> getTransportLineByNumber(String lineNumber) {
        return transportLineRepository.findByLineNumber(lineNumber);
    }
    
    public List<TransportLine> getTransportLinesByType(String transportType) {
        return transportLineRepository.findByTransportType(transportType);
    }
    
    public List<TransportLine> getTransportLinesByStatus(String status) {
        return transportLineRepository.findByStatus(status);
    }
    
    public List<TransportLine> getTransportLinesByStation(String station) {
        return transportLineRepository.findByStation(station);
    }
    
    public TransportLine createTransportLine(TransportLine transportLine) {
        return transportLineRepository.save(transportLine);
    }
    
    public TransportLine updateTransportLineStatus(Long id, String status, Integer delayMinutes) {
        Optional<TransportLine> optionalLine = transportLineRepository.findById(id);
        if (optionalLine.isPresent()) {
            TransportLine line = optionalLine.get();
            line.setStatus(status);
            line.setDelayMinutes(delayMinutes);
            return transportLineRepository.save(line);
        }
        return null;
    }
    
    // Schedule Operations
    public List<Schedule> getSchedulesByLine(String lineNumber) {
        return scheduleRepository.findByTransportLineLineNumber(lineNumber);
    }
    
    public List<Schedule> getSchedulesByStationAndDayType(String station, String dayType) {
        return scheduleRepository.findByStationAndDayType(station, dayType);
    }
    
    public List<Schedule> getSchedulesByLineAndStationAndTime(String lineNumber, String station, 
                                                             String dayType, LocalTime startTime, 
                                                             LocalTime endTime) {
        return scheduleRepository.findSchedulesByLineAndStationAndTime(
            lineNumber, station, dayType, startTime, endTime);
    }
    
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
    
    // Traffic Info Operations
    public List<TrafficInfo> getAllActiveIncidents() {
        return trafficInfoRepository.findByResolvedAtIsNull();
    }
    
    public List<TrafficInfo> getIncidentsBySeverity(String severity) {
        return trafficInfoRepository.findBySeverity(severity);
    }
    
    public List<TrafficInfo> getIncidentsByLocation(String location) {
        return trafficInfoRepository.findByLocationContainingIgnoreCase(location);
    }
    
    public List<TrafficInfo> getIncidentsByType(String incidentType) {
        return trafficInfoRepository.findByIncidentType(incidentType);
    }
    
    public TrafficInfo reportIncident(TrafficInfo trafficInfo) {
        return trafficInfoRepository.save(trafficInfo);
    }
    
    public TrafficInfo resolveIncident(Long incidentId) {
        Optional<TrafficInfo> optionalIncident = trafficInfoRepository.findById(incidentId);
        if (optionalIncident.isPresent()) {
            TrafficInfo incident = optionalIncident.get();
            incident.setResolvedAt(LocalDateTime.now());
            return trafficInfoRepository.save(incident);
        }
        return null;
    }
    
    // Business Logic Methods
    public List<TransportLine> getDelayedLines() {
        return transportLineRepository.findByDelayMinutesGreaterThan(0);
    }
    
    public List<TrafficInfo> getRecentIncidents() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return trafficInfoRepository.findActiveIncidentsSince(yesterday);
    }
}