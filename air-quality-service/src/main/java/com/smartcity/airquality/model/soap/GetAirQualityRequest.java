package com.smartcity.airquality.model.soap;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "zoneName"
})
@XmlRootElement(name = "GetAirQualityRequest", namespace = "http://smartcity.com/airquality")
public class GetAirQualityRequest {

    @XmlElement(required = true)
    protected String zoneName;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String value) {
        this.zoneName = value;
    }
}