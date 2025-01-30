package com.BatteryInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FaultyBatteryRequest {
    private String stationId;
    private List<FaultyBatteryTypeRequest> batteries;
}
