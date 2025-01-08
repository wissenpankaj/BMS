import json
import os
import time
from datetime import datetime
import random
import paho.mqtt.client as mqtt


# MQTT Configuration
MQTT_BROKER = "localhost"
MQTT_PORT = 1883
MQTT_TOPIC = "ev/fleet/battery/telemetry"

# Thresholds for risk levels
RISK_THRESHOLDS = {
    "voltage": {"low": 390, "medium": 410, "high": 420},
    "current": {"low": -80, "medium": 80, "high": 100},
    "soc": {"low": 30, "medium": 80, "high": 100},
    "temperature": {"low": 25, "medium": 50, "high": 60},
    "internal_resistance": {"low": 0.1, "medium": 0.3, "high": 0.5},
    "cycle_count": {"low": 500, "medium": 800, "high": 1000},
    "energy_throughput": {"low": 200, "medium": 400, "high": 500},
    "charging_time": {"low": 100, "medium": 200, "high": 300},
}

# File containing the initialization date
INIT_DATE_FILE = "battery_init.json"

# Calculate overall risk level based on telemetry data
def calculate_risk_level(telemetry):
    overall_risk = "low"
    for param, value in telemetry.items():
        if param in RISK_THRESHOLDS:
            thresholds = RISK_THRESHOLDS[param]
            if value >= thresholds["high"]:
                return "high"
            elif value >= thresholds["medium"]:
                overall_risk = "medium"
    return overall_risk

# Fetch battery initialization date
def get_battery_init_date():
    try:
        with open(INIT_DATE_FILE, "r") as file:
            data = json.load(file)
            return datetime.strptime(data["init_date"], "%Y-%m-%d")
    except (FileNotFoundError, KeyError, ValueError):
        print(f"Initialization date file not found or invalid: {INIT_DATE_FILE}")
        return None

# Generate random telemetry data
def generate_telemetry():
    return {
        "voltage": round(random.uniform(380, 420), 2),
        "current": round(random.uniform(-100, 100), 2),
        "soc": round(random.uniform(20, 100), 2),
        "temperature": round(random.uniform(15, 60), 2),
        "internal_resistance": round(random.uniform(0.01, 0.5), 2),
        "cycle_count": random.randint(100, 1000),
        "energy_throughput": round(random.uniform(50, 500), 2),
        "charging_time": round(random.uniform(10, 300), 2),
    }

# Send data to the MQTT server
def send_to_server(client, data):
    payload = json.dumps(data)
    client.publish(MQTT_TOPIC, payload)
    print("Data sent to server:", payload)

# Initialize MQTT client
mqtt_client = mqtt.Client()
mqtt_client.connect(MQTT_BROKER, MQTT_PORT)

# Fetch the initialization date
battery_init_date = get_battery_init_date()
if not battery_init_date:
    print("Initialization date is missing. Exiting...")
    exit(1)

# Processing loop
send_interval = 10  # Periodic interval for low/medium risk data
last_sent_time = time.time()

while True:
    try:
        # Generate telemetry data
        telemetry = generate_telemetry()

        # Calculate risk level
        risk_level = calculate_risk_level(telemetry)
        telemetry["risk_level"] = risk_level

        # Add fault detection date
        fault_date = datetime.now()
        telemetry["fault_date"] = fault_date.strftime("%Y-%m-%d %H:%M:%S")

        # Add initialization date to the payload
        telemetry["init_date"] = battery_init_date.strftime("%Y-%m-%d")

        if risk_level == "high":
            send_to_server(mqtt_client, telemetry)
        elif time.time() - last_sent_time >= send_interval:
            send_to_server(mqtt_client, telemetry)
            last_sent_time = time.time()

    except Exception as e:
        print(f"Error: {e}")

    time.sleep(1)  # Short sleep to avoid excessive CPU usage
