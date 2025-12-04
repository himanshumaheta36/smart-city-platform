package com.smartcity.airquality.model.soap;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"airQualityData"})
@XmlRootElement(name = "GetAirQualityResponse", namespace = "http://smartcity.com/airquality")
public class GetAirQualityResponse {

    @XmlElement(name = "airQualityData", namespace = "http://smartcity.com/airquality")
    protected AirQualityData airQualityData;

    public AirQualityData getAirQualityData() {
        return airQualityData;
    }

    public void setAirQualityData(AirQualityData value) {
        this.airQualityData = value;
    }
}