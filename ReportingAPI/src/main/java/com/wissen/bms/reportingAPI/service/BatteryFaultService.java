package com.wissen.bms.reportingAPI.service;

import com.wissen.bms.reportingAPI.model.BatteryFaultModel;
import com.wissen.bms.reportingAPI.model.FaultLogModel;

import java.util.List;

public interface BatteryFaultService {
    public List<BatteryFaultModel> getBatteryFaults(String faultId, String gps, String vehicleId, String batteryId, String faultReason, String recommendation, String level, String risk, String time);
}
