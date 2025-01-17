-- Table for station
CREATE TABLE station (
    station_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

-- Table for location
CREATE TABLE location (
    location_id VARCHAR(50) PRIMARY KEY,
    location_name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);




-- Table for vehicle
CREATE TABLE vehicle (
    vehicle_id VARCHAR(50) PRIMARY KEY,
    make VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    vehicle_type VARCHAR(50),
    battery_id VARCHAR(50) REFERENCES battery(battery_id)
);


-- Table for supplier
CREATE TABLE supplier (
    supplier_id VARCHAR(50) PRIMARY KEY,
    supplier_name VARCHAR(255) NOT NULL,
    contact_details VARCHAR(255),
    address VARCHAR(255)
);

-- Table for stock_management
CREATE TABLE stock_management (
    stock_transaction_id VARCHAR(50) PRIMARY KEY,
    battery_id VARCHAR(50),
    transaction_date DATE NOT NULL,
    quantity INT NOT NULL,
    transaction_type VARCHAR(50), -- e.g., "IN", "OUT"
    supplier_id VARCHAR(50),
    FOREIGN KEY (battery_id) REFERENCES battery(battery_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id)
);

-- Table for service_log
CREATE TABLE service_log (
    service_log_id VARCHAR(50) PRIMARY KEY,
    battery_id VARCHAR(50),
    service_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL, -- Use TIMESTAMP instead of DATETIME
    details VARCHAR(255),
    FOREIGN KEY (battery_id) REFERENCES battery(battery_id)
);

-- Table for customer
CREATE TABLE customer (
    customer_id VARCHAR(50) PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    address VARCHAR(255)
);

-- Table for sales_order
CREATE TABLE sales_order (
    sales_order_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50),
    order_date DATE NOT NULL,
    total_amount DECIMAL(10, 2),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Table for sales_order_details
CREATE TABLE sales_order_details (
    sales_order_detail_id VARCHAR(50) PRIMARY KEY,
    sales_order_id VARCHAR(50),
    battery_id VARCHAR(50),
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2),
    FOREIGN KEY (sales_order_id) REFERENCES sales_order(sales_order_id),
    FOREIGN KEY (battery_id) REFERENCES battery(battery_id)
);

-- Table for service_station
CREATE TABLE service_station (
    service_station_id VARCHAR(50) PRIMARY KEY,
    service_station_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    contact_details VARCHAR(255)
);

-- Table for transaction
CREATE TABLE transaction (
    transaction_id VARCHAR(50) PRIMARY KEY,
    battery_id VARCHAR(50),
    transaction_type VARCHAR(50),
    service_station_id VARCHAR(50),
    customer_id VARCHAR(50),
    supplier_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    FOREIGN KEY (battery_id) REFERENCES battery(battery_id),
    FOREIGN KEY (service_station_id) REFERENCES service_station(service_station_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id)
);

-- Table for service_history
CREATE TABLE service_history (
    service_id VARCHAR(50) PRIMARY KEY,
    battery_id VARCHAR(50),
    service_station_id VARCHAR(50),
    service_description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    FOREIGN KEY (battery_id) REFERENCES battery(battery_id),
    FOREIGN KEY (service_station_id) REFERENCES service_station(service_station_id)
);

--Table for Battery_Fault
CREATE TABLE battery_fault (
    fault_id VARCHAR(50) PRIMARY KEY,
    vehicle_id VARCHAR(50),
    battery_id VARCHAR(50),
    gps VARCHAR(50),
    fault_reason VARCHAR(50),
    recommendation VARCHAR(50),
    level VARCHAR(50),
    risk VARCHAR(50),
    time TIMESTAMP,
    FOREIGN KEY (battery_id) REFERENCES battery(battery_id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)
);

-- Table for fault_log
CREATE TABLE fault_log (
    fault_id VARCHAR(50) PRIMARY KEY,
    battery_id VARCHAR(50),
    fault_type VARCHAR(50),
    description TEXT,
    service_station_id VARCHAR(50),
    vehicle_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    FOREIGN KEY (battery_id) REFERENCES battery(battery_id),
    FOREIGN KEY (service_station_id) REFERENCES service_station(service_station_id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)
);

-- Table for user
CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) -- e.g., Admin, Customer
);

-- Table for user_token
CREATE TABLE user_token (
    token_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50),
    token VARCHAR(255),
    expiry TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Insert into location
