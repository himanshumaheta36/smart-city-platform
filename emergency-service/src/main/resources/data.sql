-- Données de test pour les urgences
INSERT INTO emergency_alerts (emergency_id, reporter_id, emergency_type, severity_level, location, latitude, longitude, description, affected_people, status, created_at, updated_at, responder_id, update_notes) VALUES
('test-emergency-1', 'citizen-123', 'ACCIDENT', 'HIGH', 'Centre-ville, Rue Principale', 48.8566, 2.3522, 'Accident de voiture avec blessés', 3, 'IN_PROGRESS', '2024-01-15 10:30:00', '2024-01-15 11:00:00', 'responder-456', 'Équipe médicale sur place'),
('test-emergency-2', 'citizen-456', 'FIRE', 'CRITICAL', 'Quartier Nord, Avenue des Champs', 48.8606, 2.3376, 'Incendie dans un immeuble résidentiel', 15, 'CONFIRMED', '2024-01-15 11:15:00', '2024-01-15 11:20:00', 'responder-789', 'Pompiers en route'),
('test-emergency-3', 'citizen-789', 'MEDICAL', 'MEDIUM', 'Quartier Sud, Place Centrale', 48.8525, 2.3472, 'Personne inconsciente dans la rue', 1, 'RESOLVED', '2024-01-15 09:45:00', '2024-01-15 10:30:00', 'responder-123', 'Patient transporté à l hôpital');

-- Tags pour les urgences
INSERT INTO emergency_tags (emergency_id, tag) VALUES
('test-emergency-1', 'accident'),
('test-emergency-1', 'blessés'),
('test-emergency-1', 'urgence'),
('test-emergency-2', 'incendie'),
('test-emergency-2', 'résidentiel'),
('test-emergency-2', 'évacuation'),
('test-emergency-3', 'médical'),
('test-emergency-3', 'inconscient');

-- Ressources assignées
INSERT INTO resource_assignments (resource_type, resource_id, quantity, estimated_arrival_minutes, emergency_id) VALUES
('AMBULANCE', 'amb-001', 2, 5, 'test-emergency-1'),
('DOCTOR', 'doc-001', 1, 8, 'test-emergency-1'),
('FIRE_TRUCK', 'fire-001', 3, 10, 'test-emergency-2'),
('POLICE', 'pol-001', 2, 7, 'test-emergency-2'),
('AMBULANCE', 'amb-002', 1, 5, 'test-emergency-3');