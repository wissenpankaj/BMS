package com.wissen.bms.notification.consumer;

import java.util.Optional;

import com.wissen.bms.notification.model.BatteryFault;
import com.wissen.bms.notification.service.NotificationService;
import com.wissen.bms.notification.factory.NotificationServiceFactory;
import com.wissen.bms.notification.repository.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import com.wissen.bms.notification.entity.UserSubscription;

//import io.reactivex.rxjava3.internal.subscriptions.SubscriptionHelper;


@Service

@EnableKafka
public class KafkaConsumer {
	@Autowired
	private NotificationServiceFactory notificationServiceFactory;

	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;

	// Define the Kafka topic name
	private static final String TOPIC_NAME = "faultalert";

	@KafkaListener(topics = TOPIC_NAME, groupId = "vehicle-group")
	public void listen(BatteryFault vehicleData) { // Check if the fault reason is not empty or null
		if (vehicleData.getFaultReason() != null && !vehicleData.getFaultReason().isEmpty()) { // Prepare the email
																								// content

			String subject = "Fault Alert for Vehicle " + vehicleData.getVehicleId();

			String body = "Fault Reason: " + vehicleData.getFaultReason() + "\nRisk Level: " + vehicleData.getRisk()
					+ "\nRecommendation: " + vehicleData.getRecommendation() + "\nTimestamp: "
					+ vehicleData.getTime();

			String recipientEmail = "RECIPIENT_EMAIL";    // Replace with actual emailaddress

			// Send email
			//emailService.sendEmail(recipientEmail, subject, body);

			//Dynamic decision to send notification based on subscription
		Optional<UserSubscription> subscription = userSubscriptionRepository.findById(vehicleData.getVehicleId());//Call to DB

            // Get the appropriate notification service and send the notification
            NotificationService notificationService =
                    notificationServiceFactory.getNotificationService(subscription.get().getNotificationType());
            notificationService.sendNotification(vehicleData, subscription, Optional.empty());
		}
	}
}
