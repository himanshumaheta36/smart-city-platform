package com.smartcity.airquality.model.soap;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"airQualityData"})
@XmlRootElement(name = "GetAllZonesResponse", namespace = "http://smartcity.com/airquality")
public class GetAllZonesResponse {

    @XmlElement(name = "airQualityData", namespace = "http://smartcity.com/airquality")
    protected List<AirQualityData> airQualityData;

    public List<AirQualityData> getAirQualityData() {
        if (airQualityData == null) {
            airQualityData = new ArrayList<>();
        }
        return this.airQualityData;
    }
}