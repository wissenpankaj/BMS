package com.BatteryInventory.controller;

import com.BatteryInventory.dto.FaultyBatteryRequest;
import com.BatteryInventory.dto.SalesOrderResponse;
import com.BatteryInventory.service.BatteryService;
import com.BatteryInventory.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles battery-related endpoints:
 * - POST /api/v1/batteries/faulty
 * - GET  /api/v1/batteries/availability
 */
@RestController
@RequestMapping("/api/v1/batteries")
public class BatteryController {

    @Autowired
    @Lazy
    private SalesOrderService salesOrderService;

    @Autowired
    @Lazy
    private BatteryService batteryService;

    @PostMapping("/faulty")
    public ResponseEntity<SalesOrderResponse> handleFaultyBatteries(@RequestBody FaultyBatteryRequest request) {
        return ResponseEntity.ok(salesOrderService.processFaultyBatteries(request));
    }

    /**
     * GET /api/v1/batteries/availability
     * Returns aggregated availability info
     **/

    @GetMapping("/availability")
    public ResponseEntity<List<String>> getAvailableBatteries(@RequestParam String batteryType) {
        return ResponseEntity.ok(batteryService.getAvailableBatteryIds(batteryType));
    }
}


