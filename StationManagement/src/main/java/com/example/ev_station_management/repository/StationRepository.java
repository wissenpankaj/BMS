package com.example.ev_station_management.repository;

import com.example.ev_station_management.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Object> findByStationId(Long stationId);
}
