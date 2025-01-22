package com.wissen.bms.notification.consumer;

import java.util.Optional;

import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.common.model.StationInfoDTO;
import com.wissen.bms.common.model.VehicleInfo;
import com.wissen.bms.notification.service.NotificationService;
import com.wissen.bms.notification.factory.NotificationServiceFactory;
import com.wissen.bms.notification.repository.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import com.wissen.bms.notification.entity.UserSubscription;

@Service
@EnableKafka
public class KafkaConsumer {

	@Autowired
	private NotificationServiceFactory notificationServiceFactory;

	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;

	// Define Kafka topic names
	private static final String FAULT_ALERT_TOPIC = "faultalert";
	private static final String NEAR_STATION_TOPIC = "nearstation";

	// Listener for Fault Alert Topic
	@KafkaListener(topics = FAULT_ALERT_TOPIC, groupId = "vehicle-group")
	public void listenFaultAlert(BatteryFault batteryFault) {
		handleMessage(batteryFault);
	}

	// Listener for Other Topic
	@KafkaListener(topics = NEAR_STATION_TOPIC, groupId = "vehicle-group")
	public void listenOtherTopic(StationInfoDTO stationInfo) {
		handleMessage(stationInfo);
	}

	// Generic method to process messages
	private void handleMessage(VehicleInfo vehicleInfo) {
		try {
			String vehicleId = vehicleInfo.getVehicleId();
			if (vehicleId == null || vehicleId.isEmpty()) {
				throw new IllegalArgumentException("Vehicle ID is missing in the message");
			}

			System.out.println("Received data for vehicleId: " + vehicleId + ", message: " + vehicleInfo);

			// Fetch all subscriptions for the vehicleId
			Iterable<UserSubscription> subscriptions = userSubscriptionRepository.findAllByVehicleId(vehicleId);
			for (UserSubscription subscription : subscriptions) {
				NotificationService notificationService =
						notificationServiceFactory.getNotificationService(subscription.getNotificationType());
				notificationService.sendNotification(vehicleInfo, Optional.of(subscription));
			}
		} catch (Exception e) {
			System.err.println("Error processing message: " + e.getMessage());
			// Log error or send to a Dead Letter Queue (DLQ)
		}
	}
}
