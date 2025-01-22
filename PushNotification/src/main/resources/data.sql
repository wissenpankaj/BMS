-- Insert records into user_subscription table
INSERT INTO user_subscription (vehicle_id, notification_type, device_token, email_id) VALUES
('Vehicle-1', 'EMAIL', NULL, 'user1@example.com'),
('Vehicle-2', 'IOS_PUSH', 'token-1abc2def3ghi', NULL),
('Vehicle-3', 'ANDROID_PUSH', 'token-4jkl5mno6pqr', NULL),
('Vehicle-4', 'WEB_PUSH', 'token-7stu8vwx9yz0', NULL),
('Vehicle-5', 'EMAIL', NULL, 'user5@example.com'),
('Vehicle-6', 'IOS_PUSH', 'token-2ghi3jkl4mno', NULL),
('Vehicle-7', 'ANDROID_PUSH', 'token-5pqr6stu7vwx', NULL),
('Vehicle-8', 'WEB_PUSH', 'token-8yz90abc1def', NULL),
('Vehicle-9', 'EMAIL', NULL, 'user9@example.com'),
('Vehicle-10', 'IOS_PUSH', 'token-3mno4pqr5stu', NULL)
ON CONFLICT (vehicle_id) DO NOTHING;