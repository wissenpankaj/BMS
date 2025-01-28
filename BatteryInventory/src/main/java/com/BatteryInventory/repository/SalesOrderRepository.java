package com.BatteryInventory.repository;

import com.BatteryInventory.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    // Find sales orders that contain a specific battery type in their missing batteries list
    List<SalesOrder> findByMissingBatteriesContains(String batteryType);
}