package com.smartcity.airquality.model.soap;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"zone1", "zone2"})
@XmlRootElement(name = "CompareZonesRequest", namespace = "http://smartcity.com/airquality")
public class CompareZonesRequest {

    @XmlElement(name = "zone1", namespace = "http://smartcity.com/airquality", required = true)
    protected String zone1;

    @XmlElement(name = "zone2", namespace = "http://smartcity.com/airquality", required = true)
    protected String zone2;

    public String getZone1() {
        return zone1;
    }

    public void setZone1(String value) {
        this.zone1 = value;
    }

    public String getZone2() {
        return zone2;
    }

    public void setZone2(String value) {
        this.zone2 = value;
    }
}