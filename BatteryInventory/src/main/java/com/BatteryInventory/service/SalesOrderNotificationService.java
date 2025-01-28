package com.BatteryInventory.service;

import com.BatteryInventory.model.SalesOrder;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderNotificationService {

    /**
     * Sends a notification once the sales order is completed.
     */
    public void sendCompletionNotification(SalesOrder salesOrder) {
        // Implement the logic for sending notifications, e.g., email, SMS, or API call to UI.
        // Example: send email, webhook, etc.
        System.out.println("Sales order " + salesOrder.getId() + " is now completed and all batteries are allocated.");
    }
}
