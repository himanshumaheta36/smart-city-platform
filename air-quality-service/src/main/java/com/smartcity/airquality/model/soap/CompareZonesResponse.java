package com.smartcity.airquality.model.soap;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "comparisonResult"
})
@XmlRootElement(name = "CompareZonesResponse", namespace = "http://smartcity.com/airquality")
public class CompareZonesResponse {

    @XmlElement(required = true)
    protected String comparisonResult;

    public String getComparisonResult() {
        return comparisonResult;
    }

    public void setComparisonResult(String value) {
        this.comparisonResult = value;
    }
}