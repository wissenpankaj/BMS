package com.wissen.bms.reportingAPI.repo;

import com.wissen.bms.reportingAPI.model.BatteryFaultModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BatteryFaultRepo extends JpaRepository<BatteryFaultModel, String> {

    Optional<List<BatteryFaultModel>> findByBatteryId(String batteryId);

    Optional<List<BatteryFaultModel>> findByTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<List<BatteryFaultModel>> findByFaultReason(String faultReason);
}
