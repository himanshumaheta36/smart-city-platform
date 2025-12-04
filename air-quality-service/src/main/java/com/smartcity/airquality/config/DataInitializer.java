package com.smartcity.airquality.config;

import com.smartcity.airquality.model.AirQualityData;
import com.smartcity.airquality.repository.AirQualityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AirQualityRepository airQualityRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║     INITIALISATION DES DONNÉES QUALITÉ DE L'AIR              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        
        // Vérifier le nombre d'enregistrements existants
        long existingCount = airQualityRepository.count();
        System.out.println("📊 Enregistrements existants: " + existingCount);
        
        // Supprimer les données existantes pour éviter les doublons
        if (existingCount > 0) {
            System.out.println("🗑️ Suppression des données existantes...");
            airQualityRepository.deleteAll();
            airQualityRepository.flush();
        }

        // Créer les données de qualité d'air
        LocalDateTime now = LocalDateTime.now();
        
        List<AirQualityData> dataList = Arrays.asList(
            createData("Centre-ville", 45.2, "Good", 10.5, 22.1, 18.3, 42.7, 0.8, 3.2, now, 48.8566, 2.3522),
            createData("Quartier Nord", 68.7, "Moderate", 15.8, 35.2, 25.6, 38.9, 1.2, 4.8, now, 48.8900, 2.3500),
            createData("Zone Industrielle", 125.3, "Unhealthy for Sensitive Groups", 35.6, 68.9, 45.2, 28.4, 2.5, 12.7, now, 48.8300, 2.3700),
            createData("Parc Central", 32.1, "Good", 8.2, 18.7, 12.4, 48.3, 0.6, 2.1, now, 48.8600, 2.3300),
            createData("Banlieue Sud", 55.8, "Moderate", 12.9, 28.4, 22.1, 35.6, 1.1, 3.9, now, 48.8200, 2.2900)
        );

        // Sauvegarder toutes les données
        List<AirQualityData> savedData = airQualityRepository.saveAll(dataList);
        airQualityRepository.flush();
        
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║     DONNÉES INITIALISÉES AVEC SUCCÈS                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("📊 Nombre d'enregistrements créés: " + savedData.size());
        System.out.println("");
        System.out.println("📋 Liste des zones disponibles:");
        
        // Vérification en relisant depuis la base
        List<AirQualityData> verification = airQualityRepository.findAll();
        verification.forEach(d -> {
            System.out.println("   ✅ Zone: '" + d.getZoneName() + "'");
            System.out.println("      - AQI: " + d.getAqiValue() + " (" + d.getAqiCategory() + ")");
            System.out.println("      - PM2.5: " + d.getPm25() + " µg/m³");
            System.out.println("      - PM10: " + d.getPm10() + " µg/m³");
            System.out.println("      - NO2: " + d.getNo2() + " µg/m³");
        });
        
        System.out.println("");
        System.out.println("🎯 Total zones en base: " + airQualityRepository.count());
    }
    
    private AirQualityData createData(String zoneName, double aqi, String category,
            double pm25, double pm10, double no2, double o3, double co, double so2,
            LocalDateTime measurementDate, double lat, double lon) {
        
        AirQualityData data = new AirQualityData();
        data.setZoneName(zoneName);
        data.setAqiValue(aqi);
        data.setAqiCategory(category);
        data.setPm25(pm25);
        data.setPm10(pm10);
        data.setNo2(no2);
        data.setO3(o3);
        data.setCo(co);
        data.setSo2(so2);
        data.setMeasurementDate(measurementDate);
        data.setLatitude(lat);
        data.setLongitude(lon);
        
        return data;
    }
}