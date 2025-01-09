#!/bin/bash

# Step 1: Run docker-compose to start the containers
echo "Starting containers with docker-compose..."
docker-compose up -d

# Step 2: Wait for InfluxDB to initialize
echo "Waiting for InfluxDB to initialize..."
# Sleep time to wait for InfluxDB to start (adjust if needed)
sleep 10

# Step 3: Run the init.sh script
echo "Running scripts.sh script..."
bash ./scripts.sh