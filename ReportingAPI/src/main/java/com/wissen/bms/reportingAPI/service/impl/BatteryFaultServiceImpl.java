package com.wissen.bms.reportingAPI.service.impl;

import com.wissen.bms.reportingAPI.model.BatteryFaultModel;
import com.wissen.bms.reportingAPI.repo.BatteryFaultRepo;
import com.wissen.bms.reportingAPI.service.BatteryFaultService;
import com.wissen.bms.reportingAPI.specification.BatteryFaultSpecification;
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
    public List<BatteryFaultModel> getBatteryFaults(String faultId, String gps, String vehicleId, String batteryId, String faultReason, String recommendation, String level, String risk, String time) {
        return batteryFaultRepo.findAll(BatteryFaultSpecification.getBatteryFaults(faultId, gps, vehicleId, batteryId, faultReason, recommendation, level, risk, time));
    }
}
