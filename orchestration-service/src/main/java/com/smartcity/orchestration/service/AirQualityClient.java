package com.smartcity.orchestration.service;

import com.smartcity.orchestration.model.AirQualityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import reactor.core.publisher.Mono;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class AirQualityClient {
    
    private static final Logger log = LoggerFactory.getLogger(AirQualityClient.class);
    
    private final WebClient webClient;
    private final String airQualityUrl;
    
    public AirQualityClient(WebClient webClient, 
                           @Value("${services.air-quality.url}") String airQualityUrl) {
        this.webClient = webClient;
        this.airQualityUrl = airQualityUrl;
    }
    
    public Mono<AirQualityInfo> getAirQualityByZone(String zoneName) {
        String soapRequest = buildSoapRequest(zoneName);
        
        log.info("📡 Appel SOAP Air Quality pour zone: {}", zoneName);
        
        return webClient.post()
                .uri(airQualityUrl + "/ws")
                .contentType(MediaType.TEXT_XML)
                .header("SOAPAction", "")
                .bodyValue(soapRequest)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseSoapResponse)
                .doOnSuccess(info -> log.info("✅ Air Quality reçu: AQI={}", info.getAqiValue()))
                .doOnError(e -> log.error("❌ Erreur Air Quality: {}", e.getMessage()))
                .onErrorReturn(createDefaultAirQuality(zoneName));
    }
    
    public Mono<List<AirQualityInfo>> getAllZones() {
        String soapRequest = """
            <?xml version="1.0" encoding="UTF-8"?>
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                              xmlns:air="http://smartcity.com/airquality">
                <soapenv:Header/>
                <soapenv:Body>
                    <air:GetAllZonesRequest/>
                </soapenv:Body>
            </soapenv:Envelope>
            """;
        
        return webClient.post()
                .uri(airQualityUrl + "/ws")
                .contentType(MediaType.TEXT_XML)
                .header("SOAPAction", "")
                .bodyValue(soapRequest)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseAllZonesResponse)
                .doOnError(e -> log.error("❌ Erreur getAllZones: {}", e.getMessage()))
                .onErrorReturn(List.of());
    }
    
    private String buildSoapRequest(String zoneName) {
        return String.format("""
            <?xml version="1.0" encoding="UTF-8"?>
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                              xmlns:air="http://smartcity.com/airquality">
                <soapenv:Header/>
                <soapenv:Body>
                    <air:GetAirQualityRequest>
                        <air:zoneName>%s</air:zoneName>
                    </air:GetAirQualityRequest>
                </soapenv:Body>
            </soapenv:Envelope>
            """, zoneName);
    }
    
    private AirQualityInfo parseSoapResponse(String xmlResponse) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));
            
            AirQualityInfo info = new AirQualityInfo();
            info.setZoneName(getElementValue(doc, "zoneName"));
            info.setAqiValue(parseDouble(getElementValue(doc, "aqiValue")));
            info.setAqiCategory(getElementValue(doc, "aqiCategory"));
            info.setPm25(parseDouble(getElementValue(doc, "pm25")));
            info.setPm10(parseDouble(getElementValue(doc, "pm10")));
            info.setNo2(parseDouble(getElementValue(doc, "no2")));
            info.setO3(parseDouble(getElementValue(doc, "o3")));
            
            return info;
        } catch (Exception e) {
            log.error("Erreur parsing SOAP: {}", e.getMessage());
            return createDefaultAirQuality("Unknown");
        }
    }
    
    private List<AirQualityInfo> parseAllZonesResponse(String xmlResponse) {
        List<AirQualityInfo> zones = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));
            
            NodeList nodes = doc.getElementsByTagNameNS("*", "airQualityData");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element elem = (Element) nodes.item(i);
                AirQualityInfo info = new AirQualityInfo();
                info.setZoneName(getElementValueFromElement(elem, "zoneName"));
                info.setAqiValue(parseDouble(getElementValueFromElement(elem, "aqiValue")));
                info.setAqiCategory(getElementValueFromElement(elem, "aqiCategory"));
                info.setPm25(parseDouble(getElementValueFromElement(elem, "pm25")));
                zones.add(info);
            }
        } catch (Exception e) {
            log.error("Erreur parsing zones: {}", e.getMessage());
        }
        return zones;
    }
    
    private String getElementValue(Document doc, String tagName) {
        NodeList nodes = doc.getElementsByTagNameNS("*", tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
    
    private String getElementValueFromElement(Element elem, String tagName) {
        NodeList nodes = elem.getElementsByTagNameNS("*", tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
    
    private Double parseDouble(String value) {
        try {
            return value != null && !value.isEmpty() ? Double.parseDouble(value) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private AirQualityInfo createDefaultAirQuality(String zoneName) {
        AirQualityInfo info = new AirQualityInfo();
        info.setZoneName(zoneName);
        info.setAqiValue(50.0);
        info.setAqiCategory("Moderate");
        info.setPm25(12.0);
        return info;
    }
}