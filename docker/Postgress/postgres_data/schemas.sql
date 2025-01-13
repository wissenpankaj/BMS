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
    vehicle_name VARCHAR(255) NOT NULL,
    vehicle_type VARCHAR(50)
);


-- Table for battery
CREATE TABLE battery (
    battery_id VARCHAR(50) PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    brand VARCHAR(50),
    capacity INT, -- in watt-hours or any other unit
    location_id VARCHAR(50),
    expiry_date DATE,
    price INT,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (location_id) REFERENCES station(station_id)
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
    battery_fault_id VARCHAR(50) PRIMARY KEY,
    vehicle_id VARCHAR(50),
    battery_id VARCHAR(50),
    gps VARCHAR(50),
    fault_reason VARCHAR(50),
    recommendation VARCHAR(50),
    risk_level VARCHAR(50),
    risk VARCHAR(50),
    fault_time TIMESTAMP,
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

-- Insert into vehicle
INSERT INTO vehicle (vehicle_id, vehicle_name, vehicle_type) VALUES
('veh001', 'Vehicle A', 'Type 1'),
('veh002', 'Vehicle B', 'Type 2'),
('veh003', 'Vehicle C', 'Type 3'),
('veh004', 'Vehicle D', 'Type 4'),
('veh005', 'Vehicle E', 'Type 5');

-- Insert into battery
INSERT INTO battery (battery_id, type, brand, capacity, location_id, expiry_date, price, status) VALUES
('bat001', 'Type A', 'Brand X', 100, 'stat001', '2025-12-31', 5000, 'Available'),
('bat002', 'Type B', 'Brand Y', 200, 'stat002', '2026-12-31', 6000, 'Available'),
('bat003', 'Type C', 'Brand Z', 300, 'stat003', '2027-12-31', 7000, 'Available'),
('bat004', 'Type D', 'Brand X', 400, 'stat004', '2028-12-31', 8000, 'Available'),
('bat005', 'Type E', 'Brand Y', 500, 'stat005', '2029-12-31', 9000, 'Available');

-- Insert into supplier
INSERT INTO supplier (supplier_id, supplier_name, contact_details, address) VALUES
('sup001', 'Supplier A', 'contactA@example.com', '123 XYZ Street'),
('sup002', 'Supplier B', 'contactB@example.com', '456 XYZ Street'),
('sup003', 'Supplier C', 'contactC@example.com', '789 XYZ Street'),
('sup004', 'Supplier D', 'contactD@example.com', '101 XYZ Street'),
('sup005', 'Supplier E', 'contactE@example.com', '102 XYZ Street');

-- Insert into stock_management
INSERT INTO stock_management (stock_transaction_id, battery_id, transaction_date, quantity, transaction_type, supplier_id) VALUES
('stk001', 'bat001', '2025-01-01', 10, 'IN', 'sup001'),
('stk002', 'bat002', '2025-01-02', 20, 'IN', 'sup002'),
('stk003', 'bat003', '2025-01-03', 30, 'IN', 'sup003'),
('stk004', 'bat004', '2025-01-04', 40, 'IN', 'sup004'),
('stk005', 'bat005', '2025-01-05', 50, 'IN', 'sup005');

-- Insert into service_log
INSERT INTO service_log (service_log_id, battery_id, service_type, timestamp, details) VALUES
('srv001', 'bat001', 'Checkup', '2025-01-01 10:00:00', 'Routine checkup'),
('srv002', 'bat002', 'Maintenance', '2025-01-02 11:00:00', 'Battery maintenance'),
('srv003', 'bat003', 'Replacement', '2025-01-03 12:00:00', 'Battery replacement'),
('srv004', 'bat004', 'Repair', '2025-01-04 13:00:00', 'Battery repair'),
('srv005', 'bat005', 'Inspection', '2025-01-05 14:00:00', 'Battery inspection');

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
('order003', 'cust003', '2025-01-03', 3000.00),
('order004', 'cust004', '2025-01-04', 4000.00),
('order005', 'cust005', '2025-01-05', 5000.00);

-- Insert into sales_order_details
INSERT INTO sales_order_details (sales_order_detail_id, sales_order_id, battery_id, quantity, unit_price) VALUES
('ordd001', 'order001', 'bat001', 1, 1000.00),
('ordd002', 'order002', 'bat002', 2, 1000.00),
('ordd003', 'order003', 'bat003', 3, 1000.00),
('ordd004', 'order004', 'bat004', 4, 1000.00),
('ordd005', 'order005', 'bat005', 5, 1000.00);

-- Insert into service_station
INSERT INTO service_station (service_station_id, service_station_name, address, contact_details) VALUES
('ss001', 'Service Station 1', '123 ABC Street', 'contact1@example.com'),
('ss002', 'Service Station 2', '456 DEF Street', 'contact2@example.com'),
('ss003', 'Service Station 3', '789 GHI Street', 'contact3@example.com'),
('ss004', 'Service Station 4', '101 JKL Street', 'contact4@example.com'),
('ss005', 'Service Station 5', '102 MNO Street', 'contact5@example.com');

-- Insert into transaction
INSERT INTO transaction (transaction_id, battery_id, transaction_type, service_station_id, customer_id, supplier_id, created_by) VALUES
('trans001', 'bat001', 'Sale', 'ss001', 'cust001', 'sup001', 'User1'),
('trans002', 'bat002', 'Service', 'ss002', 'cust002', 'sup002', 'User2'),
('trans003', 'bat003', 'Return', 'ss003', 'cust003', 'sup003', 'User3'),
('trans004', 'bat004', 'Exchange', 'ss004', 'cust004', 'sup004', 'User4'),
('trans005', 'bat005', 'Inspection', 'ss005', 'cust005', 'sup005', 'User5');

-- Insert into service_history
INSERT INTO service_history (service_id, battery_id, service_station_id, service_description, created_by) VALUES
('servh001', 'bat001', 'ss001', 'Battery checkup', 'User1'),
('servh002', 'bat002', 'ss002', 'Battery maintenance', 'User2'),
('servh003', 'bat003', 'ss003', 'Battery replacement', 'User3'),
('servh004', 'bat004', 'ss004', 'Battery repair', 'User4'),
('servh005', 'bat005', 'ss005', 'Battery inspection', 'User5');

-- Insert into fault_log
INSERT INTO fault_log (fault_id, battery_id, fault_type, description, service_station_id, vehicle_id, created_by) VALUES
('fault001', 'bat001', 'Overheating', 'Battery overheating issue', 'ss001', 'veh001', 'User1'),
('fault002', 'bat002', 'Low Capacity', 'Battery low capacity issue', 'ss002', 'veh002', 'User2'),
('fault003', 'bat003', 'Leakage', 'Battery leakage issue', 'ss003', 'veh003', 'User3'),
('fault004', 'bat004', 'Voltage Drop', 'Battery voltage drop issue', 'ss004', 'veh004', 'User4'),
('fault005', 'bat005', 'Physical Damage', 'Battery physical damage issue', 'ss005', 'veh005', 'User5');

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
