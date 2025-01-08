package com.wissen.bms.common.model;


import lombok.Data;
import lombok.Getter;

@Data
public class TelemetryData {

    private String batteryId;

    private String vehicleId;

    private Double voltage;

    private Double current;


    private double soc; // State of Charge in %

    private double soh; // State of Health in %

    private double temperature; // Temperature in °C

    private double energyThroughput; // Energy throughput (in Wh)

    private double chargingTime; // Charging time (in minutes)

    private int cycleCount; // Charge cycles

    public double getInternalResistance() {
        return internalResistance;
    }

    @Override
    public String toString() {
        return "TelemetryData [batteryId=" + batteryId + ", vehicleId=" + vehicleId + ", voltage=" + voltage
                + ", current=" + current + ", soc=" + soc + ", soh=" + soh + ", temperature=" + temperature
                + ", energyThroughput=" + energyThroughput + ", chargingTime=" + chargingTime + ", cycleCount="
                + cycleCount + ", gps=" + gps + ", time=" + time + ", internalResistance=" + internalResistance
                + ", riskLevel=" + riskLevel + "]";
    }

    private String gps; // GPS coordinates

    private String time;

    private double internalResistance;

    private String riskLevel;

}
