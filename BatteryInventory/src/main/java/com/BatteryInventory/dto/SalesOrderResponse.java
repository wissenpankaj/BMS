package com.BatteryInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SalesOrderResponse {
    private Long salesOrderId;
    private String status;
    private int totalQuantity;
    private List<AllocatedBatteryResponse> allocatedBatteries;
    private List<MissingBatteryResponse> missingBatteries; // Added missing batteries
}
