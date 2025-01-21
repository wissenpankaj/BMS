package com.wissen.bms.notification.service.builders.push;

import com.google.gson.JsonObject;
import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.notification.model.NotificationType;
import com.wissen.bms.common.model.VehicleInfo;
import com.wissen.bms.notification.service.builders.NotificationContentBuilder;
import org.springframework.stereotype.Component;

@Component
public class AndroidPushBatteryFaultContentBuilder implements NotificationContentBuilder<BatteryFault> {

    @Override
    public boolean supports(Class<? extends VehicleInfo> type) {
        return BatteryFault.class.isAssignableFrom(type);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.ANDROID_PUSH;  // Specify that this builder is for email notifications
    }

    @Override
    public String buildContent(BatteryFault data, String deviceToken) {
        JsonObject notification = new JsonObject();
        notification.addProperty("title", "Risk: " + data.getRisk());
        notification.addProperty("body", "Level: " + data.getLevel());

        JsonObject message = new JsonObject();
        message.add("notification", notification);
        message.addProperty("token", deviceToken); // Assuming a device token exists on the model

        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("batteryId", data.getBatteryId());
        dataObject.addProperty("vehicleId", data.getVehicleId());
        dataObject.addProperty("gps", data.getGps());
        dataObject.addProperty("faultReason", data.getFaultReason());
        dataObject.addProperty("recommendation", data.getRecommendation());
        dataObject.addProperty("timestamp", data.getTime());

        message.add("data", dataObject);

        JsonObject payload = new JsonObject();
        payload.add("message", message);

        return payload.toString();
    }
}

