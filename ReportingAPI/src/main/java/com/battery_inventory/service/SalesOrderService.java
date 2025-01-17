package com.battery_inventory.service;


import com.battery_inventory.dto.SalesOrderRequest;
import com.battery_inventory.entity.SalesOrder;

import java.util.List;

public interface SalesOrderService {
    String createSalesOrder(SalesOrderRequest request);

    List<SalesOrder> getSalesOrders(String customerId, String startDate, String endDate, String status);

    void updateSalesOrder(String orderId, SalesOrderRequest request);

    void cancelSalesOrder(String orderId);

}