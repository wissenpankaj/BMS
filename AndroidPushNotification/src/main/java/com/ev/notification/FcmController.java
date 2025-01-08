package com.ev.notification;

import com.ev.notification.dto.NotificationResponse;
import com.ev.notification.model.VehicleData;
import com.ev.notification.service.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class FcmController {

    @Autowired
    private FcmService fcmService;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> sendNotification(@RequestParam String deviceToken,
                                                                 @RequestBody VehicleData vehicleData) {
        try {
            return fcmService.sendNotification(deviceToken, vehicleData);
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();

            // Return failure response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new NotificationResponse("error", "Failed to send notification: " + e.getMessage())
            );
        }
    }
}

