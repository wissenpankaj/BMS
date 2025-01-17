package com.battery_inventory.controller;

import com.battery_inventory.dto.SalesOrderRequest;
import com.battery_inventory.entity.SalesOrder;
import com.battery_inventory.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/orders")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @PostMapping
    public ResponseEntity<?> createSalesOrder(@RequestBody SalesOrderRequest request) {
        try {
            String orderId = salesOrderService.createSalesOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sales order created successfully. Order ID: " + orderId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<SalesOrder>> getSalesOrders(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String status) {
        try {
            List<SalesOrder> salesOrders = salesOrderService.getSalesOrders(customerId, startDate, endDate, status);
            return ResponseEntity.ok(salesOrders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<?> updateSalesOrder(@PathVariable String orderId, @RequestBody SalesOrderRequest request) {
        try {
            // Update sales order based on the provided orderId and request details
            salesOrderService.updateSalesOrder(orderId, request);
            return ResponseEntity.status(HttpStatus.OK).body("Sales order updated successfully. Order ID: " + orderId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> cancelSalesOrder(@PathVariable String orderId) {
        try {
            salesOrderService.cancelSalesOrder(orderId);
            return ResponseEntity.ok("Sales order canceled successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
