package com.wissen.bms.reportingAPI.controller;

import com.wissen.bms.reportingAPI.model.FaultLogModel;
import com.wissen.bms.reportingAPI.service.FaultLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/faultlogs")
@Validated
public class FaultLogController {

    @Autowired
    private FaultLogService faultLogService;

    @GetMapping("/all")
    public ResponseEntity<List<FaultLogModel>> getAllFaultLogs() {
        List<FaultLogModel> faultLogs = faultLogService.getAllFaultLogs();
        return ResponseEntity.ok(faultLogs);
    }

    @GetMapping("/{faultId}")
    public ResponseEntity<FaultLogModel> getFaultLogByFaultId(@Valid @PathVariable Integer faultId) {
        FaultLogModel faultLog = faultLogService.getFaultLogByFaultId(faultId);
        return faultLog != null ? ResponseEntity.ok(faultLog) : ResponseEntity.notFound().build();
    }

    @GetMapping("/battery/{batteryId}")
    public ResponseEntity<List<FaultLogModel>> getFaultLogsByBatteryId(@Valid @PathVariable Integer batteryId) {
        List<FaultLogModel> faultLogs = faultLogService.getFaultLogsByBatteryId(batteryId);
        return ResponseEntity.ok(faultLogs);
    }

    @GetMapping("/date")
    public ResponseEntity<List<FaultLogModel>> getFaultLogsByCreateDate(@Valid @RequestParam String createDate) {
        List<FaultLogModel> faultLogs = faultLogService.getFaultLogsByCreatedAt(createDate);
        return ResponseEntity.ok(faultLogs);
    }

    @GetMapping("/type")
    public ResponseEntity<List<FaultLogModel>> getFaultLogsByFaultType(@Valid @RequestParam String faultType) {
        List<FaultLogModel> faultLogs = faultLogService.getFaultLogsByFaultType(faultType);
        return ResponseEntity.ok(faultLogs);
    }

}
