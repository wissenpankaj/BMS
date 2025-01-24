package com.wissen.bms.reportingAPI.service.impl;

import com.wissen.bms.reportingAPI.model.FaultLogModel;
import com.wissen.bms.reportingAPI.repo.FaultLogRepo;
import com.wissen.bms.reportingAPI.service.FaultLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

//@Service
public class FaultLogServiceImpl implements FaultLogService {

    @Autowired
    private FaultLogRepo faultLogRepo;

    @Override
    public List<FaultLogModel> getAllFaultLogs() {
        return faultLogRepo.findAll();
    }

    @Override
    public FaultLogModel getFaultLogByFaultId(String faultId) {
        Optional<FaultLogModel> faultLog = faultLogRepo.findById(faultId);
        return faultLog.orElse(null);
    }

    @Override
    public List<FaultLogModel> getFaultLogsByBatteryId(String batteryId) {
        return faultLogRepo.findByBatteryId(batteryId);
    }
    @Override
    public List<FaultLogModel> getFaultLogsByCreatedAt(String createDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(createDate, formatter);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return faultLogRepo.findByCreatedAtBetween(startOfDay, endOfDay);
    }

    @Override
    public List<FaultLogModel> getFaultLogsByFaultType(String faultType) {
        return faultLogRepo.findByFaultType(faultType);
    }
}
