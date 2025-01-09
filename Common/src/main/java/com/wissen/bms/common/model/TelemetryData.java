package com.wissen.bms.common.model;


import lombok.Data;

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
    private String gps;
//    private double latitude; // GPS coordinates
//    private double longitude; // GPS coordinates

    private long time;

    private double internalResistance;

    private String riskLevel;

}
