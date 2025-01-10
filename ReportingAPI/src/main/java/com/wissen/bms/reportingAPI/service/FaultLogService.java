package com.wissen.bms.reportingAPI.service;

import com.wissen.bms.reportingAPI.model.FaultLogModel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface FaultLogService {

    // Method to get all fault logs
    List<FaultLogModel> getAllFaultLogs();

    // Method to get fault logs by fault ID
    FaultLogModel getFaultLogByFaultId(String faultId);

    // Method to get fault logs by battery ID
    List<FaultLogModel> getFaultLogsByBatteryId(String batteryId);

    // Method to get fault logs by creation date
    List<FaultLogModel> getFaultLogsByCreatedAt(String createDate);

    // Method to get fault logs by fault type
    List<FaultLogModel> getFaultLogsByFaultType(String faultType);

}
