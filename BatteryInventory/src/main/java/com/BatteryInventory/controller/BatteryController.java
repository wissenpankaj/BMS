package com.BatteryInventory.controller;

import com.BatteryInventory.model.Battery;
import com.BatteryInventory.service.BatteryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles battery-related endpoints:
 *  - POST /api/v1/batteries/faulty
 *  - GET  /api/v1/batteries/availability
 */
@RestController
@RequestMapping("/api/v1/batteries")
public class BatteryController {

    private final BatteryService batteryService;

    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    /**
     * POST /api/v1/batteries/faulty
     * Request Body Example:
     * {
     *   "stationId": "station-123",
     *   "faultyBatteries": [
     *     { "batteryId": "battery-001", "type": "Li-Ion 12V", "serialNumber": "SN1234567890" }
     *   ]
     * }
     */
    @PostMapping("/faulty")
    public ResponseEntity<FaultyBatteryResponse> submitFaultyBatteries(@RequestBody FaultyBatteryRequest request) {

        List<Battery> saved = batteryService.saveFaultyBatteries(
                request.getStationId(),
                request.getFaultyBatteries()
        );

        FaultyBatteryResponse response = new FaultyBatteryResponse();
        response.setMessage("Faulty batteries have been recorded successfully");
        response.setUpdatedCount(saved.size());
        response.setFaultyBatteries(saved);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/batteries/availability (Optional)
     * Returns aggregated availability info
     */
    @GetMapping("/availability")
    public ResponseEntity<AvailabilityResponse> getAvailability() {
        var availabilityList = batteryService.getBatteryAvailability();
        AvailabilityResponse resp = new AvailabilityResponse();
        resp.setBatteries(availabilityList);
        return ResponseEntity.ok(resp);
    }

    // ----------- DTOs for the request/response -----------

    public static class FaultyBatteryRequest {
        private String stationId;
        private List<Battery> faultyBatteries;

        public String getStationId() { return stationId; }
        public void setStationId(String stationId) { this.stationId = stationId; }

        public List<Battery> getFaultyBatteries() { return faultyBatteries; }
        public void setFaultyBatteries(List<Battery> faultyBatteries) { this.faultyBatteries = faultyBatteries; }
    }

    public static class FaultyBatteryResponse {
        private String message;
        private int updatedCount;
        private List<Battery> faultyBatteries;

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public int getUpdatedCount() { return updatedCount; }
        public void setUpdatedCount(int updatedCount) { this.updatedCount = updatedCount; }

        public List<Battery> getFaultyBatteries() { return faultyBatteries; }
        public void setFaultyBatteries(List<Battery> faultyBatteries) { this.faultyBatteries = faultyBatteries; }
    }

    public static class AvailabilityResponse {
        private List<BatteryService.BatteryAvailabilityDto> batteries;

        public List<BatteryService.BatteryAvailabilityDto> getBatteries() { return batteries; }
        public void setBatteries(List<BatteryService.BatteryAvailabilityDto> batteries) {
            this.batteries = batteries;
        }
    }
}

