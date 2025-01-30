package com.example.ev_station_management.model;


import jakarta.persistence.*;

@Entity
public class BatterySwapLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Auto-incrementing ID

    @Column(name = "fault_battery_id")
    private Long faultBatteryId;  // ID of the faulty battery

    @Column(name = "swapped_battery_id")
    private String swappedBatteryId;  // ID of the swapped battery

    @Column(name = "station_id")
    private Long stationId;  // ID of the station

    @Column(name = "success")
    private Boolean success;  // Whether the swap was successful or not


    // Default constructor (required by JPA)
    public BatterySwapLog() {}

    // Constructor to set all fields
    public BatterySwapLog(Long faultBatteryId, String swappedBatteryId, Long stationId, Boolean success) {
        this.faultBatteryId = faultBatteryId;
        this.swappedBatteryId = swappedBatteryId;
        this.stationId = stationId;
        this.success = success;
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFaultBatteryId() {
        return faultBatteryId;
    }

    public void setFaultBatteryId(Long faultBatteryId) {
        this.faultBatteryId = faultBatteryId;
    }

    public String getSwappedBatteryId() {
        return swappedBatteryId;
    }

    public void setSwappedBatteryId(String swappedBatteryId) {
        this.swappedBatteryId = swappedBatteryId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
