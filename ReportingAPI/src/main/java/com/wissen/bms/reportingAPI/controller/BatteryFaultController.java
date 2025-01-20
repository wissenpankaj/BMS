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

    @GetMapping
    public ResponseEntity<List<BatteryFaultModel>> getBatteryFaults(
            @RequestParam(required = false) String faultId,
            @RequestParam(required = false) String gps,
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String batteryId,
            @RequestParam(required = false) String faultReason,
            @RequestParam(required = false) String recommendation,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String risk,
            @RequestParam(required = false) String date) {

        List<BatteryFaultModel> batteryFaults = batteryFaultService.getBatteryFaults(faultId, gps, vehicleId, batteryId, faultReason, recommendation, level, risk, date);
        return ResponseEntity.ok(batteryFaults);
    }
}
