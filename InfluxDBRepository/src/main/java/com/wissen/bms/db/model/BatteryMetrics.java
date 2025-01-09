package com.wissen.bms.db.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "battery_metrics")
public class BatteryMetrics {

    public BatteryMetrics(){}

    public BatteryMetrics(String vehicleId, String batteryId, Double longitude, Double latitude,
                          Double voltage, Double current, Double soc, Double temperature,
                          Double internalResistance, Integer cycleCount, Double energyThroughput,
                          Double chargingTime, Double soh) {
        this.vehicleId = vehicleId;
        this.batteryId = batteryId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.voltage = voltage;
        this.current = current;
        this.soc = soc;
        this.temperature = temperature;
        this.internalResistance = internalResistance;
        this.cycleCount = cycleCount;
        this.energyThroughput = energyThroughput;
        this.chargingTime = chargingTime;
        this.soh = soh;
    }

    @Column(name = "vehicle_id", tag = true)
    private String vehicleId;

    @Column(name = "battery_id", tag = true)
    private String batteryId;

    @Column
    private Double longitude;

    @Column
    private Double latitude;

    @Column
    private Double voltage;

    @Column
    private Double current;

    @Column
    private Double soc; // State of charge

    @Column
    private Double temperature;

    @Column(name = "internal_resistance")
    private Double internalResistance;

    @Column(name = "cycle_count")
    private Integer cycleCount;

    @Column(name = "energy_throughput")
    private Double energyThroughput;

    @Column(name = "charging_time")
    private Double chargingTime;

    @Column
    private Double soh; // State of health

    @Column(timestamp = true)
    private Instant time;

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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    public Double getSoc() {
        return soc;
    }

    public void setSoc(Double soc) {
        this.soc = soc;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getInternalResistance() {
        return internalResistance;
    }

    public void setInternalResistance(Double internalResistance) {
        this.internalResistance = internalResistance;
    }

    public Integer getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(Integer cycleCount) {
        this.cycleCount = cycleCount;
    }

    public Double getEnergyThroughput() {
        return energyThroughput;
    }

    public void setEnergyThroughput(Double energyThroughput) {
        this.energyThroughput = energyThroughput;
    }

    public Double getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(Double chargingTime) {
        this.chargingTime = chargingTime;
    }

    public Double getSoh() {
        return soh;
    }

    public void setSoh(Double soh) {
        this.soh = soh;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BatteryMetrics{" +
                "vehicleId='" + vehicleId + '\'' +
                ", batteryId='" + batteryId + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", voltage=" + voltage +
                ", current=" + current +
                ", soc=" + soc +
                ", temperature=" + temperature +
                ", internalResistance=" + internalResistance +
                ", cycleCount=" + cycleCount +
                ", energyThroughput=" + energyThroughput +
                ", chargingTime=" + chargingTime +
                ", soh=" + soh +
                ", time=" + time +
                '}';
    }
}

