package com.BatteryInventory.service;

import com.BatteryInventory.DTO.*;
import com.BatteryInventory.model.Battery;
import com.BatteryInventory.model.PurchaseOrder;
import com.BatteryInventory.model.SalesOrder;
import com.BatteryInventory.repository.BatteryRepository;
import com.BatteryInventory.repository.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesOrderService {

    @Autowired
    private BatteryService batteryService;

    @Autowired
    @Lazy
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private BatteryRepository batteryRepository;

    @Autowired
    private SalesOrderStatusService salesOrderStatusService;

     /*
     * Process the faulty batteries and check availability in inventory.
     * If batteries are available, a sales order is placed
     * If batteries are not available, a purchase order is placed.
     */

    public SalesOrderResponse processFaultyBatteries(FaultyBatteryRequest request) {

        // Mark all batteries in the request as faulty
        batteryService.makeBatteryAsFaulty(request);

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setStationId(request.getStationId());
        salesOrder.setStatus("pending");
        salesOrder.setOrderDate(LocalDateTime.now());

        List<AllocatedBatteryResponse> allocatedBatteries = new ArrayList<>(); //Track allocated batteries
        List<MissingBatteryResponse> missingBatteries = new ArrayList<>(); // Track missing batteries
        int totalRequested = 0;

        // Track the missing batteries and allocate available ones
        for (FaultyBatteryTypeRequest batteryRequest : request.getBatteries()) {
            totalRequested += batteryRequest.getBatteryIds().size();

            // Allocate available batteries
            List<Battery> allocated = batteryService.allocateAvailableBatteries(
                    batteryRequest.getBatteryType(),
                    salesOrder);

            // Add allocated batteries to response
            allocated.forEach(battery -> allocatedBatteries.add(new AllocatedBatteryResponse(
                    battery.getBatteryId(), battery.getType(), battery.getStatus())));

            // Calculate missing batteries and create a purchase order
            int missingCount = batteryRequest.getBatteryIds().size() - allocated.size();
            if (missingCount > 0) {
                missingBatteries.add(new MissingBatteryResponse(batteryRequest.getBatteryType(), missingCount));
                // Create a purchase order for the missing batteries
                purchaseOrderService.createPurchaseOrder(batteryRequest.getBatteryType(), missingCount);
            }
        }

        // If there are missing batteries, set the status to 'partial'
        if (!missingBatteries.isEmpty()) {
            salesOrder.setStatus("partial");
        } else {
            salesOrder.setStatus("completed");
        }

        salesOrder.setTotalQuantity(totalRequested);
        SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);

        return new SalesOrderResponse(
                savedSalesOrder.getId(),
                savedSalesOrder.getStatus(),
                savedSalesOrder.getTotalQuantity(),
                allocatedBatteries,
                missingBatteries);
    }

    /**
     * This method is called when a purchase order is completed.
     * It updates the sales order by removing missing batteries and setting status to 'completed' if all batteries are available.
     */
    public void handleCompletedPurchaseOrder(PurchaseOrder purchaseOrder) {
        salesOrderStatusService.updateSalesOrderStatus(purchaseOrder);

    }
}

