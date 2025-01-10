-- Table for Station
CREATE TABLE Station (
    stationId INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);


-- Table for Battery
CREATE TABLE Battery (
    batteryId INT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    brand VARCHAR(50),
    capacity INT, -- in watt-hours or any other unit
    locationId INT,
    expiryDate DATE,
    price INT,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (locationId) REFERENCES Station(stationId)
);

-- Table for Location
CREATE TABLE Location (
    locationId INT PRIMARY KEY,
    locationName VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);

-- Table for StockManagement
CREATE TABLE StockManagement (
    stockTransactionId INT PRIMARY KEY,
    batteryId INT,
    transactionDate DATE NOT NULL,
    quantity INT NOT NULL,
    transactionType VARCHAR(50), -- e.g., "IN", "OUT"
    supplierId INT,
    FOREIGN KEY (batteryId) REFERENCES Battery(batteryId),
    FOREIGN KEY (supplierId) REFERENCES Supplier(supplierId)
);

-- Table for Supplier
CREATE TABLE Supplier (
    supplierId INT PRIMARY KEY,
    supplierName VARCHAR(255) NOT NULL,
    contactDetails VARCHAR(255),
    address VARCHAR(255)
);

-- Table for ServiceLog
CREATE TABLE ServiceLog (
    serviceLogId INT PRIMARY KEY,
    batteryId INT,
    serviceType VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL, -- Use TIMESTAMP instead of DATETIME
    details VARCHAR(255),
    FOREIGN KEY (batteryId) REFERENCES Battery(batteryId)
);


-- Table for SalesOrder
CREATE TABLE SalesOrder (
    salesOrderId INT PRIMARY KEY,
    customerId INT,
    orderDate DATE NOT NULL,
    totalAmount DECIMAL(10, 2),
    FOREIGN KEY (customerId) REFERENCES Customer(customerId)
);

-- Table for SalesOrderDetails
CREATE TABLE SalesOrderDetails (
    salesOrderDetailId INT PRIMARY KEY,
    salesOrderId INT,
    batteryId INT,
    quantity INT NOT NULL,
    unitPrice DECIMAL(10, 2),
    FOREIGN KEY (salesOrderId) REFERENCES SalesOrder(salesOrderId),
    FOREIGN KEY (batteryId) REFERENCES Battery(batteryId)
);

-- Table for Customer
CREATE TABLE Customer (
    customerId INT PRIMARY KEY,
    customerName VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    address VARCHAR(255)
);

-- Table for Transaction
CREATE TABLE Transaction (
    transactionId INT PRIMARY KEY,
    batteryId INT,
    transactionType VARCHAR(50),
    serviceStationId INT,
    customerId INT,
    supplierId INT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    createdBy INT,
    FOREIGN KEY (batteryId) REFERENCES Battery(batteryId),
    FOREIGN KEY (serviceStationId) REFERENCES ServiceStation(serviceStationId),
    FOREIGN KEY (customerId) REFERENCES Customer(customerId),
    FOREIGN KEY (supplierId) REFERENCES Supplier(supplierId)
);

-- Table for ServiceHistory
CREATE TABLE ServiceHistory (
    serviceId INT PRIMARY KEY,
    batteryId INT,
    serviceStationId INT,
    serviceDescription TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    createdBy INT,
    FOREIGN KEY (batteryId) REFERENCES Battery(batteryId),
    FOREIGN KEY (serviceStationId) REFERENCES ServiceStation(serviceStationId)
);

-- Table for FaultLog
CREATE TABLE FaultLog (
    faultId INT PRIMARY KEY,
    batteryId INT,
    faultType VARCHAR(50),
    description TEXT,
    serviceStationId INT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    createdBy INT,
    FOREIGN KEY (batteryId) REFERENCES Battery(batteryId),
    FOREIGN KEY (serviceStationId) REFERENCES ServiceStation(serviceStationId)
);

-- Table for ServiceStation
CREATE TABLE ServiceStation (
    serviceStationId INT PRIMARY KEY,
    serviceStationName VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    contactDetails VARCHAR(255)
);

-- Table for User
CREATE TABLE Users (
    userId INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) -- e.g., Admin, Customer
);n

-- Table for UserToken
CREATE TABLE UserToken (
    tokenId INT PRIMARY KEY,
    userId INT,
    token VARCHAR(255),
    expiry TIMESTAMP NOT NULL,
    FOREIGN KEY (userId) REFERENCES Users(userId)
);







