package com.wissen.bms.notification.service;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.notification.model.BatteryFault1;
import com.wissen.bms.notification.model.NotificationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.wissen.bms.notification.entity.UserSubscription;

@Component
public class AndroidPushNotificationService implements NotificationService {
	private static final String FCM_ENDPOINT = "https://fcm.googleapis.com/v1/projects/ev-battery-fault-detection/messages:send";

	@Value("${firebase.credentials.path}")
	private String credentialsPath;

	@Value("${mock:false}")
	private boolean mock;

	/**
	 * Sends a notification to a device using Firebase Cloud Messaging (FCM).
	 *
	 * @param vehicleData The vehicle data object containing the information for the notification.
	 * @return ResponseEntity with the status and message.
	 * @throws Exception If an error occurs while sending the notification.
	 */
	@Override
	public ResponseEntity<NotificationResponse> sendNotification(BatteryFault vehicleData, Optional<UserSubscription> subscription) {
		if (mock) {
			// Mock Mode
			return simulateNotification(vehicleData);
		} else {
			// Real Mode
            try {
				if(subscription.isPresent()){
					Optional<String> deviceToken = Optional.of(subscription.get().getToken());
					return sendRealNotification(deviceToken, vehicleData);
				}
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
						.body(new NotificationResponse("error", "User subscription is not available."));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
	}

	/**
	 * Simulates sending a notification (for mock mode).
	 *
	 * @param vehicleData The vehicle data object containing the information for the notification.
	 * @return ResponseEntity with the mock success message.
	 */
	private ResponseEntity<NotificationResponse> simulateNotification(BatteryFault vehicleData) {
		System.out.println("Mock Mode Enabled: Simulating notification sending...");
		System.out.println("Vehicle Data: " + vehicleData);

		// Mock Response: Return a mock notification response
		String mockResponse = buildPayload(vehicleData);
		return ResponseEntity.status(HttpStatus.OK).body(new NotificationResponse("success", mockResponse));
	}

	/**
	 * Sends a real notification using Firebase's HTTP v1 API.
	 *
	 * @param deviceToken The target device's FCM token.
	 * @param vehicleData The vehicle data object containing the information for the notification.
	 * @return ResponseEntity with the real notification response.
	 * @throws Exception If an error occurs while sending the notification.
	 */
	private ResponseEntity<NotificationResponse> sendRealNotification(Optional<String> deviceToken, BatteryFault vehicleData) throws Exception {
		if(deviceToken.isPresent()) {
			// Load Google Credentials
			GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath))
					.createScoped("https://www.googleapis.com/auth/firebase.messaging");

			// Refresh token if expired
			googleCredentials.refreshIfExpired();
			String accessToken = googleCredentials.getAccessToken().getTokenValue();

			// Build JSON payload
			JsonObject notification = new JsonObject();
			notification.addProperty("title", "Risk: " + vehicleData.getRisk());
			notification.addProperty("body", "Level: " + vehicleData.getLevel());

			JsonObject message = new JsonObject();
			message.add("notification", notification);
			message.addProperty("token", deviceToken.get());

			JsonObject data = new JsonObject();
			data.addProperty("batteryId", vehicleData.getBatteryId());
			data.addProperty("vehicleId", vehicleData.getVehicleId());
			data.addProperty("gps", vehicleData.getGps());
			data.addProperty("faultReason", vehicleData.getFaultReason());
			data.addProperty("recommendation", vehicleData.getRecommendation());
			data.addProperty("timestamp", vehicleData.getTime());

			message.add("data", data);

			JsonObject payload = new JsonObject();
			payload.add("message", message);

			// Send HTTP request
			URL url = new URL(FCM_ENDPOINT);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);
			connection.setRequestProperty("Content-Type", "application/json; UTF-8");
			connection.setDoOutput(true);

			try (OutputStream os = connection.getOutputStream()) {
				os.write(payload.toString().getBytes("UTF-8"));
			}

			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				// Read the response from FCM server
				StringBuilder response = new StringBuilder();
				try (InputStreamReader in = new InputStreamReader(connection.getInputStream())) {
					int charRead;
					while ((charRead = in.read()) != -1) {
						response.append((char) charRead);
					}
				}

				// Convert the response into a JSON object and return the response from FCM
				JsonObject fcmResponse = new JsonParser().parse(response.toString()).getAsJsonObject();

				// Return the response from Firebase (status and message ID)
				return ResponseEntity.status(HttpStatus.OK)
						.body(new NotificationResponse("success", fcmResponse.toString()));
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new NotificationResponse("error", "Failed to send notification. HTTP error code: " + responseCode));
			}
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(new NotificationResponse("error", "Device token is missing for the specified notification type."));
	}

	/**
	 * Builds the notification payload.
	 *
	 * @param vehicleData The vehicle data object.
	 * @return The formatted payload string.
	 */
	private String buildPayload(BatteryFault vehicleData) {
		return String.format("{\"alert\":{\"title\":\"Risk: %s\", \"body\":\"Level: %s\"},\"sound\":\"default\"}," +
						"\"data\":{\"batteryId\":\"%s\",\"vehicleId\":\"%s\",\"gps\":\"%s\",\"faultReason\":\"%s\"," +
						"\"recommendation\":\"%s\",\"timestamp\":\"%s\"}",
				vehicleData.getRisk(), vehicleData.getLevel(),
				vehicleData.getBatteryId(), vehicleData.getVehicleId(),
				vehicleData.getGps(), vehicleData.getFaultReason(),
				vehicleData.getRecommendation(), vehicleData.getTime());
	}

}
