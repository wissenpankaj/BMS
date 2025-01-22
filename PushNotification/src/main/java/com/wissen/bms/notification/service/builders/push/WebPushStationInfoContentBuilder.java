package com.wissen.bms.notification.service.builders.push;

import com.google.gson.JsonObject;
import com.wissen.bms.common.model.StationInfoDTO;
import com.wissen.bms.notification.model.NotificationType;
import com.wissen.bms.common.model.VehicleInfo;
import com.wissen.bms.notification.service.builders.NotificationContentBuilder;
import org.springframework.stereotype.Component;

@Component
public class WebPushStationInfoContentBuilder implements NotificationContentBuilder<StationInfoDTO> {

    @Override
    public boolean supports(Class<? extends VehicleInfo> type) {
        return StationInfoDTO.class.equals(type);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.WEB_PUSH;
    }

    @Override
    public String buildContent(StationInfoDTO data, String deviceToken) {
        // Create the notification JSON payload
        JsonObject notification = new JsonObject();
        notification.addProperty("title", "Station Alert: " + data.getStationName());
        notification.addProperty("body", "Available Stock: " + data.getAvailableStock());

        JsonObject message = new JsonObject();
        message.add("notification", notification);
        message.addProperty("token", deviceToken);

        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("vehicleId", data.getVehicleId());
        dataObject.addProperty("stationName", data.getStationName());
        dataObject.addProperty("availableStock", String.valueOf(data.getAvailableStock()));
        dataObject.addProperty("latitude", String.valueOf(data.getLatitude()));
        dataObject.addProperty("longitude", String.valueOf(data.getLongitude()));

        message.add("data", dataObject);

        JsonObject payload = new JsonObject();
        payload.add("message", message);

        return payload.toString();
    }
}

