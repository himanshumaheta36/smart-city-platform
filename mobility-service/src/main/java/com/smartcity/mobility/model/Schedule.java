package com.smartcity.mobility.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "transport_line_id")
    private TransportLine transportLine;
    
    @NotNull
    private String station;
    
    @NotNull
    private LocalTime departureTime;
    
    @NotNull
    private LocalTime arrivalTime;
    
    private String dayType; // WEEKDAY, WEEKEND, HOLIDAY
    
    // Constructors
    public Schedule() {}
    
    public Schedule(TransportLine transportLine, String station, 
                   LocalTime departureTime, LocalTime arrivalTime, String dayType) {
        this.transportLine = transportLine;
        this.station = station;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.dayType = dayType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public TransportLine getTransportLine() { return transportLine; }
    public void setTransportLine(TransportLine transportLine) { this.transportLine = transportLine; }
    
    public String getStation() { return station; }
    public void setStation(String station) { this.station = station; }
    
    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }
    
    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }
    
    public String getDayType() { return dayType; }
    public void setDayType(String dayType) { this.dayType = dayType; }
}