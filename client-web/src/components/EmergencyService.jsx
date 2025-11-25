import React, { useState, useEffect } from 'react';
import { emergencyAPI } from '../services/api';
import '../App.css';

const EmergencyService = () => {
  const [alerts, setAlerts] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [activeTab, setActiveTab] = useState('create');

  // Form state
  const [formData, setFormData] = useState({
    reporterId: 'citizen-' + Math.floor(Math.random() * 10000),
    emergencyType: 'ACCIDENT',
    severityLevel: 'MEDIUM',
    location: '',
    latitude: 48.8566,
    longitude: 2.3522,
    description: '',
    affectedPeople: 1,
    tags: []
  });

  useEffect(() => {
    loadAlerts();
    loadStats();
  }, []);

  const loadAlerts = async () => {
    try {
      const response = await emergencyAPI.getAlerts();
      setAlerts(response.data || []);
    } catch (error) {
      console.error('Erreur chargement alertes:', error);
    }
  };

  const loadStats = async () => {
    try {
      const response = await emergencyAPI.getStats(24);
      setStats(response.data || {});
    } catch (error) {
      console.error('Erreur chargement stats:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.location || !formData.description) {
      setMessage('⚠️ Veuillez remplir tous les champs obligatoires');
      return;
    }

    setLoading(true);
    try {
      await emergencyAPI.createAlert(formData);
      setMessage('✅ Alerte envoyée avec succès!');
      
      // Reset form
      setFormData({
        ...formData,
        location: '',
        description: '',
        affectedPeople: 1,
        tags: []
      });
      
      // Reload alerts
      await loadAlerts();
      await loadStats();
      
      setTimeout(() => setMessage(''), 5000);
    } catch (error) {
      setMessage('❌ Erreur lors de l\'envoi de l\'alerte');
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const getTypeIcon = (type) => {
    const icons = {
      ACCIDENT: '🚗',
      FIRE: '🔥',
      MEDICAL: '🏥',
      SECURITY: '👮',
      NATURAL_DISASTER: '🌪️',
      TECHNICAL: '⚡'
    };
    return icons[type] || '⚠️';
  };

  const getTypeColor = (type) => {
    const colors = {
      ACCIDENT: '#dc3545',
      FIRE: '#fd7e14',
      MEDICAL: '#0d6efd',
      SECURITY: '#6f42c1',
      NATURAL_DISASTER: '#198754',
      TECHNICAL: '#ffc107'
    };
    return colors[type] || '#6c757d';
  };

  const getSeverityColor = (severity) => {
    const colors = {
      LOW: '#10b981',
      MEDIUM: '#f59e0b',
      HIGH: '#ef4444',
      CRITICAL: '#7f1d1d'
    };
    return colors[severity] || '#6b7280';
  };

  const getStatusColor = (status) => {
    const colors = {
      REPORTED: '#6b7280',
      CONFIRMED: '#3b82f6',
      IN_PROGRESS: '#f59e0b',
      RESOLVED: '#10b981',
      CANCELLED: '#ef4444'
    };
    return colors[status] || '#6b7280';
  };

  return (
    <div>
      {/* Header */}
      <div className="card" style={{ 
        background: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
        color: 'white'
      }}>
        <h2 style={{ marginBottom: '0.5rem' }}>🚨 Service d'Urgences</h2>
        <p style={{ opacity: 0.9, marginBottom: '1rem' }}>
          gRPC - Alertes et gestion de crises en temps réel
        </p>
        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Protocole:</strong> gRPC (REST Adapter)
          </div>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Ports:</strong> 8083 (REST) • 9090 (gRPC)
          </div>
          <div style={{ background: 'rgba(255,255,255,0.2)', padding: '0.5rem 1rem', borderRadius: '8px' }}>
            <strong>Endpoint:</strong> /api/emergency
          </div>
        </div>
      </div>

      {/* Stats Dashboard */}
      {stats && (
        <div className="card">
          <h3 style={{ marginBottom: '1rem' }}>📊 Statistiques (Dernières 24h)</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
            <div style={{
              padding: '1.5rem',
              background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              color: 'white',
              borderRadius: '12px',
              textAlign: 'center'
            }}>
              <div style={{ fontSize: '2.5rem', fontWeight: 'bold' }}>
                {stats.totalEmergencies || 0}
              </div>
              <div style={{ fontSize: '0.875rem', opacity: 0.9 }}>Total Urgences</div>
            </div>
            
            <div style={{
              padding: '1.5rem',
              background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
              color: 'white',
              borderRadius: '12px',
              textAlign: 'center'
            }}>
              <div style={{ fontSize: '2.5rem', fontWeight: 'bold' }}>
                {stats.activeEmergencies || 0}
              </div>
              <div style={{ fontSize: '0.875rem', opacity: 0.9 }}>Urgences Actives</div>
            </div>
            
            <div style={{
              padding: '1.5rem',
              background: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
              color: 'white',
              borderRadius: '12px',
              textAlign: 'center'
            }}>
              <div style={{ fontSize: '2.5rem', fontWeight: 'bold' }}>
                {stats.averageResponseTimeMinutes?.toFixed(1) || '0'}
              </div>
              <div style={{ fontSize: '0.875rem', opacity: 0.9 }}>Temps Réponse (min)</div>
            </div>
          </div>
        </div>
      )}

      {/* Tabs */}
      <div className="card">
        <div style={{ display: 'flex', gap: '0.5rem', borderBottom: '2px solid #e5e7eb', paddingBottom: '0.5rem' }}>
          <button
            onClick={() => setActiveTab('create')}
            style={{
              padding: '0.5rem 1rem',
              background: activeTab === 'create' ? '#fa709a' : 'transparent',
              color: activeTab === 'create' ? 'white' : '#666',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            🆘 Créer Alerte
          </button>
          <button
            onClick={() => setActiveTab('alerts')}
            style={{
              padding: '0.5rem 1rem',
              background: activeTab === 'alerts' ? '#fa709a' : 'transparent',
              color: activeTab === 'alerts' ? 'white' : '#666',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            📋 Alertes Actives
          </button>
        </div>

        {/* Create Alert Tab */}
        {activeTab === 'create' && (
          <div style={{ marginTop: '1rem' }}>
            <form onSubmit={handleSubmit}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Type d'urgence *</label>
                  <select 
                    value={formData.emergencyType}
                    onChange={(e) => setFormData({...formData, emergencyType: e.target.value})}
                  >
                    <option value="ACCIDENT">🚗 Accident</option>
                    <option value="FIRE">🔥 Incendie</option>
                    <option value="MEDICAL">🏥 Urgence médicale</option>
                    <option value="SECURITY">👮 Sécurité</option>
                    <option value="NATURAL_DISASTER">🌪️ Catastrophe naturelle</option>
                    <option value="TECHNICAL">⚡ Problème technique</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Niveau de sévérité *</label>
                  <select 
                    value={formData.severityLevel}
                    onChange={(e) => setFormData({...formData, severityLevel: e.target.value})}
                  >
                    <option value="LOW">🟢 Faible</option>
                    <option value="MEDIUM">🟡 Moyen</option>
                    <option value="HIGH">🟠 Élevé</option>
                    <option value="CRITICAL">🔴 Critique</option>
                  </select>
                </div>
              </div>

              <div className="form-group">
                <label>Localisation exacte *</label>
                <input
                  type="text"
                  value={formData.location}
                  onChange={(e) => setFormData({...formData, location: e.target.value})}
                  placeholder="Ex: Rue principale, intersection..."
                  required
                />
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Latitude</label>
                  <input
                    type="number"
                    step="0.000001"
                    value={formData.latitude}
                    onChange={(e) => setFormData({...formData, latitude: parseFloat(e.target.value)})}
                  />
                </div>

                <div className="form-group">
                  <label>Longitude</label>
                  <input
                    type="number"
                    step="0.000001"
                    value={formData.longitude}
                    onChange={(e) => setFormData({...formData, longitude: parseFloat(e.target.value)})}
                  />
                </div>
              </div>

              <div className="form-group">
                <label>Description détaillée *</label>
                <textarea
                  value={formData.description}
                  onChange={(e) => setFormData({...formData, description: e.target.value})}
                  placeholder="Décrivez la situation d'urgence en détail..."
                  rows="4"
                  required
                />
              </div>

              <div className="form-group">
                <label>Nombre de personnes affectées</label>
                <input
                  type="number"
                  min="1"
                  value={formData.affectedPeople}
                  onChange={(e) => setFormData({...formData, affectedPeople: parseInt(e.target.value)})}
                />
              </div>

              <button type="submit" className="btn btn-primary" disabled={loading} style={{ width: '100%' }}>
                {loading ? '🔄 Envoi en cours...' : '🚨 Envoyer l\'alerte d\'urgence'}
              </button>
            </form>

            {message && (
              <div className={message.includes('✅') ? 'success' : message.includes('⚠️') ? 'error' : 'error'} style={{ marginTop: '1rem' }}>
                {message}
              </div>
            )}
          </div>
        )}

        {/* Alerts Tab */}
        {activeTab === 'alerts' && (
          <div style={{ marginTop: '1rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
              <h3>Alertes Récentes ({alerts.length})</h3>
              <button onClick={loadAlerts} className="btn btn-secondary">
                🔄 Actualiser
              </button>
            </div>

            {alerts.length > 0 ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                {alerts.map((alert) => (
                  <div 
                    key={alert.emergencyId}
                    style={{
                      padding: '1.5rem',
                      borderLeft: `5px solid ${getTypeColor(alert.emergencyType)}`,
                      background: '#f9fafb',
                      borderRadius: '8px',
                      boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                    }}
                  >
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: '1rem' }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                        <div style={{ fontSize: '2rem' }}>
                          {getTypeIcon(alert.emergencyType)}
                        </div>
                        <div>
                          <div style={{ fontWeight: '600', fontSize: '1.125rem' }}>
                            {alert.emergencyType}
                          </div>
                          <div style={{ fontSize: '0.875rem', color: '#666' }}>
                            ID: {alert.emergencyId}
                          </div>
                        </div>
                      </div>
                      
                      <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap', justifyContent: 'flex-end' }}>
                        <span style={{
                          background: getSeverityColor(alert.severityLevel),
                          color: 'white',
                          padding: '0.25rem 0.75rem',
                          borderRadius: '12px',
                          fontSize: '0.75rem',
                          fontWeight: '600'
                        }}>
                          {alert.severityLevel}
                        </span>
                        <span style={{
                          background: getStatusColor(alert.status),
                          color: 'white',
                          padding: '0.25rem 0.75rem',
                          borderRadius: '12px',
                          fontSize: '0.75rem',
                          fontWeight: '600'
                        }}>
                          {alert.status}
                        </span>
                      </div>
                    </div>
                    
                    <div style={{ marginBottom: '0.75rem' }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.5rem' }}>
                        <span style={{ fontSize: '1rem' }}>📍</span>
                        <span style={{ fontWeight: '600' }}>{alert.location}</span>
                      </div>
                      <p style={{ color: '#666', lineHeight: '1.5' }}>
                        {alert.description}
                      </p>
                    </div>
                    
                    <div style={{ display: 'flex', gap: '1.5rem', fontSize: '0.875rem', color: '#666', flexWrap: 'wrap' }}>
                      <span>👥 {alert.affectedPeople} personne(s)</span>
                      <span>🕐 {new Date(alert.createdAt).toLocaleString('fr-FR')}</span>
                      {alert.responderId && (
                        <span>👨‍🚒 Intervenant: {alert.responderId}</span>
                      )}
                    </div>
                    
                    {alert.tags && alert.tags.length > 0 && (
                      <div style={{ marginTop: '0.75rem', display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                        {alert.tags.map((tag, idx) => (
                          <span 
                            key={idx}
                            style={{
                              background: '#e5e7eb',
                              padding: '0.25rem 0.5rem',
                              borderRadius: '12px',
                              fontSize: '0.75rem'
                            }}
                          >
                            #{tag}
                          </span>
                        ))}
                      </div>
                    )}
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
                <div style={{ fontSize: '1.125rem', fontWeight: '600' }}>Aucune alerte active</div>
                <div style={{ fontSize: '0.875rem', marginTop: '0.5rem' }}>Tout est calme pour le moment</div>
              </div>
            )}
          </div>
        )}
      </div>

      {/* gRPC Advantages */}
      <div className="card" style={{ background: '#f0fdf4', border: '2px solid #10b981' }}>
        <h3>⚡ Avantages de gRPC pour les Urgences</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1rem', marginTop: '1rem' }}>
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>🚀</div>
            <div style={{ fontWeight: '600', marginBottom: '0.25rem' }}>Ultra-rapide</div>
            <div style={{ fontSize: '0.875rem', color: '#666' }}>
              Communication binaire avec Protocol Buffers pour une latence minimale
            </div>
          </div>
          
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>🔄</div>
            <div style={{ fontWeight: '600', marginBottom: '0.25rem' }}>Streaming bidirectionnel</div>
            <div style={{ fontSize: '0.875rem', color: '#666' }}>
              Flux en temps réel pour le suivi des urgences
            </div>
          </div>
          
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>🔒</div>
            <div style={{ fontWeight: '600', marginBottom: '0.25rem' }}>Sécurisé</div>
            <div style={{ fontSize: '0.875rem', color: '#666' }}>
              TLS/SSL intégré et authentification forte
            </div>
          </div>
          
          <div style={{ padding: '1rem', background: 'white', borderRadius: '8px' }}>
            <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>📊</div>
            <div style={{ fontWeight: '600', marginBottom: '0.25rem' }}>Fortement typé</div>
            <div style={{ fontSize: '0.875rem', color: '#666' }}>
              Contrat d'API strict avec définition Protocol Buffer
            </div>
          </div>
        </div>
      </div>

      {/* Documentation */}
      <div className="card" style={{ background: '#fffbeb', border: '2px solid #fcd34d' }}>
        <h3>📚 Documentation gRPC</h3>
        <div style={{ marginTop: '1rem' }}>
          <div style={{ marginBottom: '1rem' }}>
            <strong>Endpoints REST disponibles (adaptateur gRPC):</strong>
          </div>
          <code style={{ 
            display: 'block', 
            background: 'white', 
            padding: '1rem', 
            borderRadius: '6px',
            fontSize: '0.875rem',
            whiteSpace: 'pre-wrap'
          }}>
{`POST   /api/emergency              - Créer une alerte
GET    /api/emergency              - Liste des alertes
GET    /api/emergency/{id}         - Détails d'une alerte
PUT    /api/emergency/{id}/status  - Modifier le statut
GET    /api/emergency/stats        - Statistiques`}
          </code>
          <div style={{ marginTop: '1rem', fontSize: '0.875rem', color: '#666' }}>
            <strong>Note:</strong> Le service utilise gRPC en interne (port 9090) avec un adaptateur REST pour la compatibilité web.
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmergencyService;