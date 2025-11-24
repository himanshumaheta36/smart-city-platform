package com.smartcity.airquality.endpoint;

import com.smartcity.airquality.model.AirQualityData;
import com.smartcity.airquality.model.soap.*;
import com.smartcity.airquality.service.AirQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Endpoint
public class AirQualityEndpoint {
    
    private static final String NAMESPACE_URI = "http://smartcity.com/airquality";
    
    @Autowired
    private AirQualityService airQualityService;
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAirQualityRequest")
    @ResponsePayload
    public GetAirQualityResponse getAirQuality(@RequestPayload GetAirQualityRequest request) {
        GetAirQualityResponse response = new GetAirQualityResponse();
        
        String zoneName = request.getZoneName();
        Optional<AirQualityData> airQualityData = airQualityService.getLatestAirQualityByZone(zoneName);
        
        if (airQualityData.isPresent()) {
            response.setAirQualityData(mapToSoapAirQualityData(airQualityData.get()));
        } else {
            // Retourner des données par défaut si la zone n'est pas trouvée
            response.setAirQualityData(createDefaultAirQualityData(zoneName));
        }
        
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllZonesRequest")
    @ResponsePayload
    public GetAllZonesResponse getAllZones(@RequestPayload GetAllZonesRequest request) {
        GetAllZonesResponse response = new GetAllZonesResponse();
        
        List<AirQualityData> latestData = airQualityService.getLatestAirQualityForAllZones();
        List<com.smartcity.airquality.model.soap.AirQualityData> airQualityDataList = latestData.stream()
                .map(this::mapToSoapAirQualityData)
                .toList();
        
        response.getAirQualityData().addAll(airQualityDataList);
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CompareZonesRequest")
    @ResponsePayload
    public CompareZonesResponse compareZones(@RequestPayload CompareZonesRequest request) {
        CompareZonesResponse response = new CompareZonesResponse();
        
        String zone1 = request.getZone1();
        String zone2 = request.getZone2();
        
        String comparisonResult = airQualityService.compareZones(zone1, zone2);
        response.setComparisonResult(comparisonResult);
        
        return response;
    }
    
    private com.smartcity.airquality.model.soap.AirQualityData mapToSoapAirQualityData(com.smartcity.airquality.model.AirQualityData entity) {
        com.smartcity.airquality.model.soap.AirQualityData soapData = new com.smartcity.airquality.model.soap.AirQualityData();
        soapData.setZoneName(entity.getZoneName());
        soapData.setAqiValue(entity.getAqiValue());
        soapData.setAqiCategory(entity.getAqiCategory());
        soapData.setPm25(entity.getPm25());
        soapData.setPm10(entity.getPm10());
        soapData.setNo2(entity.getNo2());
        soapData.setO3(entity.getO3());
        soapData.setCo(entity.getCo());
        soapData.setSo2(entity.getSo2());
        soapData.setMeasurementDate(convertToXmlGregorianCalendar(entity.getMeasurementDate()));
        
        return soapData;
    }
    
    private com.smartcity.airquality.model.soap.AirQualityData createDefaultAirQualityData(String zoneName) {
        com.smartcity.airquality.model.soap.AirQualityData defaultData = new com.smartcity.airquality.model.soap.AirQualityData();
        defaultData.setZoneName(zoneName);
        defaultData.setAqiValue(50.0);
        defaultData.setAqiCategory("Moderate");
        defaultData.setPm25(12.5);
        defaultData.setPm10(25.0);
        defaultData.setNo2(20.0);
        defaultData.setO3(45.0);
        defaultData.setCo(1.2);
        defaultData.setSo2(5.0);
        defaultData.setMeasurementDate(convertToXmlGregorianCalendar(LocalDateTime.now()));
        
        return defaultData;
    }
    
    private XMLGregorianCalendar convertToXmlGregorianCalendar(LocalDateTime localDateTime) {
        try {
            GregorianCalendar gregorianCalendar = GregorianCalendar.from(localDateTime.atZone(java.time.ZoneId.systemDefault()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Error converting LocalDateTime to XMLGregorianCalendar", e);
        }
    }
}