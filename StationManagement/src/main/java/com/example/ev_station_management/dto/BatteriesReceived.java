package com.example.ev_station_management.dto;

import java.util.List;

public class BatteriesReceived {

    private Long station_id; // The station ID (formerly id)
    private List<Long> listOfBattery; // List of battery IDs

    // Constructors
    public BatteriesReceived() {}

    public BatteriesReceived(Long station_id, List<Long> listOfBattery) {
        this.station_id = station_id;
        this.listOfBattery = listOfBattery;
    }

    // Getters and Setters
    public Long getStation_id() {
        return station_id;
    }

    public void setStation_id(Long station_id) {
        this.station_id = station_id;
    }

    public List<Long> getListOfBattery() {
        return listOfBattery;
    }

    public void setListOfBattery(List<Long> listOfBattery) {
        this.listOfBattery = listOfBattery;
    }

    @Override
    public String toString() {
        return "BatteriesReceived{" +
                "station_id=" + station_id +
                ", listOfBattery=" + listOfBattery +
                '}';
    }
}
