import React, { useState, useEffect } from 'react';
import { mobilityAPI } from '../services/api';
import '../App.css';

const MobilityService = () => {
  const [transportLines, setTransportLines] = useState([]);
  const [selectedLine, setSelectedLine] = useState(null);
  const [schedules, setSchedules] = useState([]);
  const [trafficInfo, setTrafficInfo] = useState([]);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('lines');

  useEffect(() => {
    loadTransportLines();
    loadTrafficInfo();
  }, []);

  const loadTransportLines = async () => {
    setLoading(true);
    try {
      const response = await mobilityAPI.getTransportLines();
      setTransportLines(response.data || []);
    } catch (error) {
      console.error('Erreur chargement lignes:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadTrafficInfo = async () => {
    try {
      const response = await mobilityAPI.getTrafficInfo();
      setTrafficInfo(response.data || []);
    } catch (error) {
      console.error('Erreur chargement trafic:', error);
    }
  };

  const handleLineSelect = async (line) => {
    setSelectedLine(line);
    setLoading(true);
    try {
      const response = await mobilityAPI.getSchedulesByLine(line.lineNumber);
      setSchedules(response.data || []);
    } catch (error) {
      console.error('Erreur chargement horaires:', error);
      setSchedules([]);
    } finally {
      setLoading(false);
    }
  };

  const getLineTypeColor = (type) => {
    switch (type) {
      case 'BUS': return '#f59e0b';
      case 'METRO': return '#3b82f6';
      case 'TRAIN': return '#10b981';
      default: return '#6b7280';
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'ACTIVE': return '#10b981';
      case 'DELAYED': return '#f59e0b';
      case 'INACTIVE': return '#ef4444';
      default: return '#6b7280';
    }
  };

  const getSeverityColor = (severity) => {
    switch (severity) {
      case 'LOW': return '#10b981';
      case 'MEDIUM': return '#f59e0b';
      case 'HIGH': return '#ef4444';
      case 'CRITICAL': return '#7f1d1d';
      default: return '#6b7280';
    }
  };

  return (
    <div>
      {/* Header */}
      <div className="card" style={{ 
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white'
      }}>
        <h2 style={{ marginBottom: '0.5rem' }}>🚗 Service de Mobilité Intelligente</h2>
        <p style={{ opacity: 0.9, marginBottom: '1rem' }}>
          API REST - Gestion des transports publics urbains
        </p>
        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Protocole:</strong> REST API
          </div>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Port:</strong> 8081
          </div>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Endpoint:</strong> /api/mobility
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="card">
        <div style={{ display: 'flex', gap: '0.5rem', borderBottom: '2px solid #e5e7eb', paddingBottom: '0.5rem' }}>
          <button
            onClick={() => setActiveTab('lines')}
            style={{
              padding: '0.5rem 1rem',
              background: activeTab === 'lines' ? '#667eea' : 'transparent',
              color: activeTab === 'lines' ? 'white' : '#666',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            🚌 Lignes de Transport
          </button>
          <button
            onClick={() => setActiveTab('traffic')}
            style={{
              padding: '0.5rem 1rem',
              background: activeTab === 'traffic' ? '#667eea' : 'transparent',
              color: activeTab === 'traffic' ? 'white' : '#666',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            🚦 Info Trafic
          </button>
        </div>

        {/* Transport Lines Tab */}
        {activeTab === 'lines' && (
          <div style={{ marginTop: '1rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
              <h3>Lignes Disponibles ({transportLines.length})</h3>
              <button onClick={loadTransportLines} className="btn btn-secondary" disabled={loading}>
                {loading ? '🔄 Chargement...' : '🔄 Actualiser'}
              </button>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '1rem' }}>
              {transportLines.map((line) => (
                <div
                  key={line.id}
                  onClick={() => handleLineSelect(line)}
                  style={{
                    padding: '1rem',
                    border: selectedLine?.id === line.id ? '2px solid #667eea' : '1px solid #e5e7eb',
                    borderRadius: '8px',
                    cursor: 'pointer',
                    background: selectedLine?.id === line.id ? '#f3f4f6' : 'white',
                    transition: 'all 0.2s ease'
                  }}
                  onMouseEnter={(e) => e.currentTarget.style.boxShadow = '0 4px 6px rgba(0,0,0,0.1)'}
                  onMouseLeave={(e) => e.currentTarget.style.boxShadow = 'none'}
                >
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: '0.5rem' }}>
                    <div style={{ 
                      background: getLineTypeColor(line.transportType),
                      color: 'white',
                      padding: '0.25rem 0.75rem',
                      borderRadius: '12px',
                      fontSize: '0.875rem',
                      fontWeight: '600'
                    }}>
                      {line.lineNumber}
                    </div>
                    <div style={{
                      width: '10px',
                      height: '10px',
                      borderRadius: '50%',
                      background: getStatusColor(line.status),
                      boxShadow: `0 0 8px ${getStatusColor(line.status)}`
                    }} />
                  </div>
                  
                  <div style={{ fontWeight: '600', marginBottom: '0.5rem' }}>
                    {line.description}
                  </div>
                  
                  <div style={{ fontSize: '0.875rem', color: '#666', marginBottom: '0.5rem' }}>
                    {line.transportType} • {line.status}
                  </div>
                  
                  {line.delayMinutes > 0 && (
                    <div style={{ 
                      background: '#fef3c7',
                      color: '#92400e',
                      padding: '0.25rem 0.5rem',
                      borderRadius: '4px',
                      fontSize: '0.75rem',
                      marginBottom: '0.5rem'
                    }}>
                      ⏱️ Retard: {line.delayMinutes} min
                    </div>
                  )}
                  
                  <div style={{ fontSize: '0.75rem', color: '#999' }}>
                    Stations: {line.stations?.length || 0}
                  </div>
                </div>
              ))}
            </div>

            {/* Schedules */}
            {selectedLine && (
              <div style={{ marginTop: '2rem', padding: '1rem', background: '#f9fafb', borderRadius: '8px' }}>
                <h3 style={{ marginBottom: '1rem' }}>
                  📅 Horaires - {selectedLine.lineNumber}
                </h3>
                
                {loading ? (
                  <div style={{ textAlign: 'center', padding: '2rem' }}>
                    🔄 Chargement des horaires...
                  </div>
                ) : schedules.length > 0 ? (
                  <div style={{ overflowX: 'auto' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                      <thead>
                        <tr style={{ background: '#e5e7eb' }}>
                          <th style={{ padding: '0.75rem', textAlign: 'left' }}>Station</th>
                          <th style={{ padding: '0.75rem', textAlign: 'left' }}>Arrivée</th>
                          <th style={{ padding: '0.75rem', textAlign: 'left' }}>Départ</th>
                          <th style={{ padding: '0.75rem', textAlign: 'left' }}>Type de jour</th>
                        </tr>
                      </thead>
                      <tbody>
                        {schedules.map((schedule, idx) => (
                          <tr key={idx} style={{ borderBottom: '1px solid #e5e7eb' }}>
                            <td style={{ padding: '0.75rem' }}>{schedule.station}</td>
                            <td style={{ padding: '0.75rem' }}>{schedule.arrivalTime}</td>
                            <td style={{ padding: '0.75rem' }}>{schedule.departureTime}</td>
                            <td style={{ padding: '0.75rem' }}>
                              <span style={{
                                background: '#dbeafe',
                                padding: '0.25rem 0.5rem',
                                borderRadius: '4px',
                                fontSize: '0.75rem'
                              }}>
                                {schedule.dayType}
                              </span>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                ) : (
                  <div style={{ textAlign: 'center', padding: '2rem', color: '#666' }}>
                    Aucun horaire disponible
                  </div>
                )}
              </div>
            )}
          </div>
        )}

        {/* Traffic Info Tab */}
        {activeTab === 'traffic' && (
          <div style={{ marginTop: '1rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
              <h3>Incidents de Trafic Actifs ({trafficInfo.length})</h3>
              <button onClick={loadTrafficInfo} className="btn btn-secondary">
                🔄 Actualiser
              </button>
            </div>

            {trafficInfo.length > 0 ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                {trafficInfo.map((incident) => (
                  <div 
                    key={incident.id}
                    style={{
                      padding: '1rem',
                      borderLeft: `4px solid ${getSeverityColor(incident.severity)}`,
                      background: '#f9fafb',
                      borderRadius: '4px'
                    }}
                  >
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: '0.5rem' }}>
                      <div style={{ fontWeight: '600', fontSize: '1.125rem' }}>
                        📍 {incident.location}
                      </div>
                      <div style={{
                        background: getSeverityColor(incident.severity),
                        color: 'white',
                        padding: '0.25rem 0.75rem',
                        borderRadius: '12px',
                        fontSize: '0.75rem',
                        fontWeight: '600'
                      }}>
                        {incident.severity}
                      </div>
                    </div>
                    
                    <p style={{ color: '#666', marginBottom: '0.5rem' }}>
                      {incident.description}
                    </p>
                    
                    <div style={{ display: 'flex', gap: '1rem', fontSize: '0.875rem', color: '#999' }}>
                      <span>Type: {incident.incidentType}</span>
                      <span>Signalé: {new Date(incident.reportedAt).toLocaleString('fr-FR')}</span>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div style={{ 
                textAlign: 'center', 
                padding: '3rem',
                background: '#ecfdf5',
                borderRadius: '8px',
                color: '#059669'
              }}>
                <div style={{ fontSize: '3rem', marginBottom: '0.5rem' }}>✅</div>
                <div style={{ fontSize: '1.125rem', fontWeight: '600' }}>Aucun incident actif</div>
                <div style={{ fontSize: '0.875rem', marginTop: '0.5rem' }}>Le trafic est fluide dans toute la ville</div>
              </div>
            )}
          </div>
        )}
      </div>

      {/* API Info */}
      <div className="card" style={{ background: '#fffbeb', border: '2px solid #fcd34d' }}>
        <h3>📚 Documentation API REST</h3>
        <div style={{ marginTop: '1rem' }}>
          <div style={{ marginBottom: '1rem' }}>
            <strong>Endpoints disponibles:</strong>
          </div>
          <code style={{ 
            display: 'block', 
            background: 'white', 
            padding: '1rem', 
            borderRadius: '6px',
            fontSize: '0.875rem',
            whiteSpace: 'pre-wrap'
          }}>
{`GET  /api/mobility/transport-lines
GET  /api/mobility/transport-lines/number/{lineNumber}
GET  /api/mobility/transport-lines/type/{type}
GET  /api/mobility/schedules/line/{lineNumber}
GET  /api/mobility/traffic-info/active
GET  /api/mobility/traffic-info/severity/{severity}`}
          </code>
          <a 
            href="http://localhost:8081/mobility/swagger-ui.html" 
            target="_blank" 
            rel="noopener noreferrer"
            className="btn btn-primary"
            style={{ marginTop: '1rem', display: 'inline-block' }}
          >
            📖 Voir Swagger UI
          </a>
        </div>
      </div>
    </div>
  );
};

export default MobilityService;