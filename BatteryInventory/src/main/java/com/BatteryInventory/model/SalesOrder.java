package com.BatteryInventory.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales_order")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stationId;
    private String status; // e.g., "pending", "partial", "completed"
    private LocalDateTime orderDate;
    private LocalDateTime completedDate;

    private int totalQuantity;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Battery> batteries = new ArrayList<>();

    @ElementCollection
    private List<String> missingBatteries;  // List of battery types that are missing

    @ElementCollection
    private List<String> allocatedBatteryIds;

    @ElementCollection
    private List<String> allocatedBatteryTypes;  // List of allocated battery types

    // Optional field to keep track if notification has been sent for this order
    private boolean notificationSent;

    // Getters, setters, and constructors

    public SalesOrder() {
    }

    public SalesOrder(String stationId, String status, LocalDateTime orderDate, LocalDateTime completedDate, int totalQuantity, List<Battery> batteries) {
        this.stationId = stationId;
        this.status = status;
        this.orderDate = orderDate;
        this.completedDate = completedDate;
        this.totalQuantity = totalQuantity;
        this.batteries = batteries;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
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

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public List<Battery> getBatteries() {
        return batteries;
    }

    public void setBatteries(List<Battery> batteries) {
        this.batteries = batteries;
    }

    public List<String> getMissingBatteries() {
        return missingBatteries;
    }

    public void setMissingBatteries(List<String> missingBatteries) {
        this.missingBatteries = missingBatteries;
    }

    public List<String> getAllocatedBatteryIds() {
        return allocatedBatteryIds;
    }

    public void setAllocatedBatteryIds(List<String> allocatedBatteryIds) {
        this.allocatedBatteryIds = allocatedBatteryIds;
    }

    public boolean isNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }
}

