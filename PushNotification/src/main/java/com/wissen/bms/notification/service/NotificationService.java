package com.wissen.bms.notification.service;

import java.util.Optional;

import com.wissen.bms.notification.entity.UserSubscription;
import com.wissen.bms.notification.model.NotificationResponse;
import com.wissen.bms.notification.model.VehicleData;
import org.springframework.http.ResponseEntity;


public interface NotificationService {
    ResponseEntity<NotificationResponse> sendNotification(VehicleData data, Optional<UserSubscription> subscription, Optional<String> deviceToken);
}
