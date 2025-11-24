import React, { useState } from 'react'
import './App.css'
import Dashboard from './components/Dashboard'
import MobilityService from './components/MobilityService'
import AirQualityService from './components/AirQualityService'
import EmergencyService from './components/EmergencyService'
import EventsService from './components/EventsService'
import JourneyPlanner from './components/JourneyPlanner'

function App() {
  const [activeTab, setActiveTab] = useState('dashboard')

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return <Dashboard />
      case 'mobility':
        return <MobilityService />
      case 'air-quality':
        return <AirQualityService />
      case 'emergency':
        return <EmergencyService />
      case 'events':
        return <EventsService />
      case 'journey':
        return <JourneyPlanner />
      default:
        return <Dashboard />
    }
  }

  return (
    <div className="app">
      <header className="app-header">
        <div className="header-content">
          <h1>🏙️ Plateforme Ville Intelligente</h1>
          <p>Services urbains interopérables</p>
        </div>
      </header>

      <nav className="app-nav">
        <button 
          className={activeTab === 'dashboard' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('dashboard')}
        >
          📊 Tableau de Bord
        </button>
        <button 
          className={activeTab === 'mobility' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('mobility')}
        >
          🚗 Mobilité
        </button>
        <button 
          className={activeTab === 'air-quality' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('air-quality')}
        >
          🌫️ Qualité d'Air
        </button>
        <button 
          className={activeTab === 'emergency' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('emergency')}
        >
          🚨 Urgences
        </button>
        <button 
          className={activeTab === 'events' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('events')}
        >
          🎭 Événements
        </button>
        <button 
          className={activeTab === 'journey' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('journey')}
        >
          🗺️ Planificateur
        </button>
      </nav>

      <main className="app-main">
        {renderContent()}
      </main>

      <footer className="app-footer">
        <p>Projet ING GINF - Service Oriented Computing - Année 2025-2026</p>
      </footer>
    </div>
  )
}

export default App