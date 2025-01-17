package com.BatteryInventory.service;

import com.BatteryInventory.model.Battery;
import com.BatteryInventory.model.PurchaseOrder;
import com.BatteryInventory.repository.BatteryRepository;
import com.BatteryInventory.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final BatteryRepository batteryRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
                                BatteryRepository batteryRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.batteryRepository = batteryRepository;
    }

    /**
     * Create a new purchase order (POST /api/v1/purchase-orders)
     */
    public PurchaseOrder createPurchaseOrder(String stationId, String batteryType, int quantity) {
        PurchaseOrder po = new PurchaseOrder();
        po.setPurchaseOrderId(generatePOId()); // or use a UUID approach
        po.setStationId(stationId);
        po.setBatteryType(batteryType);
        po.setQuantity(quantity);
        po.setStatus("pending");
        po.setOrderDate(LocalDateTime.now());

        return purchaseOrderRepository.save(po);
    }

    /**
     * Retrieve a purchase order by external ID (GET /api/v1/purchase-orders/{purchaseOrderId})
     */
    public PurchaseOrder getPurchaseOrder(String purchaseOrderId) {
        PurchaseOrder po = purchaseOrderRepository.findByPurchaseOrderId(purchaseOrderId);
        if (po == null) {
            throw new RuntimeException("Purchase order not found: " + purchaseOrderId);
        }
        return po;
    }

    /**
     * Update the purchase order (PUT /api/v1/purchase-orders/{purchaseOrderId})
     * If status is "approved", allocate dummy batteries for demonstration.
     */
    public PurchaseOrder updatePurchaseOrder(String purchaseOrderId, String newStatus, LocalDateTime expectedDate) {
        PurchaseOrder po = getPurchaseOrder(purchaseOrderId);
        po.setStatus(newStatus);
        po.setExpectedDeliveryDate(expectedDate);

        if ("approved".equalsIgnoreCase(newStatus)) {
            allocateBatteries(po);
        }

        return purchaseOrderRepository.save(po);
    }

    /**
     * Allocate 'quantity' active batteries (dummy logic) to the purchase order.
     * In real life, you'd filter by batteryType, station location, etc.
     */
    private void allocateBatteries(PurchaseOrder po) {
        // Find 'active' or 'ready_for_shipment' batteries (example: let's say status='active')
        List<Battery> activeList = batteryRepository.findBatteryAvailability().stream()
                .filter(prj -> prj.getStatus().equalsIgnoreCase("active")
                        && prj.getType().equalsIgnoreCase(po.getBatteryType()))
                .findAny()
                .map(prj -> {
                    // This query only returns aggregated data, so you'd likely do a second query
                    // for actual Battery records that match (type, status="active").
                    // For the sake of the example, let's just find all with "active".
                    return batteryRepository.findAll();
                })
                .orElse(new ArrayList<>());

        // This is a naive approach: pick the first N active from the entire battery table.
        int needed = po.getQuantity();
        List<Battery> allocated = new ArrayList<>();
        for (Battery b : activeList) {
            if (allocated.size() == needed) break;
            if ("active".equalsIgnoreCase(b.getStatus()) && b.getType().equalsIgnoreCase(po.getBatteryType())) {
                b.setStatus("allocated");
                b.setPurchaseOrder(po);
                allocated.add(b);
            }
        }
        po.setBatteries(allocated);
    }

    private String generatePOId() {
        // Example: "po-" + currentTimeMillis, or a UUID
        return "po-" + System.currentTimeMillis();
    }
}
