import React, { useState, useEffect } from 'react';
import { mobilityAPI } from '../services/api';
import '../App.css';

const MobilityService = () => {
  const [transportOptions, setTransportOptions] = useState([]);
  const [trafficStatus, setTrafficStatus] = useState({});
  const [selectedLine, setSelectedLine] = useState('');
  const [schedules, setSchedules] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadInitialData();
  }, []);

  const loadInitialData = async () => {
    try {
      setLoading(true);
      const [transportResponse, trafficResponse] = await Promise.all([
        mobilityAPI.getTransportOptions(),
        mobilityAPI.getTrafficStatus()
      ]);
      
      setTransportOptions(transportResponse.data || []);
      setTrafficStatus(trafficResponse.data || {});
    } catch (error) {
      console.error('Error loading mobility data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLineSelect = async (line) => {
    setSelectedLine(line);
    try {
      const response = await mobilityAPI.getSchedules(line);
      setSchedules(response.data || []);
    } catch (error) {
      console.error('Error loading schedules:', error);
    }
  };

  return (
    <div>
      <div className="card">
        <h2>🚗 Service de Mobilité Intelligente (REST)</h2>
        <p>Consultez les horaires des transports et l'état du trafic en temps réel.</p>
      </div>

      {loading && <div className="loading">Chargement des données de mobilité...</div>}

      <div className="card">
        <h3>🚍 Transports Disponibles</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
          {transportOptions.map((option, index) => (
            <div 
              key={index}
              style={{
                padding: '1rem',
                border: '1px solid #e1e5e9',
                borderRadius: '8px',
                cursor: 'pointer',
                background: selectedLine === option.line ? '#667eea' : '#f8f9fa',
                color: selectedLine === option.line ? 'white' : 'inherit'
              }}
              onClick={() => handleLineSelect(option.line)}
            >
              <strong>{option.type}</strong>
              <p>{option.line}</p>
              <small>Status: {option.status}</small>
            </div>
          ))}
        </div>
      </div>

      {selectedLine && (
        <div className="card">
          <h3>🕒 Horaires - Ligne {selectedLine}</h3>
          {schedules.length > 0 ? (
            <ul>
              {schedules.map((schedule, index) => (
                <li key={index} style={{ padding: '0.5rem 0', borderBottom: '1px solid #eee' }}>
                  {schedule.time} - {schedule.destination} 
                  {schedule.delay && <span style={{ color: 'red', marginLeft: '1rem' }}>Retard: {schedule.delay}</span>}
                </li>
              ))}
            </ul>
          ) : (
            <p>Aucun horaire disponible</p>
          )}
        </div>
      )}

      <div className="card">
        <h3>🚦 État du Trafic</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '1rem' }}>
          <div style={{ textAlign: 'center', padding: '1rem', background: '#d4edda', borderRadius: '8px' }}>
            <h4>✅ Fluid</h4>
            <p>75% des routes</p>
          </div>
          <div style={{ textAlign: 'center', padding: '1rem', background: '#fff3cd', borderRadius: '8px' }}>
            <h4>⚠️ Ralenti</h4>
            <p>20% des routes</p>
          </div>
          <div style={{ textAlign: 'center', padding: '1rem', background: '#f8d7da', borderRadius: '8px' }}>
            <h4>🚨 Bloqué</h4>
            <p>5% des routes</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MobilityService;