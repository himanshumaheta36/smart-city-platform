import React, { useState } from 'react';
import { orchestrationAPI } from '../services/api';
import '../App.css';

const JourneyPlanner = () => {
  const [startLocation, setStartLocation] = useState('');
  const [endLocation, setEndLocation] = useState('');
  const [journeyPlan, setJourneyPlan] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handlePlanJourney = async (e) => {
    e.preventDefault();
    if (!startLocation || !endLocation) {
      setError('Veuillez saisir les lieux de départ et d\'arrivée');
      return;
    }

    setLoading(true);
    setError('');
    
    try {
      const response = await orchestrationAPI.planJourney(startLocation, endLocation);
      setJourneyPlan(response.data);
    } catch (err) {
      setError('Erreur lors de la planification du trajet');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="card">
        <h2>🗺️ Planificateur de Trajet Intelligent</h2>
        <p>Planifiez votre trajet en tenant compte de la qualité de l'air et des transports disponibles.</p>

        <form onSubmit={handlePlanJourney}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div className="form-group">
              <label>Lieu de départ:</label>
              <input
                type="text"
                value={startLocation}
                onChange={(e) => setStartLocation(e.target.value)}
                placeholder="Ex: Centre-ville"
              />
            </div>
            <div className="form-group">
              <label>Lieu d'arrivée:</label>
              <input
                type="text"
                value={endLocation}
                onChange={(e) => setEndLocation(e.target.value)}
                placeholder="Ex: Quartier Nord"
              />
            </div>
          </div>
          
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Planification...' : 'Planifier le trajet'}
          </button>
        </form>

        {error && <div className="error">{error}</div>}
      </div>

      {journeyPlan && (
        <div className="card">
          <h3>📋 Plan de Trajet</h3>
          <p><strong>De:</strong> {journeyPlan.startLocation} → <strong>À:</strong> {journeyPlan.endLocation}</p>
          
          <div style={{ 
            padding: '1rem', 
            background: journeyPlan.airQualityGood ? '#d4edda' : '#fff3cd',
            borderRadius: '8px',
            margin: '1rem 0'
          }}>
            <h4>🌫️ Qualité de l'Air</h4>
            <p>{journeyPlan.recommendation}</p>
            <p>Statut: {journeyPlan.airQualityGood ? '✅ Bonne' : '⚠️ Médiocre'}</p>
          </div>

          <div>
            <h4>🚗 Options de Transport</h4>
            {journeyPlan.transportOptions && journeyPlan.transportOptions.map((option, index) => (
              <div key={index} style={{
                padding: '1rem',
                border: '1px solid #e1e5e9',
                borderRadius: '8px',
                margin: '0.5rem 0',
                background: '#f8f9fa'
              }}>
                <strong>{option.type} - {option.line}</strong>
                <p>Prochain: {option.schedule} • Durée: {option.duration}min</p>
                <small>Status: {option.status}</small>
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="card">
        <h3>🔄 Workflow d'Orchestration</h3>
        <ol style={{ lineHeight: '2', marginLeft: '1.5rem' }}>
          <li><strong>Étape 1:</strong> Vérification qualité de l'air (SOAP)</li>
          <li><strong>Étape 2:</strong> Analyse des recommandations</li>
          <li><strong>Étape 3:</strong> Recherche options transport (REST)</li>
          <li><strong>Étape 4:</strong> Génération du plan optimal</li>
        </ol>
      </div>
    </div>
  );
};

export default JourneyPlanner;