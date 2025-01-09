package com.wissen.bms.notification.service;

import java.util.Optional;

import com.wissen.bms.notification.entity.UserSubscription;
import com.wissen.bms.notification.model.NotificationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.wissen.bms.notification.model.VehicleData;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender javaMailSender;

    // Constructor injection of JavaMailSender
    public EmailNotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // Method to send a simple email
    @Override
    public ResponseEntity<NotificationResponse> sendNotification(VehicleData data, Optional<UserSubscription> subscription, Optional<String> deviceToken) {
         try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(subscription.get().getEmail_Id());
             message.setSubject("data");
            message.setText("body");
             javaMailSender.send(message);
             System.out.println("Email sent successfully to " );
             return ResponseEntity.status(HttpStatus.OK)
                     .body(new NotificationResponse("success", "Email sent successfully to "));

         } catch (Exception e) {
             System.err.println("Failed to send email: " + e.getMessage());
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body(new NotificationResponse("error", "Failed to send email: " + e.getMessage()));
         }
    }

    // Method to send a MIME email (with HTML content, attachments, etc.)
    public void sendHtmlEmail(String toEmail, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setTo(toEmail);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);  // true means the body is HTML

            javaMailSender.send(mimeMessage);
            System.out.println("HTML email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send HTML email: " + e.getMessage());
        }
    }

	
}
