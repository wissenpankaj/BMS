package com.BatteryInventory.DTO;

public class MissingBatteryResponse {
    private String batteryType;
    private int missingCount;

    public MissingBatteryResponse(String batteryType, int missingCount) {
        this.batteryType = batteryType;
        this.missingCount = missingCount;
    }

    // Getters and Setters
    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

    public int getMissingCount() {
        return missingCount;
    }

    public void setMissingCount(int missingCount) {
        this.missingCount = missingCount;
    }
}

