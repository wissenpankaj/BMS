CREATE TABLE user_subscription (
    vehicle_id VARCHAR(255) NOT NULL PRIMARY KEY,
    notification_type VARCHAR(255),
    token VARCHAR(255),
    email_id VARCHAR(255)
);
