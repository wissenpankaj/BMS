package com.BatteryInventory.model;



import jakarta.persistence.*;



@Entity
@Table(name = "battery")
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Internal primary key

    @Column(unique = true)
    private String batteryId;

    private String type;          // e.g. "Li-Ion 12V"
    private String serialNumber;
    private String status;        // e.g. "faulty", "active", "allocated", ...

    // In this design, Battery might be "stand-alone" or
    // it might link to a PurchaseOrder if allocated.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    public Battery() {}

    public Battery(String batteryId, String type, String serialNumber, String status) {
        this.batteryId = batteryId;
        this.type = type;
        this.serialNumber = serialNumber;
        this.status = status;
    }

    // Getters/Setters

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

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}

