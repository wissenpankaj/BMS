package com.wissen.bms.notification.service.builders;

import com.wissen.bms.notification.model.NotificationType;
import com.wissen.bms.common.model.VehicleInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationContentBuilderRegistry {

    private final List<NotificationContentBuilder<? extends VehicleInfo>> builders;

    public NotificationContentBuilderRegistry(List<NotificationContentBuilder<? extends VehicleInfo>> builders) {
        this.builders = builders;
    }

    public <T extends VehicleInfo> NotificationContentBuilder<T> findBuilder(T vehicleInfoClass, NotificationType notificationType) {
        for (NotificationContentBuilder<? extends VehicleInfo> builder : builders) {
            if (builder.supports(vehicleInfoClass.getClass()) && builder.getNotificationType() == notificationType) {
                return (NotificationContentBuilder<T>) builder;
            }
        }
        throw new IllegalArgumentException("No builder found for type: " + vehicleInfoClass.getClass().getName() + " and notification type: " + notificationType);
    }
}