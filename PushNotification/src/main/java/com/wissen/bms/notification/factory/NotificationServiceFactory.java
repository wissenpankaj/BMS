package com.wissen.bms.notification.factory;

import com.wissen.bms.notification.service.AndroidPushNotificationService;
import com.wissen.bms.notification.service.EmailNotificationService;
import com.wissen.bms.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationServiceFactory {

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private com.wissen.bms.notification.service.iOSPushNotificationService iOSPushNotificationService;

    @Autowired
    private AndroidPushNotificationService androidPushNotificationService;

    public NotificationService getNotificationService(String type) {
        switch (type) {
            case "EMAIL":
                return emailNotificationService;
            case "IOS_PUSH":
                //return iOSPushNotificationService;
            case "ANDROID_PUSH":
                //return androidPushNotificationService;
            default:
                throw new IllegalArgumentException("Unsupported notification type: " + type);
        }
    }
}
