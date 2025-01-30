package com.BatteryInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FaultyBatteryTypeRequest {
    private String batteryType;
    private List<String> batteryIds;
}
