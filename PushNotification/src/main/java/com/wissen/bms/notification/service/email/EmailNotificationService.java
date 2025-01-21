package com.wissen.bms.notification.service.email;

import java.util.Optional;

import com.wissen.bms.notification.entity.UserSubscription;
import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.notification.model.NotificationResponse;
import com.wissen.bms.notification.model.NotificationType;
import com.wissen.bms.common.model.VehicleInfo;
import com.wissen.bms.notification.service.NotificationService;
import com.wissen.bms.notification.service.builders.NotificationContentBuilder;
import com.wissen.bms.notification.service.builders.NotificationContentBuilderRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailNotificationService implements NotificationService {
    private final NotificationContentBuilderRegistry builderRegistry;
    private final JavaMailSender javaMailSender;

    // Constructor injection of JavaMailSender
    public EmailNotificationService(NotificationContentBuilderRegistry builderRegistry, JavaMailSender javaMailSender) {
        this.builderRegistry = builderRegistry;
        this.javaMailSender = javaMailSender;
    }

    // Method to send a notification email
    @Override
    public <T extends VehicleInfo> ResponseEntity<NotificationResponse> sendNotification(T data, Optional<UserSubscription> subscription) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            // Email recipient
            String toEmail = subscription.map(UserSubscription::getEmail_Id).orElseThrow(() -> new IllegalArgumentException("Email not provided"));

            // Find the appropriate content builder based on the vehicle data type and notification type
            NotificationContentBuilder<T> builder = builderRegistry.findBuilder(data, NotificationType.EMAIL);

            // Build the content for the notification
            String content = builder.buildContent(data, null);

            // Set email details
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("Battery Health Status");
            messageHelper.setText(content, true); // true indicates HTML content

            // Send the email
            javaMailSender.send(mimeMessage);
            System.out.println("Email sent successfully to " + toEmail);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new NotificationResponse("success", "Email sent successfully to " + toEmail));

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new NotificationResponse("error", "Failed to send email: " + e.getMessage()));
        }
    }

    public void sendHtmlEmail(String toEmail, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setTo(toEmail);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);  // true means the body is HTML

            javaMailSender.send(mimeMessage);
            System.out.println("HTML email sent successfully to " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send HTML email: " + e.getMessage());
        }
    }
    // Helper method to build the HTML email body
    private <T extends VehicleInfo> String buildHtmlBody(T data) {
        if (data instanceof BatteryFault faultData) {
            return String.format(
                    """
                    <html>
                    <body>
                        <p><strong>Battery Health Status</strong></p>
                        <p>
                            <strong>batteryId:</strong> %s<br>
                            <strong>vehicleId:</strong> %s<br>
                            <strong>gps:</strong> %s<br>
                            <strong>Risk:</strong> <span style="color: red;">%s</span><br>
                            <strong>Level:</strong> %s<br>
                            <strong>Recommendation:</strong> <span style="color: green;">%s</span><br>
                            <strong>Faultreason:</strong> <span style="color: red;">%s</span><br>
                            <strong>Timestamp:</strong> %s<br>
                        </p>
                    </body>
                    </html>
                    """,
                    faultData.getBatteryId(),
                    faultData.getVehicleId(),
                    faultData.getGps(),
                    faultData.getRisk(),
                    faultData.getLevel(),
                    faultData.getRecommendation(),
                    faultData.getFaultReason(),
                    faultData.getTime()
            );
        }
        throw new IllegalArgumentException("Unsupported data type: " + data.getClass().getName());
    }
}
