package com.smartcity.emergency.repository;

import com.smartcity.emergency.model.EmergencyAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyAlertRepository extends JpaRepository<EmergencyAlert, String> {
    
    List<EmergencyAlert> findByStatusIn(List<EmergencyAlert.EmergencyStatus> statuses);
    
    List<EmergencyAlert> findByEmergencyTypeAndSeverityLevelAndStatusAndLocationContaining(
        EmergencyAlert.EmergencyType emergencyType,
        EmergencyAlert.SeverityLevel severityLevel,
        EmergencyAlert.EmergencyStatus status,
        String location
    );
    
    @Query("SELECT e FROM EmergencyAlert e WHERE " +
           "(:type IS NULL OR e.emergencyType = :type) AND " +
           "(:severity IS NULL OR e.severityLevel = :severity) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:location IS NULL OR e.location LIKE %:location%)")
    List<EmergencyAlert> findWithFilters(
        @Param("type") EmergencyAlert.EmergencyType type,
        @Param("severity") EmergencyAlert.SeverityLevel severity,
        @Param("status") EmergencyAlert.EmergencyStatus status,
        @Param("location") String location
    );
    
    @Query("SELECT e FROM EmergencyAlert e WHERE e.createdAt >= :since")
    List<EmergencyAlert> findSince(@Param("since") LocalDateTime since);
    
    long countByStatusIn(List<EmergencyAlert.EmergencyStatus> statuses);
    
    @Query("SELECT e.location, COUNT(e) FROM EmergencyAlert e WHERE e.createdAt >= :since GROUP BY e.location ORDER BY COUNT(e) DESC")
    List<Object[]> findHotspots(@Param("since") LocalDateTime since);
}