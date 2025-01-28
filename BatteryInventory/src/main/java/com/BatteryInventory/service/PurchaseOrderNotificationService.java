package com.BatteryInventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PurchaseOrderNotificationService {

    @Autowired
    private RestTemplate restTemplate;  // To make external API calls

    /**
     * Notify supplier about new purchase order by calling the supplier's API.
     */
    void notifySupplierAboutNewPurchaseOrder() {
        String supplierApiUrl = "http://localhost:8081/api/supplier/pending-purchase-orders"; // Adjust the port as needed
        try {
            // Make an API call to notify the supplier
            restTemplate.getForObject(supplierApiUrl, String.class);
        } catch (Exception e) {
            // Handle any potential exceptions like API downtime
            e.printStackTrace();
        }
    }
}
