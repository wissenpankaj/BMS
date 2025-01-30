package com.BatteryInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseOrderRequest {

    private String stationId;
    private String batteryType;
    private int quantity;
}
