package com.BatteryInventory.model;


import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Internal primary key

    @Column(unique = true)
    private String purchaseOrderId;  // e.g., "po-001"

    private String stationId;
    private String batteryType;
    private int quantity;
    private String status;    // e.g. "pending", "approved"
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;

    // One purchase order can have many batteries allocated
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Battery> batteries = new ArrayList<>();

    public PurchaseOrder() {}

    public PurchaseOrder(String purchaseOrderId, String stationId, String batteryType,
                         int quantity, String status, LocalDateTime orderDate,
                         LocalDateTime expectedDeliveryDate) {
        this.purchaseOrderId = purchaseOrderId;
        this.stationId = stationId;
        this.batteryType = batteryType;
        this.quantity = quantity;
        this.status = status;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    // Getters/Setters

    public Long getId() {
        return id;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }
    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getBatteryType() {
        return batteryType;
    }
    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }
    public void setExpectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public List<Battery> getBatteries() {
        return batteries;
    }
    public void setBatteries(List<Battery> batteries) {
        this.batteries = batteries;
    }
}
