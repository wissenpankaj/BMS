package com.example.ev_station_management.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyDriver(String vehicleId, String message) {
        // Simulate sending a notification (e.g., SMS, push notification)
        System.out.println("Notification sent to Vehicle ID: " + vehicleId);
        System.out.println("Message: " + message);
    }
}
