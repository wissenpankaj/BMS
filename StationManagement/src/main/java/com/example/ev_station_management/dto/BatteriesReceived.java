package com.example.ev_station_management.dto;

import java.util.List;
import java.util.stream.Collectors;

public class BatteriesReceived {

    private Long station_id; // The station ID
    private List<String> listOfBattery; // List of battery IDs as Strings

    // Constructors
    public BatteriesReceived() {}

    // Constructor where listOfBattery is a List<Long>, converting it to List<String>
    public BatteriesReceived(Long station_id, List<Long> listOfBattery) {
        this.station_id = station_id;
        // Convert List<Long> to List<String>
        this.listOfBattery = listOfBattery.stream()
                .map(String::valueOf)  // Convert each Long to String
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public Long getStation_id() {
        return station_id;
    }

    public void setStation_id(Long station_id) {
        this.station_id = station_id;
    }

    public List<String> getListOfBattery() {
        return listOfBattery;
    }

    public void setListOfBattery(List<String> listOfBattery) {
        // No need to convert when setting a List<String> here
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