INSERT INTO location (location_id, location_name, address) VALUES
('loc001', 'Location A', '123 ABC Street'),
('loc002', 'Location B', '456 DEF Street'),
('loc003', 'Location C', '789 GHI Street'),
('loc004', 'Location D', '101 JKL Street'),
('loc005', 'Location E', '102 MNO Street');

-- Insert into station
INSERT INTO station (station_id, name, location) VALUES
('stat001', 'Station 1', 'Location A'),
('stat002', 'Station 2', 'Location B'),
('stat003', 'Station 3', 'Location C'),
('stat004', 'Station 4', 'Location D'),
('stat005', 'Station 5', 'Location E');


-- Insert into battery
INSERT INTO battery (battery_id, type, brand, capacity_in_kwh, station_id, expiry_date, price, status) VALUES
('BATTERY-0', 'Type A', 'Brand X', 100, 'stat001', '2025-12-31', 5000, 'Available'),
('BATTERY-1', 'Type B', 'Brand Y', 200, 'stat002', '2026-12-31', 6000, 'Available'),
('BATTERY-2', 'Type C', 'Brand Z', 300, 'stat003', '2027-12-31', 7000, 'Available');

-- Insert into vehicle
INSERT INTO vehicle (vehicle_id, make, model, vehicle_type, battery_id) VALUES
('user123', 'Make A', 'Model A', 'Bike', 'BATTERY-0'),
('user456', 'Make B', 'Model B', 'Bike', 'BATTERY-1'),
('user789', 'Make C', 'Model C', 'Bike', 'BATTERY-2');


-- Insert into supplier
INSERT INTO supplier (supplier_id, supplier_name, contact_details, address) VALUES
('sup001', 'Supplier A', 'contactA@example.com', '123 XYZ Street'),
('sup002', 'Supplier B', 'contactB@example.com', '456 XYZ Street'),
('sup003', 'Supplier C', 'contactC@example.com', '789 XYZ Street'),
('sup004', 'Supplier D', 'contactD@example.com', '101 XYZ Street'),
('sup005', 'Supplier E', 'contactE@example.com', '102 XYZ Street');

-- Insert into stock_management
INSERT INTO stock_management (stock_transaction_id, battery_id, transaction_date, quantity, transaction_type, supplier_id) VALUES
('stk001', 'BATTERY-0', '2025-01-01', 10, 'IN', 'sup001'),
('stk002', 'BATTERY-1', '2025-01-02', 20, 'IN', 'sup002'),
('stk003', 'BATTERY-2', '2025-01-03', 30, 'IN', 'sup003');

-- Insert into service_log
INSERT INTO service_log (service_log_id, battery_id, service_type, timestamp, details) VALUES
('srv001', 'BATTERY-0', 'Checkup', '2025-01-01 10:00:00', 'Routine checkup'),
('srv002', 'BATTERY-1', 'Maintenance', '2025-01-02 11:00:00', 'Battery maintenance'),
('srv003', 'BATTERY-2', 'Replacement', '2025-01-03 12:00:00', 'Battery replacement');

-- Insert into customer
INSERT INTO customer (customer_id, customer_name, email, phone, address) VALUES
('cust001', 'Customer A', 'customerA@example.com', '1234567890', '123 ABC Street'),
('cust002', 'Customer B', 'customerB@example.com', '0987654321', '456 DEF Street'),
('cust003', 'Customer C', 'customerC@example.com', '1111111111', '789 GHI Street'),
('cust004', 'Customer D', 'customerD@example.com', '2222222222', '101 JKL Street'),
('cust005', 'Customer E', 'customerE@example.com', '3333333333', '102 MNO Street');

-- Insert into sales_order
INSERT INTO sales_order (sales_order_id, customer_id, order_date, total_amount) VALUES
('order001', 'cust001', '2025-01-01', 1000.00),
('order002', 'cust002', '2025-01-02', 2000.00),
('order003', 'cust003', '2025-01-03', 3000.00);

-- Insert into sales_order_details
INSERT INTO sales_order_details (sales_order_detail_id, sales_order_id, battery_id, quantity, unit_price) VALUES
('ordd001', 'order001', 'BATTERY-0', 1, 1000.00),
('ordd002', 'order002', 'BATTERY-1', 2, 1000.00),
('ordd003', 'order003', 'BATTERY-2', 3, 1000.00);

