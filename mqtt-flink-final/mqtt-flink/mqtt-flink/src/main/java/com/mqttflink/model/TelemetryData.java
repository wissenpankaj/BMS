package com.mqttflink.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.Serializable;

public class TelemetryData {
    private String batteryId;
    private String vehicleId;
    private Double voltage;
    private Double current;
    private double soc;             // State of Charge in %
    private double soh;             // State of Health in %
    private double temperature;     // Temperature in °C
    private double energyThroughput; // Energy throughput (in Wh)
    private double chargingTime;     // Charging time (in minutes)
    private int cycleCount;         // Charge cycles
    private String gps;             // GPS coordinates
    private long time;              // Timestamp of telemetry data

    // Constructor
    public TelemetryData() {
    }

    public TelemetryData(String batteryId, String vehicleId, Double voltage, Double current, double soc, double soh,
                         double temperature, double energyThroughput, double chargingTime, int cycleCount, String gps, long time) {
        this.batteryId = batteryId;
        this.vehicleId = vehicleId;
        this.voltage = voltage;
        this.current = current;
        this.soc = soc;
        this.soh = soh;
        this.temperature = temperature;
        this.energyThroughput = energyThroughput;
        this.chargingTime = chargingTime;
        this.cycleCount = cycleCount;
        this.gps = gps;
        this.time = time;
    }

    // Getters and Setters
    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public double getSoc() {
        return soc;
    }

    public void setSoc(double soc) {
        this.soc = soc;
    }

    public double getSoh() {
        return soh;
    }

    public void setSoh(double soh) {
        this.soh = soh;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getEnergyThroughput() {
        return energyThroughput;
    }

    public void setEnergyThroughput(double energyThroughput) {
        this.energyThroughput = energyThroughput;
    }

    public double getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(double chargingTime) {
        this.chargingTime = chargingTime;
    }

    public int getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TelemetryData{" +
                "batteryId='" + batteryId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", voltage=" + voltage +
                ", current=" + current +
                ", soc=" + soc +
                ", soh=" + soh +
                ", temperature=" + temperature +
                ", energyThroughput=" + energyThroughput +
                ", chargingTime=" + chargingTime +
                ", cycleCount=" + cycleCount +
                ", gps='" + gps + '\'' +
                ", time=" + time +
                '}';
    }

    // Convert object to JSON string
    public String convertObjToString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Register for handling time if needed
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Deserialize from JSON string back to object
    public static TelemetryData convertStringToObj(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(json, TelemetryData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
