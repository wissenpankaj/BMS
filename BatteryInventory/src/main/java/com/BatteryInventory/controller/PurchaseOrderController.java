package com.BatteryInventory.controller;

import com.BatteryInventory.model.PurchaseOrder;
import com.BatteryInventory.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Handles purchase-order-related endpoints:
 *   - POST /api/v1/purchase-orders
 *   - GET  /api/v1/purchase-orders/{purchaseOrderId}
 *   - PUT  /api/v1/purchase-orders/{purchaseOrderId}
 */
@RestController
@RequestMapping("/api/v1/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    /**
     * POST /api/v1/purchase-orders
     * Example Body:
     * {
     *   "stationId": "station-123",
     *   "batteryType": "Li-Ion 12V",
     *   "quantity": 10
     * }
     */
    @PostMapping
    public ResponseEntity<CreatePurchaseOrderResponse> createPurchaseOrder(@RequestBody CreatePurchaseOrderRequest req) {
        var po = purchaseOrderService.createPurchaseOrder(req.getStationId(), req.getBatteryType(), req.getQuantity());

        CreatePurchaseOrderResponse resp = new CreatePurchaseOrderResponse();
        resp.setPurchaseOrderId(po.getPurchaseOrderId());
        resp.setStationId(po.getStationId());
        resp.setBatteryType(po.getBatteryType());
        resp.setQuantity(po.getQuantity());
        resp.setStatus(po.getStatus());
        resp.setOrderDate(po.getOrderDate());

        return ResponseEntity.ok(resp);
    }

    /**
     * GET /api/v1/purchase-orders/{purchaseOrderId}
     * e.g. /api/v1/purchase-orders/po-001
     */
    @GetMapping("/{purchaseOrderId}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrder(@PathVariable String purchaseOrderId) {
        PurchaseOrder po = purchaseOrderService.getPurchaseOrder(purchaseOrderId);
        return ResponseEntity.ok(po);
    }

    /**
     * PUT /api/v1/purchase-orders/{purchaseOrderId}
     * Example Body:
     * {
     *   "status": "approved",
     *   "expectedDeliveryDate": "2025-01-21T08:00:00"
     * }
     */
    @PutMapping("/{purchaseOrderId}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(
            @PathVariable String purchaseOrderId,
            @RequestBody UpdatePurchaseOrderRequest req) {

        LocalDateTime expectedDate = null;
        if (req.getExpectedDeliveryDate() != null && !req.getExpectedDeliveryDate().isEmpty()) {
            expectedDate = LocalDateTime.parse(req.getExpectedDeliveryDate());
        }

        PurchaseOrder updated = purchaseOrderService.updatePurchaseOrder(
                purchaseOrderId,
                req.getStatus(),
                expectedDate
        );

        return ResponseEntity.ok(updated);
    }

    // -------------------------
    // DTO Classes
    // -------------------------

    public static class CreatePurchaseOrderRequest {
        private String stationId;
        private String batteryType;
        private int quantity;

        public String getStationId() { return stationId; }
        public void setStationId(String stationId) { this.stationId = stationId; }

        public String getBatteryType() { return batteryType; }
        public void setBatteryType(String batteryType) { this.batteryType = batteryType; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public static class CreatePurchaseOrderResponse {
        private String purchaseOrderId;
        private String stationId;
        private String batteryType;
        private int quantity;
        private String status;
        private LocalDateTime orderDate;

        public String getPurchaseOrderId() { return purchaseOrderId; }
        public void setPurchaseOrderId(String purchaseOrderId) { this.purchaseOrderId = purchaseOrderId; }

        public String getStationId() { return stationId; }
        public void setStationId(String stationId) { this.stationId = stationId; }

        public String getBatteryType() { return batteryType; }
        public void setBatteryType(String batteryType) { this.batteryType = batteryType; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public LocalDateTime getOrderDate() { return orderDate; }
        public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    }

    public static class UpdatePurchaseOrderRequest {
        private String status;
        private String expectedDeliveryDate;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getExpectedDeliveryDate() { return expectedDeliveryDate; }
        public void setExpectedDeliveryDate(String expectedDeliveryDate) {
            this.expectedDeliveryDate = expectedDeliveryDate;
        }
    }
}

