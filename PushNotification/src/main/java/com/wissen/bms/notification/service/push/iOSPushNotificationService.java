package com.wissen.bms.notification.service.push;

import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.wissen.bms.notification.entity.UserSubscription;
import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.notification.model.NotificationResponse;
import com.wissen.bms.common.model.VehicleInfo;
import com.wissen.bms.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
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
	public <T extends VehicleInfo> ResponseEntity<NotificationResponse> sendNotification(T data, Optional<UserSubscription> subscription) {
		if (mockMode) {
			return sendMockNotification(data);
		} else {
			if(subscription.isPresent()){
				Optional<String> deviceToken = Optional.of(subscription.get().getToken());
				return sendRealNotification(deviceToken.orElse(""), data);
			}
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body(new NotificationResponse("error", "User subscription is not available."));
		}
	}

	private <T extends VehicleInfo> ResponseEntity<NotificationResponse> sendRealNotification(String deviceToken, T data) {
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

	private <T extends VehicleInfo> ResponseEntity<NotificationResponse> sendMockNotification(T data) {
		try {
			String mockData = null;
			if (data instanceof BatteryFault) {
				BatteryFault faultData = (BatteryFault) data;
				// Build mock response data
				mockData = String.format("Mock notification for Vehicle: %s, Battery: %s, GPS: %s",
						faultData.getVehicleId(), faultData.getBatteryId(), faultData.getGps());
			}
			return ResponseEntity.ok(new NotificationResponse("success", mockData));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new NotificationResponse("failure", "Error while mocking notification: " + e.getMessage()));
		}
	}

	private <T extends VehicleInfo> String buildPayload(T data) {
		BatteryFault faultData = null;
		if (data instanceof BatteryFault) {
			faultData = (BatteryFault) data;
		}
		return String.format("{\"aps\":{\"alert\":{\"title\":\"Alert for Vehicle: %s\", \"body\":\"Battery: %s, GPS: %s\"},\"sound\":\"default\"}," +
						"\"data\":{\"batteryId\":\"%s\",\"vehicleId\":\"%s\",\"gps\":\"%s\",\"timestamp\":\"%s\"}}",
				faultData.getVehicleId(), faultData.getBatteryId(), faultData.getGps(),
				faultData.getBatteryId(), faultData.getVehicleId(),
				faultData.getGps(), faultData.getTime());
	}
}
