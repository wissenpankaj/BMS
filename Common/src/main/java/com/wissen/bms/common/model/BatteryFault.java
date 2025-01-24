package com.wissen.bms.common.model;

public class BatteryFault implements VehicleInfo {
    private String gps;
    private String vehicleId;
    private String batteryId;
    private String faultReason;
    private String recommendation;
    private String time;
    private String level;
    private String risk;

    @Override
    public String toString() {
        return "BatteryFault{" +
                "gps='" + gps + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", batteryId='" + batteryId + '\'' +
                ", faultReason='" + faultReason + '\'' +
                ", recommendation='" + recommendation + '\'' +
                ", time='" + time + '\'' +
                ", level='" + level + '\'' +
                ", risk='" + risk + '\'' +
                '}';
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public String getFaultReason() {
        return faultReason;
    }

    public void setFaultReason(String faultReason) {
        this.faultReason = faultReason;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
}

