package com.smartcity.orchestration.model;

public class AirQualityInfo {
    private String zoneName;
    private Double aqiValue;
    private String aqiCategory;
    private Double pm25;
    private Double pm10;
    private Double no2;
    private Double o3;
    private String recommendation;
    
    // Constructeurs
    public AirQualityInfo() {}
    
    public AirQualityInfo(String zoneName, Double aqiValue, String aqiCategory) {
        this.zoneName = zoneName;
        this.aqiValue = aqiValue;
        this.aqiCategory = aqiCategory;
    }
    
    // Getters et Setters
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
    
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    
    public boolean isGoodQuality() {
        return aqiValue != null && aqiValue <= 50;
    }
    
    public boolean isAcceptableForOutdoor() {
        return aqiValue != null && aqiValue <= 100;
    }
}