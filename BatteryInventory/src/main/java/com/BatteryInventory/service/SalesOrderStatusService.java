package com.BatteryInventory.service;

import com.BatteryInventory.model.PurchaseOrder;
import com.BatteryInventory.model.SalesOrder;
import com.BatteryInventory.repository.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesOrderStatusService {
    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private SalesOrderNotificationService salesOrderNotificationService;

    public void updateSalesOrderStatus(PurchaseOrder purchaseOrder) {
        // Find the sales orders associated with the missing batteries in this purchase order
        List<SalesOrder> salesOrders = salesOrderRepository.findByMissingBatteriesContains(purchaseOrder.getBatteryType());

        // For each sales order, update the missing batteries and check if the order can be completed
        for (SalesOrder salesOrder : salesOrders) {
            // Remove the missing battery type from the list of missing batteries
            List<String> updatedMissingBatteries = salesOrder.getMissingBatteries().stream()
                    .filter(batteryType -> !batteryType.equals(purchaseOrder.getBatteryType()))
                    .collect(Collectors.toList());

            salesOrder.setMissingBatteries(updatedMissingBatteries);

            // If there are no more missing batteries, set the status to 'completed'
            if (updatedMissingBatteries.isEmpty()) {
                salesOrder.setStatus("completed");
                // Optionally: Mark as notification sent once the order is completed
                salesOrder.setNotificationSent(true);
                // Send a notification (if needed)
                salesOrderNotificationService.sendCompletionNotification(salesOrder);
            } else {
                salesOrder.setStatus("partial");
            }

            // Save the updated sales order
            salesOrderRepository.save(salesOrder);
        }
    }
}
