package com.battery_inventory.repository;

import com.battery_inventory.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    Optional<SalesOrder> findByOrderId(String orderId);

    // Find all sales orders by customer ID
    @Query(value = "SELECT * FROM sales_order WHERE customer_id = :customerId", nativeQuery = true)
    List<SalesOrder> findByCustomerId(String customerId);

    // Find all sales orders by customer ID and status
    @Query(value = "SELECT * FROM sales_order WHERE customer_id = :customerId AND status = :status", nativeQuery = true)
    List<SalesOrder> findByCustomerIdAndStatus(String customerId, String status);

    // Find all sales orders by customer ID and a specific date range
    @Query(value = "SELECT * FROM sales_order WHERE customer_id = :customerId AND order_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<SalesOrder> findByCustomerIdAndOrderDateBetween(String customerId, LocalDate startDate, LocalDate endDate);

    // Find all sales orders by customer ID and start date (greater than or equal to the start date)
    @Query(value = "SELECT * FROM sales_order WHERE customer_id = :customerId AND order_date >= :startDate", nativeQuery = true)
    List<SalesOrder> findByCustomerIdAndOrderDateAfter(String customerId, LocalDate startDate);

    // Find all sales orders by customer ID and end date (less than or equal to the end date)
    @Query(value = "SELECT * FROM sales_order WHERE customer_id = :customerId AND order_date <= :endDate", nativeQuery = true)
    List<SalesOrder> findByCustomerIdAndOrderDateBefore(String customerId, LocalDate endDate);

    // Find all sales orders by order date range (start to end)
    @Query(value = "SELECT * FROM sales_order WHERE order_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<SalesOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);

    // Find all sales orders by a specific customer ID, status, and date range
    @Query(value = "SELECT * FROM sales_order WHERE customer_id = :customerId AND order_date BETWEEN :startDate AND :endDate AND status = :status", nativeQuery = true)
    List<SalesOrder> findByCustomerIdAndOrderDateBetweenAndStatus(String customerId, LocalDate startDate, LocalDate endDate, String status);

    // Find all sales orders by status
    @Query(value = "SELECT * FROM sales_order WHERE status = :status", nativeQuery = true)
    List<SalesOrder> findByStatus(String status);

}