import React, { useState } from 'react';
import { emergencyAPI } from '../services/api';
import '../App.css';

const EmergencyService = () => {
  const [alertType, setAlertType] = useState('accident');
  const [location, setLocation] = useState('');
  const [description, setDescription] = useState('');
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const handleSendAlert = async (e) => {
    e.preventDefault();
    if (!location || !description) {
      setMessage('Veuillez remplir tous les champs');
      return;
    }

    setLoading(true);
    try {
      const alertData = {
        type: alertType,
        location: location,
        description: description,
        timestamp: new Date().toISOString(),
        status: 'pending'
      };

      const response = await emergencyAPI.sendAlert(alertData);
      setMessage('✅ Alerte envoyée avec succès!');
      
      // Réinitialiser le formulaire
      setLocation('');
      setDescription('');
      
      // Recharger la liste des alertes
      loadAlerts();
    } catch (error) {
      setMessage('❌ Erreur lors de l\'envoi de l\'alerte');
      console.error('Error sending alert:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadAlerts = async () => {
    try {
      const response = await emergencyAPI.getAlerts();
      setAlerts(response.data || []);
    } catch (error) {
      console.error('Error loading alerts:', error);
    }
  };

  React.useEffect(() => {
    loadAlerts();
  }, []);

  const getAlertColor = (type) => {
    switch (type) {
      case 'accident': return '#dc3545';
      case 'fire': return '#fd7e14';
      case 'medical': return '#0d6efd';
      case 'security': return '#6f42c1';
      default: return '#6c757d';
    }
  };

  const getAlertIcon = (type) => {
    switch (type) {
      case 'accident': return '🚗';
      case 'fire': return '🔥';
      case 'medical': return '🏥';
      case 'security': return '👮';
      default: return '⚠️';
    }
  };

  return (
    <div>
      <div className="card">
        <h2>🚨 Service d'Urgences (gRPC)</h2>
        <p>Service haute performance pour la gestion des alertes en temps réel.</p>
      </div>

      <div className="card">
        <h3>🆘 Envoyer une Alerte</h3>
        <form onSubmit={handleSendAlert}>
          <div className="form-group">
            <label>Type d'urgence:</label>
            <select 
              value={alertType} 
              onChange={(e) => setAlertType(e.target.value)}
              className="form-group"
            >
              <option value="accident">Accident</option>
              <option value="fire">Incendie</option>
              <option value="medical">Urgence médicale</option>
              <option value="security">Problème de sécurité</option>
            </select>
          </div>

          <div className="form-group">
            <label>Localisation exacte:</label>
            <input
              type="text"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              placeholder="Ex: Rue principale, intersection..."
            />
          </div>

          <div className="form-group">
            <label>Description détaillée:</label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Décrivez la situation d'urgence..."
              rows="3"
            />
          </div>

          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Envoi en cours...' : '🚨 Envoyer l\'alerte'}
          </button>
        </form>

        {message && (
          <div className={message.includes('✅') ? 'success' : 'error'}>
            {message}
          </div>
        )}
      </div>

      <div className="card">
        <h3>📋 Alertes Récentes</h3>
        <button onClick={loadAlerts} className="btn btn-secondary" style={{ marginBottom: '1rem' }}>
          🔄 Actualiser
        </button>

        {alerts.length > 0 ? (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
            {alerts.map((alert, index) => (
              <div 
                key={index}
                style={{
                  padding: '1rem',
                  borderLeft: `4px solid ${getAlertColor(alert.type)}`,
                  background: '#f8f9fa',
                  borderRadius: '4px'
                }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div>
                    <strong>{getAlertIcon(alert.type)} {alert.type}</strong>
                    <p style={{ margin: '0.25rem 0' }}>{alert.location}</p>
                    <small>{alert.description}</small>
                  </div>
                  <div style={{ textAlign: 'right' }}>
                    <div style={{ 
                      padding: '0.25rem 0.5rem', 
                      background: alert.status === 'pending' ? '#fff3cd' : '#d4edda',
                      borderRadius: '12px',
                      fontSize: '0.8rem'
                    }}>
                      {alert.status}
                    </div>
                    <small style={{ color: '#666' }}>
                      {new Date(alert.timestamp).toLocaleString()}
                    </small>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p>Aucune alerte active</p>
        )}
      </div>

      <div className="card">
        <h3>⚡ Avantages gRPC</h3>
        <ul style={{ lineHeight: '2' }}>
          <li>✅ Communication ultra-rapide (protobuf)</li>
          <li>✅ Streaming bidirectionnel en temps réel</li>
          <li>✅ Faible latence pour les urgences</li>
          <li>✅ Sécurité intégrée</li>
        </ul>
      </div>
    </div>
  );
};

export default EmergencyService;