package com.example.ev_station_management.dto;

public class StationInfoDto {

    private String stationName;
    private int availableStock;
    private double latitude;
    private double longitude;
    private String vehicleId;

    // Constructor
    public StationInfoDto(String stationName, int availableStock, double latitude, double longitude, String vehicleId) {
        this.stationName = stationName;
        this.availableStock = availableStock;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vehicleId = vehicleId;
    }

    // Getter and Setter methods
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    // toString method
    @Override
    public String toString() {
        return "StationInfoDto{" +
                "stationName='" + stationName + '\'' +
                ", availableStock=" + availableStock +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", vehicleId='" + vehicleId + '\'' +
                '}';
    }
}
