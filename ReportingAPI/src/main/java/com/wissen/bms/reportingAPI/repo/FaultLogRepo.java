package com.wissen.bms.reportingAPI.repo;

import com.wissen.bms.reportingAPI.model.FaultLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FaultLogRepo extends JpaRepository<FaultLogModel, Integer> {

    List<FaultLogModel> findByBatteryid(Integer batteryid);

    List<FaultLogModel> findByFaulttype(String faulttype);

    List<FaultLogModel> findByCreatedateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
