package com.BatteryInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissingBatteryResponse {
    private String batteryType;
    private int missingCount;
}

