package com.wissen.bms.notification.service;

import java.util.Optional;

import com.wissen.bms.notification.entity.UserSubscription;
import com.wissen.bms.notification.model.NotificationResponse;
import com.wissen.bms.common.model.VehicleInfo;
import org.springframework.http.ResponseEntity;


public interface NotificationService {
    <T extends VehicleInfo> ResponseEntity<NotificationResponse> sendNotification(T data, Optional<UserSubscription> subscription);
}
