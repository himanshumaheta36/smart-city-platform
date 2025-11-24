import React, { useState } from 'react';
import { airQualityAPI } from '../services/api';
import '../App.css';

const AirQualityService = () => {
  const [location, setLocation] = useState('');
  const [location2, setLocation2] = useState('');
  const [airQuality, setAirQuality] = useState(null);
  const [comparison, setComparison] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleGetAirQuality = async (e) => {
    e.preventDefault();
    if (!location) return;

    setLoading(true);
    try {
      const response = await airQualityAPI.getAirQuality(location);
      setAirQuality(response.data);
    } catch (error) {
      console.error('Error fetching air quality:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCompare = async (e) => {
    e.preventDefault();
    if (!location || !location2) return;

    setLoading(true);
    try {
      const response = await airQualityAPI.compareAirQuality(location, location2);
      setComparison(response.data);
    } catch (error) {
      console.error('Error comparing air quality:', error);
    } finally {
      setLoading(false);
    }
  };

  const getAQIColor = (aqi) => {
    if (aqi <= 50) return '#00e400';
    if (aqi <= 100) return '#ffff00';
    if (aqi <= 150) return '#ff7e00';
    if (aqi <= 200) return '#ff0000';
    return '#99004c';
  };

  const getAQILevel = (aqi) => {
    if (aqi <= 50) return 'Bon';
    if (aqi <= 100) return 'Modéré';
    if (aqi <= 150) return 'Mauvais pour les groupes sensibles';
    if (aqi <= 200) return 'Mauvais';
    return 'Très mauvais';
  };

  return (
    <div>
      <div className="card">
        <h2>🌫️ Service Qualité de l'Air (SOAP)</h2>
        <p>Consultez les indices de pollution et les niveaux de qualité de l'air par zone.</p>
      </div>

      <div className="card">
        <h3>🔍 Qualité de l'Air par Zone</h3>
        <form onSubmit={handleGetAirQuality}>
          <div className="form-group">
            <label>Zone géographique:</label>
            <input
              type="text"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              placeholder="Ex: Centre-ville, Quartier Nord..."
            />
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Chargement...' : 'Vérifier la qualité'}
          </button>
        </form>

        {airQuality && (
          <div style={{ 
            marginTop: '1rem', 
            padding: '1rem', 
            background: getAQIColor(airQuality.aqi || 50),
            color: 'white',
            borderRadius: '8px',
            textAlign: 'center'
          }}>
            <h3>Indice AQI: {airQuality.aqi || 'N/A'}</h3>
            <p>Niveau: {getAQILevel(airQuality.aqi || 50)}</p>
            <p>Polluant principal: {airQuality.mainPollutant || 'N/A'}</p>
          </div>
        )}
      </div>

      <div className="card">
        <h3>📊 Comparaison de Zones</h3>
        <form onSubmit={handleCompare}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div className="form-group">
              <label>Zone 1:</label>
              <input
                type="text"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                placeholder="Ex: Centre-ville"
              />
            </div>
            <div className="form-group">
              <label>Zone 2:</label>
              <input
                type="text"
                value={location2}
                onChange={(e) => setLocation2(e.target.value)}
                placeholder="Ex: Quartier Industriel"
              />
            </div>
          </div>
          <button type="submit" className="btn btn-secondary" disabled={loading}>
            {loading ? 'Comparaison...' : 'Comparer'}
          </button>
        </form>

        {comparison && (
          <div style={{ marginTop: '1rem' }}>
            <h4>Résultats de la comparaison:</h4>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
              <div style={{ 
                padding: '1rem', 
                background: getAQIColor(comparison.zone1Aqi || 50),
                color: 'white',
                borderRadius: '8px',
                textAlign: 'center'
              }}>
                <strong>{comparison.zone1}</strong>
                <p>AQI: {comparison.zone1Aqi || 'N/A'}</p>
              </div>
              <div style={{ 
                padding: '1rem', 
                background: getAQIColor(comparison.zone2Aqi || 50),
                color: 'white',
                borderRadius: '8px',
                textAlign: 'center'
              }}>
                <strong>{comparison.zone2}</strong>
                <p>AQI: {comparison.zone2Aqi || 'N/A'}</p>
              </div>
            </div>
          </div>
        )}
      </div>

      <div className="card">
        <h3>📈 Échelle AQI</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(5, 1fr)', gap: '0.5rem', textAlign: 'center' }}>
          <div style={{ background: '#00e400', padding: '0.5rem', borderRadius: '4px' }}>0-50<br />Bon</div>
          <div style={{ background: '#ffff00', padding: '0.5rem', borderRadius: '4px' }}>51-100<br />Modéré</div>
          <div style={{ background: '#ff7e00', padding: '0.5rem', borderRadius: '4px' }}>101-150<br />Sensible</div>
          <div style={{ background: '#ff0000', padding: '0.5rem', borderRadius: '4px' }}>151-200<br />Mauvais</div>
          <div style={{ background: '#99004c', padding: '0.5rem', borderRadius: '4px', color: 'white' }}>201+<br />Dangereux</div>
        </div>
      </div>
    </div>
  );
};

export default AirQualityService;