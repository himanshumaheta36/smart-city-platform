package com.smartcity.airquality.model.soap;

import jakarta.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirQualityData", namespace = "http://smartcity.com/airquality", propOrder = {
    "zoneName", "aqiValue", "aqiCategory", "pm25", "pm10", "no2", "o3", "co", "so2", "measurementDate"
})
public class AirQualityData {

    @XmlElement(namespace = "http://smartcity.com/airquality")
    protected String zoneName;

    @XmlElement(namespace = "http://smartcity.com/airquality")
    protected Double aqiValue;

    @XmlElement(namespace = "http://smartcity.com/airquality")
    protected String aqiCategory;

    @XmlElement(namespace = "http://smartcity.com/airquality")
    protected Double pm25;

    @XmlElement(namespace = "http://smartcity.com/airquality")
    protected Double pm10;

    @XmlElement(namespace = "http://smartcity.com/airquality")
    protected Double no2;

    @XmlElement(namespace = "http://smartcity.com/airquality")
    protected Double o3;

    @XmlElement(namespace = "http://smartcity.com/airquality")
    protected Double co;

    @XmlElement(namespace = "http://smartcity.com/airquality")
    protected Double so2;

    @XmlElement(namespace = "http://smartcity.com/airquality")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar measurementDate;

    // Getters and Setters
    public String getZoneName() { return zoneName; }
    public void setZoneName(String value) { this.zoneName = value; }

    public Double getAqiValue() { return aqiValue; }
    public void setAqiValue(Double value) { this.aqiValue = value; }

    public String getAqiCategory() { return aqiCategory; }
    public void setAqiCategory(String value) { this.aqiCategory = value; }

    public Double getPm25() { return pm25; }
    public void setPm25(Double value) { this.pm25 = value; }

    public Double getPm10() { return pm10; }
    public void setPm10(Double value) { this.pm10 = value; }

    public Double getNo2() { return no2; }
    public void setNo2(Double value) { this.no2 = value; }

    public Double getO3() { return o3; }
    public void setO3(Double value) { this.o3 = value; }

    public Double getCo() { return co; }
    public void setCo(Double value) { this.co = value; }

    public Double getSo2() { return so2; }
    public void setSo2(Double value) { this.so2 = value; }

    public XMLGregorianCalendar getMeasurementDate() { return measurementDate; }
    public void setMeasurementDate(XMLGregorianCalendar value) { this.measurementDate = value; }
}