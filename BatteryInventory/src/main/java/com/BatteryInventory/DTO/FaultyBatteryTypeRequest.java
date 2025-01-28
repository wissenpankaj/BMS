package com.BatteryInventory.DTO;

import java.util.List;

public class FaultyBatteryTypeRequest {
    private String batteryType;
    private List<String> batteryIds;

    // Getters, setters, and constructors

    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

    public List<String> getBatteryIds() {
        return batteryIds;
    }

    public void setBatteryIds(List<String> batteryIds) {
        this.batteryIds = batteryIds;
    }
}
