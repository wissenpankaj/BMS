package com.BatteryInventory.DTO;

public class AllocatedBatteryResponse {
    private String batteryId;
    private String batteryType;
    private String status;

    // Getters, setters, and constructors

    public AllocatedBatteryResponse(String batteryId, String batteryType, String status) {
        this.batteryId = batteryId;
        this.batteryType = batteryType;
        this.status = status;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}