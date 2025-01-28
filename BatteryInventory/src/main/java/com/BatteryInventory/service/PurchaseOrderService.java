package com.BatteryInventory.service;

import com.BatteryInventory.model.PurchaseOrder;
import com.BatteryInventory.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    @Lazy
    private SalesOrderService salesOrderService;

    @Autowired
    @Lazy
    private SupplierService supplierService;

    @Autowired
    private PurchaseOrderNotificationService purchaseOrderNotificationService;

    /**
     * Creates a purchase order for a specific type of battery and quantity.
     */
    public void createPurchaseOrder(String batteryType, int quantity) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setBatteryType(batteryType);
        purchaseOrder.setQuantity(quantity);
        purchaseOrder.setStatus("pending");
        purchaseOrder.setOrderDate(LocalDateTime.now());
        purchaseOrder.setSupplier(supplierService.getDefaultSupplier());

        purchaseOrderRepository.save(purchaseOrder);

        // Triggering the supplier UI API to notify about the new purchase order
        purchaseOrderNotificationService.notifySupplierAboutNewPurchaseOrder();
    }

    /**
     * Updates a purchase order's status to 'completed'.
     */
    public void completePurchaseOrder(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new RuntimeException("Purchase order not found!"));
        purchaseOrder.setStatus("completed");
        purchaseOrderRepository.save(purchaseOrder);

        // Once the purchase order is completed, we can notify SalesOrderService
        salesOrderService.handleCompletedPurchaseOrder(purchaseOrder);
    }

    public PurchaseOrder getPurchaseOrderById(Long purchaseOrderId) {
        return purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found for ID: " + purchaseOrderId));
    }

    /**
     * Returns all purchase orders with status 'pending'.
     */
    public List<PurchaseOrder> getPendingPurchaseOrders() {
        return purchaseOrderRepository.findByStatus("pending");
    }
}
