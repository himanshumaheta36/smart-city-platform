package com.smartcity.airquality.config;

import com.smartcity.airquality.model.AirQualityData;
import com.smartcity.airquality.repository.AirQualityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AirQualityRepository airQualityRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INITIALISATION DES DONNÉES QUALITÉ DE L'AIR ===");
        
        // Nettoyer les données existantes
        airQualityRepository.deleteAll();

        // Créer les données de qualité d'air
        AirQualityData data1 = new AirQualityData();
        data1.setZoneName("Centre-ville");
        data1.setAqiValue(45.2);
        data1.setAqiCategory("Good");
        data1.setPm25(10.5);
        data1.setPm10(22.1);
        data1.setNo2(18.3);
        data1.setO3(42.7);
        data1.setCo(0.8);
        data1.setSo2(3.2);
        data1.setMeasurementDate(LocalDateTime.now());
        data1.setLatitude(48.8566);
        data1.setLongitude(2.3522);

        AirQualityData data2 = new AirQualityData();
        data2.setZoneName("Quartier Nord");
        data2.setAqiValue(68.7);
        data2.setAqiCategory("Moderate");
        data2.setPm25(15.8);
        data2.setPm10(35.2);
        data2.setNo2(25.6);
        data2.setO3(38.9);
        data2.setCo(1.2);
        data2.setSo2(4.8);
        data2.setMeasurementDate(LocalDateTime.now());
        data2.setLatitude(48.8900);
        data2.setLongitude(2.3500);

        AirQualityData data3 = new AirQualityData();
        data3.setZoneName("Zone Industrielle");
        data3.setAqiValue(125.3);
        data3.setAqiCategory("Unhealthy for Sensitive Groups");
        data3.setPm25(35.6);
        data3.setPm10(68.9);
        data3.setNo2(45.2);
        data3.setO3(28.4);
        data3.setCo(2.5);
        data3.setSo2(12.7);
        data3.setMeasurementDate(LocalDateTime.now());
        data3.setLatitude(48.8300);
        data3.setLongitude(2.3700);

        AirQualityData data4 = new AirQualityData();
        data4.setZoneName("Parc Central");
        data4.setAqiValue(32.1);
        data4.setAqiCategory("Good");
        data4.setPm25(8.2);
        data4.setPm10(18.7);
        data4.setNo2(12.4);
        data4.setO3(48.3);
        data4.setCo(0.6);
        data4.setSo2(2.1);
        data4.setMeasurementDate(LocalDateTime.now());
        data4.setLatitude(48.8600);
        data4.setLongitude(2.3300);

        AirQualityData data5 = new AirQualityData();
        data5.setZoneName("Banlieue Sud");
        data5.setAqiValue(55.8);
        data5.setAqiCategory("Moderate");
        data5.setPm25(12.9);
        data5.setPm10(28.4);
        data5.setNo2(22.1);
        data5.setO3(35.6);
        data5.setCo(1.1);
        data5.setSo2(3.9);
        data5.setMeasurementDate(LocalDateTime.now());
        data5.setLatitude(48.8200);
        data5.setLongitude(2.2900);

        // Sauvegarder toutes les données
        airQualityRepository.saveAll(Arrays.asList(data1, data2, data3, data4, data5));

        System.out.println("=== DONNÉES INITIALISÉES AVEC SUCCÈS ===");
        System.out.println("Nombre d'enregistrements créés: " + airQualityRepository.count());
    }
}