package com.wissen.bms.reportingAPI.repo;

import com.wissen.bms.reportingAPI.model.FaultLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FaultLogRepo extends JpaRepository<FaultLogModel, String> {

    List<FaultLogModel> findByBatteryId(String batteryId);

    List<FaultLogModel> findByFaultType(String faultType);

    List<FaultLogModel> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
