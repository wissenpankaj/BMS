package com.wissen.bms.common.model;

import lombok.Data;

@Data

public class BatteryFault {
    private String gps;
    private String vehicleId;
    private String batteryId;
    private String faultReason;
    private String recommendation;
    private String time;

    @Override
    public String toString() {
        return "BatteryFault{" +
                "gps='" + gps + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", batteryId='" + batteryId + '\'' +
                ", faultReason='" + faultReason + '\'' +
                ", recommendation='" + recommendation + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

