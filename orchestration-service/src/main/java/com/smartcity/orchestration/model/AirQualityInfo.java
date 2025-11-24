package com.smartcity.orchestration.model;

public class AirQualityInfo {
    private String location;
    private int aqi;
    private String quality;
    private String mainPollutant;
    
    // Constructeurs
    public AirQualityInfo() {}
    
    public AirQualityInfo(String location, int aqi, String quality, String mainPollutant) {
        this.location = location;
        this.aqi = aqi;
        this.quality = quality;
        this.mainPollutant = mainPollutant;
    }
    
    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public int getAqi() { return aqi; }
    public void setAqi(int aqi) { this.aqi = aqi; }
    
    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
    
    public String getMainPollutant() { return mainPollutant; }
    public void setMainPollutant(String mainPollutant) { this.mainPollutant = mainPollutant; }
}