package com.wissen.bms.reportingAPI.service;

import com.wissen.bms.reportingAPI.model.BatteryFaultModel;
import com.wissen.bms.reportingAPI.model.FaultLogModel;

import java.util.List;

public interface BatteryFaultService {
    // Method to get all fault logs
    List<BatteryFaultModel> getAllBatteryFaults();

    // Method to get fault logs by fault ID
    BatteryFaultModel getBatteryFaultByFaultId(String faultId);

    // Method to get fault logs by battery ID
    List<BatteryFaultModel> getBatteryFaultsByBatteryId(String batteryId);

    // Method to get fault logs by creation date
    List<BatteryFaultModel> getBatteryFaultsByCreatedAt(String createDate);

    // Method to get fault logs by fault type
    List<BatteryFaultModel> getBatteryFaultsByFaultType(String faultType);

}
