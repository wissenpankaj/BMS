package com.wissen.bms.notification.service;

import java.util.Optional;

import com.wissen.bms.notification.entity.UserSubscription;
import com.wissen.bms.notification.model.BatteryFault;
import com.wissen.bms.notification.model.NotificationResponse;
import org.springframework.http.ResponseEntity;


public interface NotificationService {
    ResponseEntity<NotificationResponse> sendNotification(BatteryFault data, Optional<UserSubscription> subscription, Optional<String> deviceToken);
}
