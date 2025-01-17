package com.BatteryInventory.repository;

import com.BatteryInventory.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    // Find by the external purchaseOrderId
    PurchaseOrder findByPurchaseOrderId(String purchaseOrderId);
}
