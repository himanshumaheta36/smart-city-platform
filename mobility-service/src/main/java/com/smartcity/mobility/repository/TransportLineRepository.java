package com.smartcity.mobility.repository;

import com.smartcity.mobility.model.TransportLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportLineRepository extends JpaRepository<TransportLine, Long> {
    
    Optional<TransportLine> findByLineNumber(String lineNumber);
    
    List<TransportLine> findByTransportType(String transportType);
    
    List<TransportLine> findByStatus(String status);
    
    @Query("SELECT tl FROM TransportLine tl WHERE :station MEMBER OF tl.stations")
    List<TransportLine> findByStation(@Param("station") String station);
    
    List<TransportLine> findByDelayMinutesGreaterThan(Integer delayMinutes);
}