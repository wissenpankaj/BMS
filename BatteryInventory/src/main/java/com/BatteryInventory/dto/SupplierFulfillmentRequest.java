package com.BatteryInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupplierFulfillmentRequest {

    private Long purchaseOrderId;
    private String batteryType;
    private int quantity;
}
