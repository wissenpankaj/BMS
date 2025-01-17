package com.battery_inventory.serviceImpl;

import com.battery_inventory.dto.BatteryRequest;
import com.battery_inventory.dto.SalesOrderRequest;
import com.battery_inventory.entity.Battery;
import com.battery_inventory.entity.SalesOrder;
import com.battery_inventory.entity.SalesOrderDetails;
import com.battery_inventory.repository.BatteryRepository;
import com.battery_inventory.repository.SalesOrderDetailsRepository;
import com.battery_inventory.repository.SalesOrderRepository;
import com.battery_inventory.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private SalesOrderDetailsRepository salesOrderDetailsRepository;

    @Autowired
    private BatteryRepository batteryRepository;

    @Override
    public String createSalesOrder(SalesOrderRequest request) {
        if (request.getCustomerId() == null || request.getCustomerId().isEmpty()) {
            throw new IllegalArgumentException("Customer ID is required.");
        }

        // Validate duplicate battery IDs
        Set<String> uniqueBatteryIds = new HashSet<>();
        for (BatteryRequest batteryRequest : request.getBatteries()) {
            if (!uniqueBatteryIds.add(batteryRequest.getBatteryId())) {
                throw new IllegalArgumentException("Duplicate battery ID found: " + batteryRequest.getBatteryId());
            }
        }

        double totalPrice = 0.0;

        // Process each battery
        for (BatteryRequest batteryRequest : request.getBatteries()) {
            Battery battery = batteryRepository.findById(batteryRequest.getBatteryId())
                    .orElseThrow(() -> new IllegalArgumentException("Battery ID " + batteryRequest.getBatteryId() + " is invalid."));

            totalPrice += battery.getPrice();  // Only one unit per battery
        }

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomerId(request.getCustomerId());
        salesOrder.setOrderDate(request.getOrderDate());
        salesOrder.setStatus(request.getStatus());
        salesOrder.setTotalPrice(totalPrice);
        salesOrderRepository.save(salesOrder);

        for (BatteryRequest batteryRequest : request.getBatteries()) {
            Battery battery = batteryRepository.findById(batteryRequest.getBatteryId()).get();

            SalesOrderDetails details = new SalesOrderDetails();
            details.setSalesOrder(salesOrder);
            details.setBattery(battery);
            details.setPrice(battery.getPrice());
            salesOrderDetailsRepository.save(details);
        }

        return salesOrder.getOrderId();
    }

    @Override
    public List<SalesOrder> getSalesOrders(String customerId, String startDate, String endDate, String status) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (customerId != null && startDate != null && endDate != null && status != null) {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            return salesOrderRepository.findByCustomerIdAndOrderDateBetween(customerId, start, end);
        } else if (customerId != null && startDate != null) {
            LocalDate start = LocalDate.parse(startDate, formatter);
            return salesOrderRepository.findByCustomerIdAndOrderDateAfter(customerId, start);
        } else if (customerId != null && endDate != null) {
            LocalDate end = LocalDate.parse(endDate, formatter);
            return salesOrderRepository.findByCustomerIdAndOrderDateBefore(customerId, end);
        } else if (customerId != null && status != null) {
            return salesOrderRepository.findByCustomerIdAndStatus(customerId, status);
        } else if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            return salesOrderRepository.findByOrderDateBetween(start, end);
        } else if (status != null) {
            return salesOrderRepository.findByStatus(status);
        } else if (customerId != null) {
            return salesOrderRepository.findByCustomerId(customerId);
        } else {
            return salesOrderRepository.findAll();
        }
    }

    // PATCH method to update an existing sales order
    @Override
    public void updateSalesOrder(String orderId, SalesOrderRequest request) {
        // Find the existing sales order by orderId
        SalesOrder existingOrder = salesOrderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Sales order not found with ID: " + orderId));

        // Update fields of the existing order if new values are provided
        if (request.getCustomerId() != null) {
            existingOrder.setCustomerId(request.getCustomerId());
        }
        if (request.getOrderDate() != null) {
            existingOrder.setOrderDate(request.getOrderDate());
        }
        if (request.getStatus() != null) {
            existingOrder.setStatus(request.getStatus());
        }

        double totalPrice = 0.0;

        // Validate and process the batteries in the request
        Set<String> uniqueBatteryIds = new HashSet<>();
        for (BatteryRequest batteryRequest : request.getBatteries()) {
            if (!uniqueBatteryIds.add(batteryRequest.getBatteryId())) {
                throw new IllegalArgumentException("Duplicate battery ID found: " + batteryRequest.getBatteryId());
            }

            // Find the battery by its ID
            Battery battery = batteryRepository.findById(batteryRequest.getBatteryId())
                    .orElseThrow(() -> new IllegalArgumentException("Battery ID " + batteryRequest.getBatteryId() + " is invalid."));

            // Calculate the total price for this battery
            totalPrice += battery.getPrice();
        }

        // Update the total price of the order
        existingOrder.setTotalPrice(totalPrice);
        salesOrderRepository.save(existingOrder);

        // Remove existing sales order details before adding the updated details
        salesOrderDetailsRepository.deleteBySalesOrder(existingOrder);

        // Add the updated sales order details for each battery
        for (BatteryRequest batteryRequest : request.getBatteries()) {
            Battery battery = batteryRepository.findById(batteryRequest.getBatteryId()).get();

            SalesOrderDetails details = new SalesOrderDetails();
            details.setSalesOrder(existingOrder);
            details.setBattery(battery);
            details.setPrice(battery.getPrice());
            salesOrderDetailsRepository.save(details);
        }
    }

    @Override
    public void cancelSalesOrder(String orderId) {
        // Find the sales order by orderId
        SalesOrder salesOrder = salesOrderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Sales order not found with ID: " + orderId));

        // Retrieve all order details for the sales order
        List<SalesOrderDetails> orderDetails = salesOrderDetailsRepository.findBySalesOrder(salesOrder);

        // Restore the inventory levels for each battery in the order
        for (SalesOrderDetails detail : orderDetails) {
            Battery battery = detail.getBattery();
            batteryRepository.save(battery);
        }

        // Delete the sales order details
        salesOrderDetailsRepository.deleteAll(orderDetails);

        // Delete the sales order
        salesOrderRepository.delete(salesOrder);
    }

}

