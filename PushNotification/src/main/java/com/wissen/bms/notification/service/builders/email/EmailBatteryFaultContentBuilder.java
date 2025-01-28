package com.wissen.bms.notification.service.builders.email;

import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.notification.model.NotificationType;
import com.wissen.bms.common.model.VehicleInfo;
import com.wissen.bms.notification.service.builders.NotificationContentBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailBatteryFaultContentBuilder implements NotificationContentBuilder<BatteryFault> {

    @Override
    public boolean supports(Class<? extends VehicleInfo> type) {
        return BatteryFault.class.isAssignableFrom(type);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.EMAIL;  // Specify that this builder is for email notifications
    }

    @Override
    public String buildContent(BatteryFault data, String deviceToken) {
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
                data.getBatteryId(),
                data.getVehicleId(),
                data.getGps(),
                data.getRisk(),
                data.getLevel(),
                data.getRecommendation(),
                data.getFaultReason(),
                data.getTime()
        );
    }
}

