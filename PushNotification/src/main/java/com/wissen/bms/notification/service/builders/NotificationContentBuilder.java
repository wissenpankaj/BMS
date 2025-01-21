package com.wissen.bms.notification.service.builders;

import com.wissen.bms.notification.model.NotificationType;
import com.wissen.bms.common.model.VehicleInfo;

public interface NotificationContentBuilder<T extends VehicleInfo> {

    /**
     * Determines if this builder supports the given VehicleInfo type.
     *
     * @param type The class of the VehicleInfo.
     * @return True if supported, false otherwise.
     */
    boolean supports(Class<? extends VehicleInfo> type);

    /**
     * Builds the content for the given data.
     *
     * @param data The VehicleInfo data to build the content for.
     * @return The built content as a String.
     */
    String buildContent(T data, String deviceToken);

    NotificationType getNotificationType();
}
