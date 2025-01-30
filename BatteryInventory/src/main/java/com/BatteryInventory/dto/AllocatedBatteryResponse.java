package com.BatteryInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AllocatedBatteryResponse {
    private String batteryId;
    private String batteryType;
    private String status;
}