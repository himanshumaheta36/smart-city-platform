package com.smartcity.mobility.repository;

import com.smartcity.mobility.model.TrafficInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrafficInfoRepository extends JpaRepository<TrafficInfo, Long> {
    
    List<TrafficInfo> findBySeverity(String severity);
    
    List<TrafficInfo> findByLocationContainingIgnoreCase(String location);
    
    List<TrafficInfo> findByIncidentType(String incidentType);
    
    @Query("SELECT t FROM TrafficInfo t WHERE t.reportedAt >= :since AND t.resolvedAt IS NULL")
    List<TrafficInfo> findActiveIncidentsSince(@Param("since") LocalDateTime since);
    
    List<TrafficInfo> findByResolvedAtIsNull();
}