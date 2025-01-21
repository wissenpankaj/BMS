package com.wissen.bms.notification.factory;

import com.wissen.bms.notification.service.push.AndroidPushNotificationService;
import com.wissen.bms.notification.service.email.EmailNotificationService;
import com.wissen.bms.notification.service.NotificationService;
import com.wissen.bms.notification.service.push.WebPushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationServiceFactory {

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private com.wissen.bms.notification.service.push.iOSPushNotificationService iOSPushNotificationService;

    @Autowired
    private AndroidPushNotificationService androidPushNotificationService;

    @Autowired
    private WebPushNotificationService webPushNotificationService;

    public NotificationService getNotificationService(String type) {
        switch (type) {
            case "EMAIL":
                return emailNotificationService;
            case "IOS_PUSH":
                return iOSPushNotificationService;
            case "ANDROID_PUSH":
                return androidPushNotificationService;
            case "WEB_PUSH":
                return webPushNotificationService;
            default:
                throw new IllegalArgumentException("Unsupported notification type: " + type);
        }
    }
}
