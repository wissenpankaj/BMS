import json
import random
import time

def generate_random_telemetry():
    return {
        "voltage": round(random.uniform(380, 420), 2),
        "current": round(random.uniform(-100, 100), 2),
        "soc": round(random.uniform(20, 100), 2),
        "temperature": round(random.uniform(15, 60), 2),
        "internal_resistance": round(random.uniform(0.01, 0.5), 2),
        "cycle_count": random.randint(100, 1000),
        "energy_throughput": round(random.uniform(50, 500), 2),
        "charging_time": round(random.uniform(10, 300), 2)
    }

def main():
    while True:
        telemetry_data = generate_random_telemetry()
        with open("telemetry.json", "w") as file:
            json.dump(telemetry_data, file)
        print("Telemetry data written:", telemetry_data)
        time.sleep(1)  # Generate data every seconds
        
        
if __name__ == "__main__":
    main()
