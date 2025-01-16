CREATE TABLE IF NOT EXISTS user_subscription (
    vehicle_id VARCHAR(255) NOT NULL PRIMARY KEY,
    notification_type VARCHAR(255),
    device_token VARCHAR(255),
    email_id VARCHAR(255)
);
