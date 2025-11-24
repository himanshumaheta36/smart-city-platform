package com.smartcity.airquality.service;

import com.smartcity.airquality.model.AirQualityData;
import com.smartcity.airquality.repository.AirQualityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AirQualityService {
    
    @Autowired
    private AirQualityRepository airQualityRepository;
    
    // Opérations CRUD de base
    public List<AirQualityData> getAllAirQualityData() {
        return airQualityRepository.findAll();
    }
    
    public Optional<AirQualityData> getAirQualityDataById(Long id) {
        return airQualityRepository.findById(id);
    }
    
    public AirQualityData saveAirQualityData(AirQualityData airQualityData) {
        if (airQualityData.getMeasurementDate() == null) {
            airQualityData.setMeasurementDate(LocalDateTime.now());
        }
        return airQualityRepository.save(airQualityData);
    }
    
    public void deleteAirQualityData(Long id) {
        airQualityRepository.deleteById(id);
    }
    
    // Méthodes métier spécifiques
    public List<AirQualityData> getAirQualityByZone(String zoneName) {
        return airQualityRepository.findByZoneName(zoneName);
    }
    
    public Optional<AirQualityData> getLatestAirQualityByZone(String zoneName) {
        return airQualityRepository.findLatestByZoneName(zoneName);
    }
    
    public List<AirQualityData> getLatestAirQualityForAllZones() {
        return airQualityRepository.findLatestForAllZones();
    }
    
    public List<AirQualityData> getAirQualityByCategory(String category) {
        return airQualityRepository.findByAqiCategory(category);
    }
    
    public List<AirQualityData> getAirQualityWithHighPollution(Double threshold) {
        return airQualityRepository.findByPollutantThreshold(threshold);
    }
    
    public List<AirQualityData> getZonesWithHighAQI(Double aqiThreshold) {
        return airQualityRepository.findByAqiValueGreaterThan(aqiThreshold);
    }
    
    public List<AirQualityData> getHistoricalData(String zoneName, LocalDateTime startDate, LocalDateTime endDate) {
        return airQualityRepository.findByZoneName(zoneName).stream()
                .filter(data -> !data.getMeasurementDate().isBefore(startDate) && 
                               !data.getMeasurementDate().isAfter(endDate))
                .toList();
    }
    
    // Méthode pour comparer deux zones
    public String compareZones(String zone1, String zone2) {
        Optional<AirQualityData> data1 = airQualityRepository.findLatestByZoneName(zone1);
        Optional<AirQualityData> data2 = airQualityRepository.findLatestByZoneName(zone2);
        
        if (data1.isEmpty() || data2.isEmpty()) {
            return "Données insuffisantes pour la comparaison";
        }
        
        Double aqi1 = data1.get().getAqiValue();
        Double aqi2 = data2.get().getAqiValue();
        
        if (aqi1 < aqi2) {
            return String.format("La zone %s a une meilleure qualité d'air (AQI: %.1f) que %s (AQI: %.1f)", 
                    zone1, aqi1, zone2, aqi2);
        } else if (aqi1 > aqi2) {
            return String.format("La zone %s a une meilleure qualité d'air (AQI: %.1f) que %s (AQI: %.1f)", 
                    zone2, aqi2, zone1, aqi1);
        } else {
            return String.format("Les deux zones ont la même qualité d'air (AQI: %.1f)", aqi1);
        }
    }
    
    // Méthode pour calculer la moyenne AQI d'une zone sur une période
    public Double calculateAverageAQI(String zoneName, LocalDateTime startDate, LocalDateTime endDate) {
        List<AirQualityData> historicalData = getHistoricalData(zoneName, startDate, endDate);
        return historicalData.stream()
                .mapToDouble(AirQualityData::getAqiValue)
                .average()
                .orElse(0.0);
    }
}