package com.BatteryInventory.controller;

import com.BatteryInventory.dto.SupplierFulfillmentRequest;
import com.BatteryInventory.model.PurchaseOrder;
import com.BatteryInventory.service.PurchaseOrderService;
import com.BatteryInventory.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    @Lazy
    private SupplierService supplierService;

    @Autowired
    @Lazy
    private PurchaseOrderService purchaseOrderService;

    /**
     * Fetches all pending purchase orders.
     */
    @GetMapping("/pending-purchase-orders")
    public ResponseEntity<List<PurchaseOrder>> getPendingPurchaseOrders() {
        return ResponseEntity.ok(purchaseOrderService.getPendingPurchaseOrders());
    }

    @PostMapping("/fulfill-order")
    public void fulfillPurchaseOrder(@RequestBody SupplierFulfillmentRequest request) {
        supplierService.fulfillPurchaseOrder(request);
    }
}
