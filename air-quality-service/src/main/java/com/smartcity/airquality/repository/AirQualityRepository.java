package com.smartcity.airquality.repository;

import com.smartcity.airquality.model.AirQualityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AirQualityRepository extends JpaRepository<AirQualityData, Long> {
    
    // Trouver les données par nom de zone
    List<AirQualityData> findByZoneName(String zoneName);
    
    // Trouver les données par catégorie AQI
    List<AirQualityData> findByAqiCategory(String aqiCategory);
    
    // Trouver les données les plus récentes pour chaque zone
    @Query("SELECT aqd FROM AirQualityData aqd WHERE aqd.measurementDate = " +
           "(SELECT MAX(aqd2.measurementDate) FROM AirQualityData aqd2 WHERE aqd2.zoneName = aqd.zoneName)")
    List<AirQualityData> findLatestForAllZones();
    
    // Trouver la donnée la plus récente pour une zone spécifique
    @Query("SELECT aqd FROM AirQualityData aqd WHERE aqd.zoneName = :zoneName " +
           "ORDER BY aqd.measurementDate DESC LIMIT 1")
    Optional<AirQualityData> findLatestByZoneName(@Param("zoneName") String zoneName);
    
    // Trouver les données dans une période spécifique
    List<AirQualityData> findByMeasurementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Trouver les zones avec AQI supérieur à une valeur
    List<AirQualityData> findByAqiValueGreaterThan(Double aqiValue);
    
    // Trouver les données par polluant spécifique
    @Query("SELECT aqd FROM AirQualityData aqd WHERE aqd.pm25 > :threshold OR aqd.pm10 > :threshold " +
           "OR aqd.no2 > :threshold OR aqd.o3 > :threshold OR aqd.co > :threshold OR aqd.so2 > :threshold")
    List<AirQualityData> findByPollutantThreshold(@Param("threshold") Double threshold);
}