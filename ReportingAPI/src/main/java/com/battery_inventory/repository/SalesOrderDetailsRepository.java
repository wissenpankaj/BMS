package com.battery_inventory.repository;

import com.battery_inventory.entity.SalesOrder;
import com.battery_inventory.entity.SalesOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderDetailsRepository extends JpaRepository<SalesOrderDetails, Long> {

    List<SalesOrderDetails> findBySalesOrder(SalesOrder salesOrder);

    void deleteBySalesOrder(SalesOrder salesOrder);
}

