package com.example.ev_station_management.repository;

import com.example.ev_station_management.model.FaultAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaultAlertRepository extends JpaRepository<FaultAlertEntity, Long> {
}
