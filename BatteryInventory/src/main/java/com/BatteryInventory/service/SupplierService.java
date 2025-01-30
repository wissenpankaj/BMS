package com.BatteryInventory.service;

import com.BatteryInventory.dto.SupplierFulfillmentRequest;
import com.BatteryInventory.model.Battery;
import com.BatteryInventory.model.PurchaseOrder;
import com.BatteryInventory.model.Supplier;
import com.BatteryInventory.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private BatteryService batteryService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    /**
     * Handles the fulfillment of a purchase order by creating new batteries and updating the order status.
     */
    public void fulfillPurchaseOrder(SupplierFulfillmentRequest request) {
        // Validate the purchase order ID
        Long purchaseOrderId = request.getPurchaseOrderId();
        String batteryType = request.getBatteryType();
        int quantity = request.getQuantity();

        // Fetch the PurchaseOrder entity from the database
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(purchaseOrderId);

        // Create new batteries
        List<Battery> newBatteries = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Battery newBattery = new Battery();
            newBattery.setBatteryId(UUID.randomUUID().toString());
            newBattery.setType(batteryType);
            newBattery.setSerialNumber("SN-" + UUID.randomUUID());
            newBattery.setStatus("available");
            newBattery.setPurchaseOrder(purchaseOrder);
            newBatteries.add(newBattery);
        }

        // Save new batteries to the battery table
        batteryService.saveBatteries(newBatteries);

        // Mark the purchase order as completed
        purchaseOrderService.completePurchaseOrder(purchaseOrderId);
    }

    /**
     * Returns the default supplier for creating purchase orders.
     */
    public Supplier getDefaultSupplier() {
        return supplierRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("No supplier found!"));
    }
}
