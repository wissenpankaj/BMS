package com.wissen.bms.notification.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatteryFault {
    private String gps;
    private String vehicleId;
    private String batteryId;
    private String faultReason;
    private String recommendation;
    private String time;
    private String level;
    private String risk;

    @Override
    public String toString() {
        return "BatteryFault{" +
                "gps='" + gps + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", batteryId='" + batteryId + '\'' +
                ", faultReason='" + faultReason + '\'' +
                ", recommendation='" + recommendation + '\'' +
                ", time='" + time + '\'' +
                ", level='" + level + '\'' +
                ", risk='" + risk + '\'' +
                '}';
    }
}
