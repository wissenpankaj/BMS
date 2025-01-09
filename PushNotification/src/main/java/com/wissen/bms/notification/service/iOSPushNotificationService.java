package com.wissen.bms.notification.service;

import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.wissen.bms.notification.entity.UserSubscription;
import com.wissen.bms.notification.model.NotificationResponse;
import com.wissen.bms.notification.model.VehicleData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Component
public class iOSPushNotificationService implements NotificationService {

	private final boolean mockMode;

	@Value("${apns.p8FilePath}")
	private String p8FilePath;

	@Value("${apns.teamId}")
	private String teamId;

	@Value("${apns.keyId}")
	private String keyId;

	@Value("${apns.topic}")
	private String topic;

	public iOSPushNotificationService(@Value("${apns.mockMode:false}") boolean mockMode) {
		this.mockMode = mockMode;
	}

	@Override
	public ResponseEntity<NotificationResponse> sendNotification(VehicleData data, Optional<UserSubscription> subscription, Optional<String> deviceToken) {
		if (mockMode) {
			return sendMockNotification(deviceToken.orElse("unknown_device"), data);
		} else {
			return sendRealNotification(deviceToken.orElse(""), data);
		}
	}

	private ResponseEntity<NotificationResponse> sendRealNotification(String deviceToken, VehicleData data) {
		try {
			// Build the payload
			String payload = buildPayload(data);

			// Create the push notification
			SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(deviceToken, topic, payload);

			// Initialize APNs client
			var apnsClient = new ApnsClientBuilder()
					.setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
					.setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(p8FilePath), teamId, keyId))
					.build();

			// Send the push notification
			PushNotificationResponse<SimpleApnsPushNotification> response = apnsClient.sendNotification(pushNotification).get();

			// Check the response
			if (response.isAccepted()) {
				return ResponseEntity.ok(new NotificationResponse("success", "Notification sent successfully!"));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new NotificationResponse("failure", "Notification rejected: " + response.getRejectionReason()));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new NotificationResponse("failure", "Error while sending notification: " + e.getMessage()));
		}
	}

	private ResponseEntity<NotificationResponse> sendMockNotification(String deviceToken, VehicleData data) {
		try {
			// Build mock response data
			String mockData = String.format("Mock notification for Vehicle: %s, Battery: %s, GPS: %s",
					data.getVehicleId(), data.getBatteryId(), data.getGps());

			return ResponseEntity.ok(new NotificationResponse("success", mockData));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new NotificationResponse("failure", "Error while mocking notification: " + e.getMessage()));
		}
	}

	private String buildPayload(VehicleData data) {
		return String.format("{\"aps\":{\"alert\":{\"title\":\"Alert for Vehicle: %s\", \"body\":\"Battery: %s, GPS: %s\"},\"sound\":\"default\"}," +
						"\"data\":{\"batteryId\":\"%s\",\"vehicleId\":\"%s\",\"gps\":\"%s\",\"timestamp\":\"%s\"}}",
				data.getVehicleId(), data.getBatteryId(), data.getGps(),
				data.getBatteryId(), data.getVehicleId(),
				data.getGps(), data.getTimestamp());
	}
}
