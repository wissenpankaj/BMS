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



-- Table for battery
CREATE TABLE battery (
    battery_id VARCHAR(50) PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    brand VARCHAR(50),
    capacity_in_kwh DECIMAL(10, 2), -- capacity in kilowatt-hours
    station_id VARCHAR(50),
    expiry_date DATE,
    price INT,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (station_id) REFERENCES station(station_id)
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

-- Insert into station
INSERT INTO station (station_id, name, location) VALUES
('S001', 'Station A', 'Location A'),
('S002', 'Station B', 'Location B'),
('S003', 'Station C', 'Location C');

-- Insert into location
INSERT INTO location (location_id, location_name, address) VALUES
('L001', 'Location A', 'Address A'),
('L002', 'Location B', 'Address B'),
('L003', 'Location C', 'Address C');

-- Insert into supplier
INSERT INTO supplier (supplier_id, supplier_name, contact_details, address) VALUES
('SP001', 'Supplier A', 'Contact A', 'Address A'),
('SP002', 'Supplier B', 'Contact B', 'Address B'),
('SP003', 'Supplier C', 'Contact C', 'Address C');

-- Insert into battery
INSERT INTO battery (battery_id, type, brand, capacity_in_kwh, station_id, expiry_date, price, status) VALUES
('BATTERY-0', 'Lithium-ion', 'Brand A', 50.00, 'S001', '2025-12-31', 5000, 'Available'),
('BATTERY-1', 'Lithium-ion', 'Brand B', 45.00, 'S002', '2025-11-30', 4500, 'Available'),
('BATTERY-2', 'Lithium-ion', 'Brand C', 55.00, 'S003', '2025-10-31', 5500, 'Available');

-- Insert into vehicle
INSERT INTO vehicle (vehicle_id, make, model, vehicle_type, battery_id) VALUES
('user123', 'Make A', 'Model A', 'Bike', 'BATTERY-0'),
('user456', 'Make B', 'Model B', 'Bike', 'BATTERY-1'),
('user789', 'Make C', 'Model C', 'Bike', 'BATTERY-2');

-- Insert into stock_management
INSERT INTO stock_management (stock_transaction_id, battery_id, transaction_date, quantity, transaction_type, supplier_id) VALUES
('ST001', 'BATTERY-0', '2023-01-15', 10, 'IN', 'SP001'),
('ST002', 'BATTERY-1', '2023-01-16', 5, 'IN', 'SP002'),
('ST003', 'BATTERY-2', '2023-01-17', 7, 'IN', 'SP003');

-- Insert into service_log
INSERT INTO service_log (service_log_id, battery_id, service_type, timestamp, details) VALUES
('SL001', 'BATTERY-0', 'Maintenance', '2023-01-15 10:00:00', 'Routine check'),
('SL002', 'BATTERY-1', 'Repair', '2023-01-16 11:00:00', 'Battery replacement'),
('SL003', 'BATTERY-2', 'Inspection', '2023-01-17 12:00:00', 'Battery inspection');

-- Insert into customer
INSERT INTO customer (customer_id, customer_name, email, phone, address) VALUES
('C001', 'Customer A', 'customerA@example.com', '1234567890', 'Address A'),
('C002', 'Customer B', 'customerB@example.com', '0987654321', 'Address B'),
('C003', 'Customer C', 'customerC@example.com', '1122334455', 'Address C');

-- Insert into sales_order
INSERT INTO sales_order (sales_order_id, customer_id, order_date, total_amount) VALUES
('SO001', 'C001', '2023-01-15', 5000.00),
('SO002', 'C002', '2023-01-16', 4500.00),
('SO003', 'C003', '2023-01-17', 5500.00);

-- Insert into sales_order_details
INSERT INTO sales_order_details (sales_order_detail_id, sales_order_id, battery_id, quantity, unit_price) VALUES
('SOD001', 'SO001', 'BATTERY-0', 1, 5000.00),
('SOD002', 'SO002', 'BATTERY-1', 1, 4500.00),
('SOD003', 'SO003', 'BATTERY-2', 1, 5500.00);

-- Insert into service_station
INSERT INTO service_station (service_station_id, service_station_name, address, contact_details) VALUES
('SS001', 'Service Station A', 'Address A', 'Contact A'),
('SS002', 'Service Station B', 'Address B', 'Contact B'),
('SS003', 'Service Station C', 'Address C', 'Contact C');

-- Insert into transaction
INSERT INTO transaction (transaction_id, battery_id, transaction_type, service_station_id, customer_id, supplier_id, created_at, created_by) VALUES
('T001', 'BATTERY-0', 'Purchase', 'SS001', 'C001', 'SP001', CURRENT_TIMESTAMP, 'UserA'),
('T002', 'BATTERY-1', 'Repair', 'SS002', 'C002', 'SP002', CURRENT_TIMESTAMP, 'UserB'),
('T003', 'BATTERY-2', 'Maintenance', 'SS003', 'C003', 'SP003', CURRENT_TIMESTAMP, 'UserC');

-- Insert into service_history
INSERT INTO service_history (service_id, battery_id, service_station_id, service_description, created_at, created_by) VALUES
('SH001', 'BATTERY-0', 'SS001', 'Battery maintenance', CURRENT_TIMESTAMP, 'UserA'),
('SH002', 'BATTERY-1', 'SS002', 'Battery repair', CURRENT_TIMESTAMP, 'UserB'),
('SH003', 'BATTERY-2', 'SS003', 'Battery inspection', CURRENT_TIMESTAMP, 'UserC');

-- Insert into battery_fault
INSERT INTO battery_fault (fault_id, vehicle_id, battery_id, gps, fault_reason, recommendation, level, risk, time) VALUES
('BF001', 'user123', 'BATTERY-0', 'GPS001', 'Overheating', 'Check cooling system', 'High', 'Medium', CURRENT_TIMESTAMP),
('BF002', 'user456', 'BATTERY-1', 'GPS002', 'Low Voltage', 'Replace battery', 'Low', 'Low', CURRENT_TIMESTAMP),
('BF003', 'user789', 'BATTERY-2', 'GPS003', 'Short Circuit', 'Inspect wiring', 'Critical', 'High', CURRENT_TIMESTAMP);

-- Insert into fault_log
INSERT INTO fault_log (fault_id, battery_id, fault_type, description, service_station_id, vehicle_id, created_at, created_by) VALUES
('FL001', 'BATTERY-0', 'Overheating', 'Battery temperature exceeded normal levels', 'SS001', 'user123', CURRENT_TIMESTAMP, 'UserA'),
('FL002', 'BATTERY-1', 'Low Voltage', 'Battery voltage below threshold', 'SS002', 'user456', CURRENT_TIMESTAMP, 'UserB'),
('FL003', 'BATTERY-2', 'Short Circuit', 'Battery short circuited', 'SS003', 'user789', CURRENT_TIMESTAMP, 'UserC');

-- Insert into users
INSERT INTO users (user_id, username, password, role) VALUES
('U001', 'admin', 'password', 'Admin'),
('U002', 'customerA', 'password', 'Customer'),
('U003', 'userC', 'password', 'Customer');

-- Insert into user_token
INSERT INTO user_token (token_id, user_id, token, expiry) VALUES
('T001', 'U001', 'token123', '2023-12-31 23:59:59'),
('T002', 'U002', 'token456', '2023-12-31 23:59:59'),
('T003', 'U003', 'token789', '2023-12-31 23:59:59');
