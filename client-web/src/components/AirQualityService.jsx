import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../App.css';

const AirQualityService = () => {
  const [zones, setZones] = useState([]);
  const [selectedZone, setSelectedZone] = useState('');
  const [zone2, setZone2] = useState('');
  const [airQualityData, setAirQualityData] = useState(null);
  const [comparisonResult, setComparisonResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('check');
  const [error, setError] = useState('');

  const availableZones = [
    'Centre-ville',
    'Quartier Nord',
    'Zone Industrielle',
    'Parc Central',
    'Banlieue Sud'
  ];

  useEffect(() => {
    loadAllZones();
  }, []);

  // ✅ CORRIGÉ: Helper function pour extraire le texte d'un élément XML
  const getTextContent = (parentElement, tagName) => {
    if (!parentElement) return '';
    
    // Liste des préfixes possibles
    const prefixes = ['ns2:', 'tns:', 'air:', ''];
    
    for (const prefix of prefixes) {
      const elements = parentElement.getElementsByTagName(prefix + tagName);
      if (elements.length > 0 && elements[0].textContent) {
        return elements[0].textContent.trim();
      }
    }
    
    // Essayer avec namespace
    try {
      const element = parentElement.getElementsByTagNameNS('http://smartcity.com/airquality', tagName)[0];
      if (element?.textContent) {
        return element.textContent.trim();
      }
    } catch (e) {
      // Ignorer les erreurs de namespace
    }
    
    return '';
  };

  // ✅ NOUVELLE: Fonction pour parser un élément airQualityData
  const parseAirQualityElement = (elem) => {
    const zoneName = getTextContent(elem, 'zoneName');
    const aqiValue = parseFloat(getTextContent(elem, 'aqiValue')) || 0;
    const aqiCategory = getTextContent(elem, 'aqiCategory') || 'Unknown';
    const pm25 = parseFloat(getTextContent(elem, 'pm25')) || 0;
    const pm10 = parseFloat(getTextContent(elem, 'pm10')) || 0;
    const no2 = parseFloat(getTextContent(elem, 'no2')) || 0;
    const o3 = parseFloat(getTextContent(elem, 'o3')) || 0;
    const co = parseFloat(getTextContent(elem, 'co')) || 0;
    const so2 = parseFloat(getTextContent(elem, 'so2')) || 0;

    console.log(`📋 Parsed: ${zoneName} - AQI: ${aqiValue}, PM2.5: ${pm25}`);

    return {
      zoneName,
      aqiValue,
      aqiCategory,
      pm25,
      pm10,
      no2,
      o3,
      co,
      so2
    };
  };

  // Fonction avec fallback pour les appels SOAP
  const callSoapService = async (soapRequest) => {
    const soapConfig = {
      headers: { 
        'Content-Type': 'text/xml; charset=utf-8',
        'SOAPAction': ''
      },
      timeout: 20000
    };

    try {
      console.log('🔄 Tentative via Gateway...');
      const response = await axios.post('http://localhost:8080/api/air-quality/ws', soapRequest, soapConfig);
      console.log('✅ Réponse reçue via Gateway');
      return response;
    } catch (gatewayError) {
      console.log('❌ Gateway échoué, tentative directe...');
      try {
        const response = await axios.post('http://localhost:8082/airquality/ws', soapRequest, soapConfig);
        console.log('✅ Réponse reçue directement');
        return response;
      } catch (directError) {
        console.error('❌ Les deux méthodes ont échoué');
        throw gatewayError;
      }
    }
  };

  // ✅ CORRIGÉ: loadAllZones avec meilleur parsing
  const loadAllZones = async () => {
    setLoading(true);
    setError('');
    try {
      const soapRequest = `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
  <soapenv:Header/>
  <soapenv:Body>
    <air:GetAllZonesRequest/>
  </soapenv:Body>
</soapenv:Envelope>`;

      const response = await callSoapService(soapRequest);
      
      console.log('📄 Réponse XML brute:', response.data.substring(0, 500));

      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(response.data, 'text/xml');
      
      // Vérifier les erreurs de parsing
      const parserError = xmlDoc.getElementsByTagName('parsererror');
      if (parserError.length > 0) {
        throw new Error('Erreur de parsing XML');
      }

      // ✅ Chercher les éléments avec différents préfixes
      let airQualityElements = xmlDoc.getElementsByTagNameNS('http://smartcity.com/airquality', 'airQualityData');
      
      if (airQualityElements.length === 0) {
        airQualityElements = xmlDoc.getElementsByTagName('ns2:airQualityData');
      }
      if (airQualityElements.length === 0) {
        airQualityElements = xmlDoc.getElementsByTagName('airQualityData');
      }
      if (airQualityElements.length === 0) {
        airQualityElements = xmlDoc.getElementsByTagName('tns:airQualityData');
      }
      
      console.log('📊 Nombre d\'éléments airQualityData trouvés:', airQualityElements.length);

      if (airQualityElements.length === 0) {
        // Debug: afficher la structure
        console.log('🔍 Structure XML:', xmlDoc.documentElement?.outerHTML?.substring(0, 1000));
        setError('Aucune zone trouvée dans la réponse');
        setZones([]);
        return;
      }

      const parsedZones = Array.from(airQualityElements).map((elem, index) => {
        console.log(`\n🔍 Parsing élément ${index + 1}...`);
        return parseAirQualityElement(elem);
      });

      // Filtrer les zones sans nom
      const validZones = parsedZones.filter(z => z.zoneName && z.zoneName.length > 0);
      
      console.log('✅ Zones valides:', validZones.length);
      validZones.forEach(z => console.log(`  - ${z.zoneName}: AQI=${z.aqiValue}`));

      setZones(validZones);
    } catch (error) {
      console.error('❌ Erreur chargement zones:', error);
      setError('Erreur lors du chargement des zones: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // ✅ CORRIGÉ: checkAirQuality avec meilleur parsing
  const checkAirQuality = async (e) => {
    e.preventDefault();
    if (!selectedZone) return;

    setLoading(true);
    setError('');
    try {
      const soapRequest = `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
  <soapenv:Header/>
  <soapenv:Body>
    <air:GetAirQualityRequest>
      <air:zoneName>${selectedZone}</air:zoneName>
    </air:GetAirQualityRequest>
  </soapenv:Body>
</soapenv:Envelope>`;

      const response = await callSoapService(soapRequest);
      
      console.log('📄 Réponse GetAirQuality:', response.data.substring(0, 500));

      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(response.data, 'text/xml');
      
      // Trouver l'élément airQualityData
      let airQualityElement = xmlDoc.getElementsByTagNameNS('http://smartcity.com/airquality', 'airQualityData')[0];
      if (!airQualityElement) {
        airQualityElement = xmlDoc.getElementsByTagName('ns2:airQualityData')[0];
      }
      if (!airQualityElement) {
        airQualityElement = xmlDoc.getElementsByTagName('airQualityData')[0];
      }

      if (airQualityElement) {
        const data = parseAirQualityElement(airQualityElement);
        console.log('✅ Données parsées:', data);
        setAirQualityData(data);
      } else {
        console.error('❌ Élément airQualityData non trouvé');
        setError('Données non trouvées pour cette zone');
      }
    } catch (error) {
      console.error('❌ Erreur vérification qualité:', error);
      setError('Erreur lors de la vérification: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // ✅ CORRIGÉ: compareZones avec meilleur parsing
  const compareZones = async (e) => {
    e.preventDefault();
    if (!selectedZone || !zone2) return;

    setLoading(true);
    setError('');
    try {
      const soapRequest = `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:air="http://smartcity.com/airquality">
  <soapenv:Header/>
  <soapenv:Body>
    <air:CompareZonesRequest>
      <air:zone1>${selectedZone}</air:zone1>
      <air:zone2>${zone2}</air:zone2>
    </air:CompareZonesRequest>
  </soapenv:Body>
</soapenv:Envelope>`;

      const response = await callSoapService(soapRequest);
      
      console.log('📄 Réponse CompareZones:', response.data);

      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(response.data, 'text/xml');
      
      // Chercher comparisonResult avec différents préfixes
      let result = null;
      const prefixes = ['ns2:', 'tns:', 'air:', ''];
      
      for (const prefix of prefixes) {
        const elem = xmlDoc.getElementsByTagName(prefix + 'comparisonResult')[0];
        if (elem?.textContent) {
          result = elem.textContent.trim();
          break;
        }
      }
      
      // Essayer avec namespace
      if (!result) {
        const elem = xmlDoc.getElementsByTagNameNS('http://smartcity.com/airquality', 'comparisonResult')[0];
        if (elem?.textContent) {
          result = elem.textContent.trim();
        }
      }

      console.log('✅ Résultat comparaison:', result);
      setComparisonResult(result || 'Résultat non disponible');
    } catch (error) {
      console.error('❌ Erreur comparaison:', error);
      setError('Erreur lors de la comparaison: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const getAQIColor = (aqi) => {
    if (aqi <= 50) return '#10b981';
    if (aqi <= 100) return '#fbbf24';
    if (aqi <= 150) return '#f97316';
    if (aqi <= 200) return '#ef4444';
    return '#7f1d1d';
  };

  const getAQILevel = (aqi) => {
    if (aqi <= 50) return 'Bon';
    if (aqi <= 100) return 'Modéré';
    if (aqi <= 150) return 'Mauvais pour groupes sensibles';
    if (aqi <= 200) return 'Mauvais';
    return 'Très mauvais';
  };

  const getAQIRecommendation = (aqi) => {
    if (aqi <= 50) return 'La qualité de l\'air est satisfaisante. Idéal pour les activités extérieures.';
    if (aqi <= 100) return 'La qualité de l\'air est acceptable. Activités extérieures possibles.';
    if (aqi <= 150) return 'Les personnes sensibles devraient limiter les activités extérieures prolongées.';
    if (aqi <= 200) return 'Tout le monde devrait limiter les activités extérieures prolongées.';
    return 'Évitez toute activité extérieure. Restez à l\'intérieur.';
  };

  return (
    <div>
      {/* Header */}
      <div className="card" style={{ 
        background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
        color: 'white'
      }}>
        <h2 style={{ marginBottom: '0.5rem' }}>🌫️ Service Qualité de l'Air</h2>
        <p style={{ opacity: 0.9, marginBottom: '1rem' }}>
          SOAP Web Service - Surveillance de la pollution atmosphérique
        </p>
        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Protocole:</strong> SOAP
          </div>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Zones:</strong> {zones.length}
          </div>
        </div>
      </div>

      {/* Error Message */}
      {error && (
        <div className="error" style={{ marginBottom: '1rem' }}>
          ❌ {error}
        </div>
      )}

      {/* Tabs */}
      <div className="card">
        <div style={{ display: 'flex', gap: '0.5rem', borderBottom: '2px solid #e5e7eb', paddingBottom: '0.5rem' }}>
          <button
            onClick={() => setActiveTab('check')}
            style={{
              padding: '0.5rem 1rem',
              background: activeTab === 'check' ? '#f5576c' : 'transparent',
              color: activeTab === 'check' ? 'white' : '#666',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            🔍 Vérifier Qualité
          </button>
          <button
            onClick={() => setActiveTab('compare')}
            style={{
              padding: '0.5rem 1rem',
              background: activeTab === 'compare' ? '#f5576c' : 'transparent',
              color: activeTab === 'compare' ? 'white' : '#666',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            📊 Comparer Zones
          </button>
          <button
            onClick={() => setActiveTab('overview')}
            style={{
              padding: '0.5rem 1rem',
              background: activeTab === 'overview' ? '#f5576c' : 'transparent',
              color: activeTab === 'overview' ? 'white' : '#666',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            🗺️ Vue d'ensemble
          </button>
        </div>

        {/* Check Quality Tab */}
        {activeTab === 'check' && (
          <div style={{ marginTop: '1rem' }}>
            <form onSubmit={checkAirQuality}>
              <div className="form-group">
                <label>Sélectionnez une zone:</label>
                <select 
                  value={selectedZone} 
                  onChange={(e) => setSelectedZone(e.target.value)}
                  style={{ width: '100%', padding: '0.75rem', fontSize: '1rem' }}
                >
                  <option value="">-- Choisir une zone --</option>
                  {availableZones.map(zone => (
                    <option key={zone} value={zone}>{zone}</option>
                  ))}
                </select>
              </div>
              <button type="submit" className="btn btn-primary" disabled={loading || !selectedZone}>
                {loading ? '🔄 Vérification...' : '🔍 Vérifier la qualité'}
              </button>
            </form>

            {airQualityData && airQualityData.aqiValue > 0 && (
              <div style={{ marginTop: '2rem' }}>
                {/* AQI Card */}
                <div style={{
                  background: getAQIColor(airQualityData.aqiValue),
                  color: 'white',
                  padding: '2rem',
                  borderRadius: '12px',
                  marginBottom: '1.5rem',
                  textAlign: 'center'
                }}>
                  <div style={{ fontSize: '0.875rem', opacity: 0.9, marginBottom: '0.5rem' }}>
                    Indice de Qualité de l'Air (AQI)
                  </div>
                  <div style={{ fontSize: '4rem', fontWeight: 'bold', marginBottom: '0.5rem' }}>
                    {Math.round(airQualityData.aqiValue)}
                  </div>
                  <div style={{ fontSize: '1.5rem', fontWeight: '600', marginBottom: '0.5rem' }}>
                    {getAQILevel(airQualityData.aqiValue)}
                  </div>
                  <div style={{ fontSize: '1rem', opacity: 0.9 }}>
                    📍 {airQualityData.zoneName}
                  </div>
                </div>

                {/* Recommendation */}
                <div style={{
                  background: '#eff6ff',
                  border: '2px solid #3b82f6',
                  padding: '1rem',
                  borderRadius: '8px',
                  marginBottom: '1.5rem'
                }}>
                  <div style={{ fontWeight: '600', color: '#1e40af', marginBottom: '0.5rem' }}>
                    💡 Recommandation
                  </div>
                  <div style={{ color: '#1e3a8a' }}>
                    {getAQIRecommendation(airQualityData.aqiValue)}
                  </div>
                </div>

                {/* Pollutants Details */}
                <div>
                  <h3 style={{ marginBottom: '1rem' }}>🧪 Détails des Polluants</h3>
                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '1rem' }}>
                    {[
                      { name: 'PM2.5', value: airQualityData.pm25, unit: 'µg/m³', icon: '💨' },
                      { name: 'PM10', value: airQualityData.pm10, unit: 'µg/m³', icon: '🌪️' },
                      { name: 'NO₂', value: airQualityData.no2, unit: 'µg/m³', icon: '🏭' },
                      { name: 'O₃', value: airQualityData.o3, unit: 'µg/m³', icon: '☀️' },
                      { name: 'CO', value: airQualityData.co, unit: 'mg/m³', icon: '🚗' },
                      { name: 'SO₂', value: airQualityData.so2, unit: 'µg/m³', icon: '⚗️' }
                    ].map(pollutant => (
                      <div key={pollutant.name} style={{
                        padding: '1rem',
                        background: '#f9fafb',
                        borderRadius: '8px',
                        textAlign: 'center'
                      }}>
                        <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>{pollutant.icon}</div>
                        <div style={{ fontWeight: '600', marginBottom: '0.25rem' }}>{pollutant.name}</div>
                        <div style={{ fontSize: '1.25rem', color: '#667eea', fontWeight: 'bold' }}>
                          {pollutant.value?.toFixed(1) || '0.0'}
                        </div>
                        <div style={{ fontSize: '0.75rem', color: '#666' }}>{pollutant.unit}</div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            )}
          </div>
        )}

        {/* Compare Tab */}
        {activeTab === 'compare' && (
          <div style={{ marginTop: '1rem' }}>
            <form onSubmit={compareZones}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Zone 1:</label>
                  <select 
                    value={selectedZone} 
                    onChange={(e) => setSelectedZone(e.target.value)}
                    style={{ width: '100%', padding: '0.75rem' }}
                  >
                    <option value="">-- Choisir --</option>
                    {availableZones.map(zone => (
                      <option key={zone} value={zone}>{zone}</option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label>Zone 2:</label>
                  <select 
                    value={zone2} 
                    onChange={(e) => setZone2(e.target.value)}
                    style={{ width: '100%', padding: '0.75rem' }}
                  >
                    <option value="">-- Choisir --</option>
                    {availableZones.map(zone => (
                      <option key={zone} value={zone}>{zone}</option>
                    ))}
                  </select>
                </div>
              </div>
              <button 
                type="submit" 
                className="btn btn-secondary" 
                disabled={loading || !selectedZone || !zone2}
              >
                {loading ? '🔄 Comparaison...' : '📊 Comparer'}
              </button>
            </form>

            {comparisonResult && (
              <div style={{
                marginTop: '2rem',
                padding: '1.5rem',
                background: '#f0fdf4',
                border: '2px solid #10b981',
                borderRadius: '8px'
              }}>
                <div style={{ fontWeight: '600', color: '#047857', marginBottom: '0.5rem' }}>
                  📊 Résultat de la comparaison
                </div>
                <div style={{ color: '#065f46', fontSize: '1.125rem' }}>
                  {comparisonResult}
                </div>
              </div>
            )}
          </div>
        )}

        {/* Overview Tab */}
        {activeTab === 'overview' && (
          <div style={{ marginTop: '1rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
              <h3>Toutes les zones surveillées ({zones.length})</h3>
              <button onClick={loadAllZones} className="btn btn-secondary" disabled={loading}>
                {loading ? '🔄 Chargement...' : '🔄 Actualiser'}
              </button>
            </div>

            {loading ? (
              <div style={{ textAlign: 'center', padding: '3rem' }}>
                🔄 Chargement des données...
              </div>
            ) : zones.length > 0 ? (
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '1rem' }}>
                {zones.map((zone, idx) => (
                  <div 
                    key={idx}
                    style={{
                      padding: '1.5rem',
                      background: 'white',
                      border: `3px solid ${getAQIColor(zone.aqiValue)}`,
                      borderRadius: '12px',
                      boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                    }}
                  >
                    <div style={{ 
                      background: getAQIColor(zone.aqiValue),
                      color: 'white',
                      padding: '0.5rem 1rem',
                      borderRadius: '8px',
                      marginBottom: '1rem',
                      textAlign: 'center'
                    }}>
                      <div style={{ fontSize: '2rem', fontWeight: 'bold' }}>
                        {Math.round(zone.aqiValue)}
                      </div>
                      <div style={{ fontSize: '0.875rem' }}>
                        {getAQILevel(zone.aqiValue)}
                      </div>
                    </div>
                    
                    <div style={{ fontWeight: '600', fontSize: '1.125rem', marginBottom: '0.5rem' }}>
                      📍 {zone.zoneName}
                    </div>
                    
                    <div style={{ fontSize: '0.875rem', color: '#666' }}>
                      <div>PM2.5: {zone.pm25?.toFixed(1)} µg/m³</div>
                      <div>PM10: {zone.pm10?.toFixed(1)} µg/m³</div>
                      <div>NO₂: {zone.no2?.toFixed(1)} µg/m³</div>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div style={{ textAlign: 'center', padding: '3rem', background: '#fef3c7', borderRadius: '8px' }}>
                <div style={{ fontSize: '3rem', marginBottom: '0.5rem' }}>⚠️</div>
                <div style={{ fontSize: '1.125rem', fontWeight: '600', marginBottom: '0.5rem' }}>
                  Aucune zone trouvée
                </div>
                <div style={{ fontSize: '0.875rem', color: '#666' }}>
                  Cliquez sur Actualiser pour recharger les données
                </div>
              </div>
            )}
          </div>
        )}
      </div>

      {/* AQI Scale */}
      <div className="card">
        <h3>📈 Échelle AQI</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(120px, 1fr))', gap: '0.5rem', marginTop: '1rem' }}>
          {[
            { range: '0-50', level: 'Bon', color: '#10b981' },
            { range: '51-100', level: 'Modéré', color: '#fbbf24' },
            { range: '101-150', level: 'Sensible', color: '#f97316' },
            { range: '151-200', level: 'Mauvais', color: '#ef4444' },
            { range: '201+', level: 'Dangereux', color: '#7f1d1d' }
          ].map(item => (
            <div 
              key={item.range}
              style={{
                background: item.color,
                color: 'white',
                padding: '0.75rem',
                borderRadius: '8px',
                textAlign: 'center'
              }}
            >
              <div style={{ fontWeight: 'bold' }}>{item.range}</div>
              <div style={{ fontSize: '0.75rem' }}>{item.level}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AirQualityService;