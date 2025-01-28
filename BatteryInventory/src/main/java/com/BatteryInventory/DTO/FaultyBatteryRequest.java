package com.BatteryInventory.DTO;

import com.BatteryInventory.model.Battery;

import java.util.List;

public class FaultyBatteryRequest {
    private String stationId;
    private List<FaultyBatteryTypeRequest> batteries;

    // Getters, setters, and constructors


    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public List<FaultyBatteryTypeRequest> getBatteries() {
        return batteries;
    }

    public void setBatteries(List<FaultyBatteryTypeRequest> batteries) {
        this.batteries = batteries;
    }
}
