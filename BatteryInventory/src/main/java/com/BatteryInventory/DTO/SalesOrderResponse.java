package com.BatteryInventory.DTO;

import java.util.List;

public class SalesOrderResponse {
    private Long salesOrderId;
    private String status;
    private int totalQuantity;
    private List<AllocatedBatteryResponse> allocatedBatteries;
    private List<MissingBatteryResponse> missingBatteries; // Added missing batteries

    // Getters, setters, and constructors

    public SalesOrderResponse(Long salesOrderId, String status, int totalQuantity, List<AllocatedBatteryResponse> allocatedBatteries, List<MissingBatteryResponse> missingBatteries) {
        this.salesOrderId = salesOrderId;
        this.status = status;
        this.totalQuantity = totalQuantity;
        this.allocatedBatteries = allocatedBatteries;
        this.missingBatteries = missingBatteries;
    }

    public Long getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(Long salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public List<AllocatedBatteryResponse> getAllocatedBatteries() {
        return allocatedBatteries;
    }

    public void setAllocatedBatteries(List<AllocatedBatteryResponse> allocatedBatteries) {
        this.allocatedBatteries = allocatedBatteries;
    }

    public List<MissingBatteryResponse> getMissingBatteries() {
        return missingBatteries;
    }

    public void setMissingBatteries(List<MissingBatteryResponse> missingBatteries) {
        this.missingBatteries = missingBatteries;
    }
}
