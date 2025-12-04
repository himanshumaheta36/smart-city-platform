package com.smartcity.airquality.service;

import com.smartcity.airquality.model.AirQualityData;
import com.smartcity.airquality.repository.AirQualityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    
    /**
     * Récupère la dernière mesure de qualité d'air pour une zone donnée
     */
    public Optional<AirQualityData> getLatestAirQualityByZone(String zoneName) {
        System.out.println("🔍 [Service] Recherche données pour zone: '" + zoneName + "'");
        
        List<AirQualityData> allData = airQualityRepository.findAll();
        System.out.println("📊 [Service] Total enregistrements en base: " + allData.size());
        
        // Filtrer par nom de zone (insensible à la casse et aux espaces)
        String normalizedZoneName = zoneName.trim().toLowerCase();
        
        Optional<AirQualityData> result = allData.stream()
            .filter(d -> d.getZoneName() != null && 
                        d.getZoneName().trim().toLowerCase().equals(normalizedZoneName))
            .max(Comparator.comparing(AirQualityData::getMeasurementDate));
        
        if (result.isPresent()) {
            AirQualityData data = result.get();
            System.out.println("✅ [Service] Données trouvées:");
            System.out.println("   - Zone: '" + data.getZoneName() + "'");
            System.out.println("   - AQI: " + data.getAqiValue());
            System.out.println("   - PM2.5: " + data.getPm25());
            return result;
        }
        
        // Debug: afficher toutes les zones disponibles
        System.out.println("❌ [Service] Zone non trouvée: '" + zoneName + "'");
        System.out.println("📋 [Service] Zones disponibles:");
        allData.forEach(d -> System.out.println("   - '" + d.getZoneName() + "'"));
        
        return Optional.empty();
    }
    
    /**
     * Récupère les dernières mesures pour toutes les zones
     */
    public List<AirQualityData> getLatestAirQualityForAllZones() {
        System.out.println("🔍 [Service] Recherche de toutes les zones...");
        
        List<AirQualityData> allData = airQualityRepository.findAll();
        
        // Grouper par zone et prendre la plus récente de chaque groupe
        Map<String, AirQualityData> latestByZone = allData.stream()
            .filter(d -> d.getZoneName() != null)
            .collect(Collectors.toMap(
                AirQualityData::getZoneName,
                d -> d,
                (existing, replacement) -> 
                    existing.getMeasurementDate().isAfter(replacement.getMeasurementDate()) 
                        ? existing : replacement
            ));
        
        List<AirQualityData> result = new ArrayList<>(latestByZone.values());
        
        System.out.println("✅ [Service] Zones trouvées: " + result.size());
        result.forEach(d -> System.out.println("   - " + d.getZoneName() + ": AQI=" + d.getAqiValue()));
        
        return result;
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
    
    /**
     * Compare la qualité de l'air entre deux zones
     */
    public String compareZones(String zone1, String zone2) {
        System.out.println("🔍 [Service] Comparaison: '" + zone1 + "' vs '" + zone2 + "'");
        
        Optional<AirQualityData> data1 = getLatestAirQualityByZone(zone1);
        Optional<AirQualityData> data2 = getLatestAirQualityByZone(zone2);
        
        if (data1.isEmpty()) {
            System.out.println("❌ [Service] Données manquantes pour: '" + zone1 + "'");
        }
        if (data2.isEmpty()) {
            System.out.println("❌ [Service] Données manquantes pour: '" + zone2 + "'");
        }
        
        if (data1.isEmpty() || data2.isEmpty()) {
            // Lister les zones disponibles
            List<String> availableZones = airQualityRepository.findAll().stream()
                .map(AirQualityData::getZoneName)
                .distinct()
                .toList();
            return "Données insuffisantes pour la comparaison. Zones disponibles: " + availableZones;
        }
        
        Double aqi1 = data1.get().getAqiValue();
        Double aqi2 = data2.get().getAqiValue();
        
        System.out.println("✅ [Service] " + zone1 + ": AQI=" + aqi1 + ", " + zone2 + ": AQI=" + aqi2);
        
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
    
    public Double calculateAverageAQI(String zoneName, LocalDateTime startDate, LocalDateTime endDate) {
        List<AirQualityData> historicalData = getHistoricalData(zoneName, startDate, endDate);
        return historicalData.stream()
                .mapToDouble(AirQualityData::getAqiValue)
                .average()
                .orElse(0.0);
    }
}