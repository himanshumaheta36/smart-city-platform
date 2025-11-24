package com.smartcity.orchestration.model;

import java.util.List;

public class JourneyPlan {
    private String startLocation;
    private String endLocation;
    private AirQualityInfo airQuality;
    private List<TransportOption> transportOptions;
    private String recommendation;
    private boolean airQualityGood;

    // Constructors
    public JourneyPlan() {}

    public JourneyPlan(String startLocation, String endLocation) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    // Getters and Setters
    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public String getEndLocation() { return endLocation; }
    public void setEndLocation(String endLocation) { this.endLocation = endLocation; }

    public AirQualityInfo getAirQuality() { return airQuality; }
    public void setAirQuality(AirQualityInfo airQuality) { this.airQuality = airQuality; }

    public List<TransportOption> getTransportOptions() { return transportOptions; }
    public void setTransportOptions(List<TransportOption> transportOptions) { this.transportOptions = transportOptions; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public boolean isAirQualityGood() { return airQualityGood; }
    public void setAirQualityGood(boolean airQualityGood) { this.airQualityGood = airQualityGood; }
}