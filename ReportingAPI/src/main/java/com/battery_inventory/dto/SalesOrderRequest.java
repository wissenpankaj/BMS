package com.battery_inventory.dto;

import java.util.List;

public class SalesOrderRequest {
    private String orderId;
    private String customerId;
    private String orderDate;
    private String status;
    private List<BatteryRequest> batteries;

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BatteryRequest> getBatteries() {
        return batteries;
    }

    public void setBatteries(List<BatteryRequest> batteries) {
        this.batteries = batteries;
    }
}

