package com.wissen.bms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationInfoDTO implements VehicleInfo {
    private String vehicleId;
    private String stationName;
    private int availableStock;
    private double latitude;
    private double longitude;

    @Override
    public String toString() {
        return "StationInfoDTO{" +
                "vehicleId='" + vehicleId + '\'' +
                ", stationName='" + stationName + '\'' +
                ", availableStock=" + availableStock +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
