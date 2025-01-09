package com.wissen.bms.notification.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.wissen.bms.notification.entity.UserSubscription;
import com.wissen.bms.notification.model.VehicleData;
import com.wissen.bms.notification.model.NotificationResponse;

@Component
public class iOSPushNotificationService implements NotificationService{

	@Override
	public ResponseEntity<NotificationResponse> sendNotification(VehicleData data, Optional<UserSubscription> subscription, Optional<String> deviceToken) {
		// TODO Auto-generated method stub
		return ResponseEntity.status(HttpStatus.OK)
				.body(new NotificationResponse("success", "Notification sent successfully"));
	}

}
