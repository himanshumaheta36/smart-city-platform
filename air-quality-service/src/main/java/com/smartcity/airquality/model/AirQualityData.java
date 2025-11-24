package com.smartcity.airquality.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "air_quality_data")
public class AirQualityData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Zone name is required")
    @Column(name = "zone_name")
    private String zoneName;
    
    @NotNull(message = "AQI value is required")
    @DecimalMin(value = "0", message = "AQI must be positive")
    @Column(name = "aqi_value")
    private Double aqiValue;
    
    @NotBlank(message = "AQI category is required")
    @Column(name = "aqi_category")
    private String aqiCategory;
    
    @Column(name = "pm25")
    private Double pm25; // Particulate Matter 2.5
    
    @Column(name = "pm10")
    private Double pm10; // Particulate Matter 10
    
    @Column(name = "no2")
    private Double no2; // Nitrogen Dioxide
    
    @Column(name = "o3")
    private Double o3; // Ozone
    
    @Column(name = "co")
    private Double co; // Carbon Monoxide
    
    @Column(name = "so2")
    private Double so2; // Sulfur Dioxide
    
    @NotNull(message = "Measurement date is required")
    @Column(name = "measurement_date")
    private LocalDateTime measurementDate;
    
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "longitude")
    private Double longitude;
    
    // Constructors
    public AirQualityData() {}
    
    public AirQualityData(String zoneName, Double aqiValue, String aqiCategory, 
                         Double pm25, Double pm10, Double no2, Double o3, 
                         Double co, Double so2, LocalDateTime measurementDate) {
        this.zoneName = zoneName;
        this.aqiValue = aqiValue;
        this.aqiCategory = aqiCategory;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.no2 = no2;
        this.o3 = o3;
        this.co = co;
        this.so2 = so2;
        this.measurementDate = measurementDate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    
    public Double getAqiValue() { return aqiValue; }
    public void setAqiValue(Double aqiValue) { this.aqiValue = aqiValue; }
    
    public String getAqiCategory() { return aqiCategory; }
    public void setAqiCategory(String aqiCategory) { this.aqiCategory = aqiCategory; }
    
    public Double getPm25() { return pm25; }
    public void setPm25(Double pm25) { this.pm25 = pm25; }
    
    public Double getPm10() { return pm10; }
    public void setPm10(Double pm10) { this.pm10 = pm10; }
    
    public Double getNo2() { return no2; }
    public void setNo2(Double no2) { this.no2 = no2; }
    
    public Double getO3() { return o3; }
    public void setO3(Double o3) { this.o3 = o3; }
    
    public Double getCo() { return co; }
    public void setCo(Double co) { this.co = co; }
    
    public Double getSo2() { return so2; }
    public void setSo2(Double so2) { this.so2 = so2; }
    
    public LocalDateTime getMeasurementDate() { return measurementDate; }
    public void setMeasurementDate(LocalDateTime measurementDate) { this.measurementDate = measurementDate; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}