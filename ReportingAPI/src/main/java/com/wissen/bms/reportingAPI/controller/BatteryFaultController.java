package com.wissen.bms.reportingAPI.controller;

import com.wissen.bms.reportingAPI.model.BatteryFaultModel;
import com.wissen.bms.reportingAPI.service.BatteryFaultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faults")
@Validated
public class BatteryFaultController {
    @Autowired
    private BatteryFaultService batteryFaultService;

    @GetMapping("/all")
    public ResponseEntity<List<BatteryFaultModel>> getAllBatteryFaults() {
        List<BatteryFaultModel> batteryFaults = batteryFaultService.getAllBatteryFaults();
        return ResponseEntity.ok(batteryFaults);
    }

    @GetMapping("/{faultId}")
    public ResponseEntity<BatteryFaultModel> getBatteryFaultByFaultId(@Valid @PathVariable String faultId) {
        BatteryFaultModel batteryFaults = batteryFaultService.getBatteryFaultByFaultId(faultId);
        return batteryFaults != null ? ResponseEntity.ok(batteryFaults) : ResponseEntity.notFound().build();
    }

    @GetMapping("/battery/{batteryId}")
    public ResponseEntity<List<BatteryFaultModel>> getBatteryFaultsByBatteryId(@Valid @PathVariable String batteryId) {
        List<BatteryFaultModel> batteryFaults = batteryFaultService.getBatteryFaultsByBatteryId(batteryId);
        return ResponseEntity.ok(batteryFaults);
    }

    @GetMapping("/date")
    public ResponseEntity<List<BatteryFaultModel>> getBatteryFaultsByCreateDate(@Valid @RequestParam String createDate) {
        List<BatteryFaultModel> batteryFaults = batteryFaultService.getBatteryFaultsByCreatedAt(createDate);
        return ResponseEntity.ok(batteryFaults);
    }

    @GetMapping("/type")
    public ResponseEntity<List<BatteryFaultModel>> getBatteryFaultsByFaultReason(@Valid @RequestParam String faultReason) {
        List<BatteryFaultModel> batteryFaults = batteryFaultService.getBatteryFaultsByFaultType(faultReason);
        return ResponseEntity.ok(batteryFaults);
    }
}
