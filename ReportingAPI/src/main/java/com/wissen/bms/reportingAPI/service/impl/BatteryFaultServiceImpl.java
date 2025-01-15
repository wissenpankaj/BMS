package com.wissen.bms.reportingAPI.service.impl;

import com.wissen.bms.reportingAPI.model.BatteryFaultModel;
import com.wissen.bms.reportingAPI.repo.BatteryFaultRepo;
import com.wissen.bms.reportingAPI.service.BatteryFaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class BatteryFaultServiceImpl implements BatteryFaultService {

    @Autowired
    private BatteryFaultRepo batteryFaultRepo;

    @Override
    public List<BatteryFaultModel> getAllBatteryFaults() {
        return batteryFaultRepo.findAll();
    }

    @Override
    public BatteryFaultModel getBatteryFaultByFaultId(String faultId) {
        Optional<BatteryFaultModel> batteryFault = batteryFaultRepo.findById(faultId);
        return batteryFault.orElse(null);
    }

    @Override
    public List<BatteryFaultModel> getBatteryFaultsByBatteryId(String batteryId) {
        Optional<List<BatteryFaultModel>> batteryFault = batteryFaultRepo.findByBatteryId(batteryId);
        return batteryFault.orElse(null);
    }

    @Override
    public List<BatteryFaultModel> getBatteryFaultsByCreatedAt(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(time, formatter);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        Optional<List<BatteryFaultModel>> batteryFault = batteryFaultRepo.findByTimeBetween(startOfDay, endOfDay);
        return batteryFault.orElse(null);
    }

    @Override
    public List<BatteryFaultModel> getBatteryFaultsByFaultType(String faultReason) {
        Optional<List<BatteryFaultModel>> batteryFault = batteryFaultRepo.findByFaultReason(faultReason);
        return batteryFault.orElse(null);
    }
}
