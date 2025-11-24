# Air Quality Service - Service SOAP

## Description
Service SOAP pour la surveillance de la qualité de l'air urbain.

## Technologie
- **Protocole**: SOAP (Web Services)
- **Framework**: Spring Boot + Spring WS
- **Base de données**: H2 (en mémoire)
- **Port**: 8082

## Opérations SOAP

### GetAirQuality
Récupérer la qualité d'air pour une zone.

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:air="http://smartcity.com/airquality">
   <soapenv:Header/>
   <soapenv:Body>
      <air:GetAirQualityRequest>
         <air:zoneName>Centre-ville</air:zoneName>
      </air:GetAirQualityRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### GetAllZones
Liste toutes les zones surveillées.

### CompareZones
Compare la qualité d'air entre deux zones.

## Tests

### Via curl (SOAP)
```bash
curl -X POST http://localhost:8082/airquality/ws \
  -H "Content-Type: text/xml" \
  -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
   <soapenv:Header/>
   <soapenv:Body>
      <air:GetAirQualityRequest>
         <air:zoneName>Centre-ville</air:zoneName>
      </air:GetAirQualityRequest>
   </soapenv:Body>
</soapenv:Envelope>'
```

### Via SoapUI
1. Importer le WSDL: `http://localhost:8082/airquality/ws/airquality.wsdl`
2. Créer une requête
3. Tester les opérations

## WSDL
http://localhost:8082/airquality/ws/airquality.wsdl

## Zones de Test
- Centre-ville (AQI: 45.2 - Good)
- Quartier Nord (AQI: 68.7 - Moderate)
- Zone Industrielle (AQI: 125.3 - Unhealthy)
- Parc Central (AQI: 32.1 - Good)
- Banlieue Sud (AQI: 55.8 - Moderate)

## H2 Console
- URL: http://localhost:8082/airquality/h2-console
- JDBC URL: `jdbc:h2:mem:airqualitydb`
- User: `sa`
- Password: `password`