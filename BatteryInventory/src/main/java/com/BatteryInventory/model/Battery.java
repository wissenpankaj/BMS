package com.BatteryInventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "battery")
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String batteryId;

    private String type;
    private String serialNumber;
    private String status; // e.g., "available", "allocated", "faulty"

    // Association to PurchaseOrder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    // Association to SalesOrder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id")
    private SalesOrder salesOrder;

    // Getters, setters, and constructors

    public Battery() {}

    public Battery(String batteryId, String type, String serialNumber, String status) {
        this.batteryId = batteryId;
        this.type = type;
        this.serialNumber = serialNumber;
        this.status = status;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Long getId() {
        return id;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }
}
