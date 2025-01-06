package com.battery_inventory.dto;

public class BatteryDto {
        public String serialNumber;
        public String type;
        public String location;
        public String expiryDate;
        public String purchaseDate;
        public String status;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BatteryDto{" +
                "serialNumber='" + serialNumber + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