-- Insert into service_station
INSERT INTO service_station (service_station_id, service_station_name, address, contact_details) VALUES
('ss001', 'Service Station 1', '123 ABC Street', 'contact1@example.com'),
('ss002', 'Service Station 2', '456 DEF Street', 'contact2@example.com'),
('ss003', 'Service Station 3', '789 GHI Street', 'contact3@example.com');

-- Insert into transaction
INSERT INTO transaction (transaction_id, battery_id, transaction_type, service_station_id, customer_id, supplier_id, created_by) VALUES
('trans001', 'BATTERY-0', 'Sale', 'ss001', 'cust001', 'sup001', 'User1'),
('trans002', 'BATTERY-1', 'Service', 'ss002', 'cust002', 'sup002', 'User2'),
('trans003', 'BATTERY-2', 'Return', 'ss003', 'cust003', 'sup003', 'User3');

-- Insert into service_history
INSERT INTO service_history (service_id, battery_id, service_station_id, service_description, created_by) VALUES
('servh001', 'BATTERY-0', 'ss001', 'Battery checkup', 'User1'),
('servh002', 'BATTERY-1', 'ss002', 'Battery maintenance', 'User2'),
('servh003', 'BATTERY-2', 'ss003', 'Battery replacement', 'User3');

-- Insert into battery_fault
INSERT INTO battery_fault (fault_id, vehicle_id, battery_id, gps, fault_reason, recommendation, level, risk, time) VALUES
('fault001', 'user456', 'BATTERY-0', 'GPS-001', 'Overheating', 'Check temperature', 'High', 'Severe', '2025-01-01 10:00:00'),
('fault002', 'user123', 'BATTERY-1', 'GPS-002', 'Low Capacity', 'Check battery life', 'Medium', 'Moderate', '2025-01-02 11:00:00'),
('fault003', 'user789', 'BATTERY-2', 'GPS-003', 'Leakage', 'Inspect for leaks', 'High', 'Severe', '2025-01-03 12:00:00');

-- Insert into fault_log
INSERT INTO fault_log (fault_id, battery_id, fault_type, description, service_station_id, vehicle_id, created_by) VALUES
('fault001', 'BATTERY-0', 'Overheating', 'Battery overheating issue', 'ss001', 'user456', 'User1'),
('fault002', 'BATTERY-1', 'Low Capacity', 'Battery low capacity issue', 'ss002', 'user123', 'User2'),
('fault003', 'BATTERY-2', 'Leakage', 'Battery leakage issue', 'ss003', 'user789', 'User3');

-- Insert into users
INSERT INTO users (user_id, username, password, role) VALUES
('user001', 'userA', 'passwordA', 'Admin'),
('user002', 'userB', 'passwordB', 'Customer'),
('user003', 'userC', 'passwordC', 'Customer'),
('user004', 'userD', 'passwordD', 'Customer'),
('user005', 'userE', 'passwordE', 'Admin');

-- Insert into user_token
INSERT INTO user_token (token_id, user_id, token, expiry) VALUES
('token001', 'user001', 'tokenA', '2025-12-31 23:59:59'),
('token002', 'user002', 'tokenB', '2025-12-31 23:59:59'),
('token003', 'user003', 'tokenC', '2025-12-31 23:59:59'),
('token004', 'user004', 'tokenD', '2025-12-31 23:59:59'),
('token005', 'user005', 'tokenE', '2025-12-31 23:59:59');

-- 1) Create purchase_orders table
CREATE TABLE IF NOT EXISTS purchase_orders (
    id SERIAL PRIMARY KEY,
    purchase_order_id VARCHAR(50) UNIQUE NOT NULL,
    station_id        VARCHAR(50),
    battery_type      VARCHAR(50),
    quantity          INT,
    status            VARCHAR(50),
    order_date        TIMESTAMP,
    expected_delivery_date TIMESTAMP
);

-- 2) Create batteries table
CREATE TABLE IF NOT EXISTS batteries (
    id SERIAL PRIMARY KEY,
    battery_id     VARCHAR(50) UNIQUE NOT NULL,
    type           VARCHAR(50),
    serial_number  VARCHAR(100),
    status         VARCHAR(50),
    purchase_order_id INT,
    CONSTRAINT fk_purchase_order
        FOREIGN KEY (purchase_order_id)
        REFERENCES purchase_orders (id)
        ON DELETE CASCADE
);

