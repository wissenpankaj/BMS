package com.wissen.bms.notification.service.builders.email;

import com.wissen.bms.common.model.StationInfoDTO;
import com.wissen.bms.notification.model.NotificationType;
import com.wissen.bms.common.model.VehicleInfo;
import com.wissen.bms.notification.service.builders.NotificationContentBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailStationInfoContentBuilder implements NotificationContentBuilder<StationInfoDTO> {

    @Override
    public boolean supports(Class<? extends VehicleInfo> type) {
        return StationInfoDTO.class.isAssignableFrom(type);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.EMAIL; // Specify that this builder is for email notifications
    }

    @Override
    public String buildContent(StationInfoDTO data, String deviceToken) {
        return String.format(
                """
                <html>
                <body>
                    <p><strong>Station Information</strong></p>
                    <p>
                        <strong>Vehicle ID:</strong> %s<br>
                        <strong>Station Name:</strong> %s<br>
                        <strong>Available Stock:</strong> %d<br>
                        <strong>Location:</strong> Latitude %f, Longitude %f<br>
                    </p>
                </body>
                </html>
                """,
                data.getVehicleId(),
                data.getStationName(),
                data.getAvailableStock(),
                data.getLatitude(),
                data.getLongitude()
        );
    }
}

