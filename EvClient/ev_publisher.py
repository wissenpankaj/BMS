import json
import time
import random
from datetime import datetime
import threading
import ssl
from awscrt import mqtt, http
from awsiot import mqtt_connection_builder

import time
import json

# Continuously monitors telemetry data using generate_telemetry_data().
# Filters critical data using is_critical() and sends it immediately via MQTT.
# Sends non-critical data periodically to reduce load.

# # MQTT Configuration
MQTT_BROKER="a16y0qpd4ehc89-ats.iot.us-west-1.amazonaws.com"
MQTT_PORT = 8883
# MQTT_BROKER = "localhost"
# MQTT_PORT = 1883
BATTERY_DATA_TOPIC = "ev/battery/telemetry"


# Paths to certificates and keys
# CA_PATH = ".\AmazonRootCA1.pem"
CA_PATH = ""
CERT_PATH = ".\\ev.cert.pem"
KEY_PATH = ".\\ev.private.key"
CLIENT_ID="ev"

received_count = 0
received_all_event = threading.Event()

# Callback when connection is accidentally lost.
def on_connection_interrupted(connection, error, **kwargs):
    print("Connection interrupted. error: {}".format(error))


# Callback when an interrupted connection is re-established.
def on_connection_resumed(connection, return_code, session_present, **kwargs):
    print("Connection resumed. return_code: {} session_present: {}".format(return_code, session_present))

    if return_code == mqtt.ConnectReturnCode.ACCEPTED and not session_present:
        print("Session did not persist. Resubscribing to existing topics...")
        resubscribe_future, _ = connection.resubscribe_existing_topics()

        # Cannot synchronously wait for resubscribe result because we're on the connection's event-loop thread,
        # evaluate result with a callback instead.
        resubscribe_future.add_done_callback(on_resubscribe_complete)


def on_resubscribe_complete(resubscribe_future):
    resubscribe_results = resubscribe_future.result()
    print("Resubscribe results: {}".format(resubscribe_results))

    for topic, qos in resubscribe_results['topics']:
        if qos is None:
            sys.exit("Server rejected resubscribe to topic: {}".format(topic))


# Callback when the subscribed topic receives a message
def on_message_received(topic, payload, dup, qos, retain, **kwargs):
    print("Received message from topic '{}': {}".format(topic, payload))
    global received_count
    received_count += 1
    if received_count == 10:
        received_all_event.set()

# Callback when the connection successfully connects
def on_connection_success(connection, callback_data):
    assert isinstance(callback_data, mqtt.OnConnectionSuccessData)
    print("Connection Successful with return code: {} session present: {}".format(callback_data.return_code, callback_data.session_present))

# Battery Data Generation - Simulating EV battery data
def generate_battery_data(vehicle_id, battery_type):
    
    # Randomly generating battery data based on the battery type
    battery_soc = random.uniform(10, 100)  # State of Charge (SOC) in percentage
    battery_health = random.uniform(40, 100)  # Battery health in percentage
    discharging_rate = random.uniform(0, 10)  # Discharging rate in amperes
    temperature = random.uniform(-10, 60)  # Temperature in Celsius
    voltage = random.uniform(2.5, 4.5)  # Voltage in volts
    current = random.uniform(0, 15)  # Current in amperes
    gps_location = f"Lat: {random.uniform(-90, 90)}, Lon: {random.uniform(-180, 180)}"  # Random GPS coordinates
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")  # Current timestamp
    replacement_center = "Default Center"
    
    
    # Return as a dictionary
    return {
        "vehicle_id": 1,
        "battery_id":1,
        "battery_type": battery_type,
        "timestamp": timestamp,
        "battery_soc": battery_soc,
        "battery_health": battery_health,
        "discharging_rate": discharging_rate,
        "temperature": temperature,
        "voltage": voltage,
        "current": current,
        "gps_location": gps_location,
       
    }

# Check if data is critical based on some thresholds (simplified for EV side)
def is_data_critical(battery_data):
    # For simplicity, use hardcoded thresholds for this example
    critical =  None
    if battery_data["battery_soc"] < 20 or battery_data["battery_health"] < 50:
        critical = "battery_soc"
    if battery_data["discharging_rate"] > 5 or battery_data["temperature"] > 50:
        critical = "temperature"
    if battery_data["voltage"] < 3.0 or battery_data["voltage"] > 4.2:
        critical = "voltage"
    return critical

# Send data to MQTT Broker
def send_battery_data(client,vehicle_id, battery_type, data):
   # client = mqtt.Client()
    # client.connect(MQTT_BROKER, MQTT_PORT)
    message_json = json.dumps(data)
    # Check if data is critical
    critical = is_data_critical(data)
    if critical != None:
        print(f"Critical data for {battery_type} battery, sending immediately...")
        data["alert_level"]="high"
        data["alert_type"] =critical
        client.publish(topic=BATTERY_DATA_TOPIC,
                payload=json.dumps(data),
                qos=mqtt.QoS.AT_LEAST_ONCE)
        # client.publish(BATTERY_DATA_TOPIC, json.dumps(data))
    else:
        print(f"Non-critical data for {battery_type} battery, sending periodically...")
        client.publish(topic=BATTERY_DATA_TOPIC,
                payload=message_json,
                qos=mqtt.QoS.AT_LEAST_ONCE)

    # client.disconnect()

# Main EV Data Simulation
def ev_simulation(client,vehicle_id, battery_type):
    c=0
    while c<5:
        c+=1
        # Generate battery data
        data = generate_battery_data(vehicle_id, battery_type)

        # Send data based on criticality
        send_battery_data(client,vehicle_id, battery_type, data)

        # Wait for 5 seconds if non-critical data, send every second if critical
        if is_data_critical(data):
            time.sleep(1)  # Send immediately, so send every second
        else:
            time.sleep(5)  # Send periodically (every 5 seconds)

def connect_mqtt() :
    proxy_options = None
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_connection_builder.mtls_from_path(
        endpoint=MQTT_BROKER,
        port=MQTT_PORT,
        cert_filepath=CERT_PATH,
        pri_key_filepath=KEY_PATH,
        ca_filepath=CA_PATH,
        on_connection_interrupted=on_connection_interrupted,
        on_connection_resumed=on_connection_resumed,
        client_id='ev',
        clean_session=False,
        keep_alive_secs=30,
        http_proxy_options=proxy_options,
        on_connection_success=on_connection_success,
        )
    
# Configure TLS/SSL
    
    return client


def subscribe(client):
    def on_message_received(topic, payload, dup, qos, retain, **kwargs):
        print("Received message from topic '{}': {}".format(topic, payload))
        global received_count
        received_count += 1
        if received_count ==10:
            received_all_event.set()
    def on_message(client, userdata, msg):
        print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")
    client.subscribe(BATTERY_DATA_TOPIC,qos=mqtt.QoS.AT_LEAST_ONCE,callback=on_message_received)
    # client.on_message = on_message




def publish(client,topic=BATTERY_DATA_TOPIC):
    msg_count = 1
    while True:
        time.sleep(1)
        msg = f"messages: {msg_count}"
        result = client.publish(topic, msg)
        # result: [0, 1]
        status = result[0]
        if status == 0:
            print(f"Send `{msg}` to topic `{topic}`")
        else:
            print(f"Failed to send message to topic {topic}")
        msg_count += 1
        if msg_count > 5:
            break
        

def run():
    client = connect_mqtt()
    client.connect()
    # client.loop_start()
    subscribe(client)
    vehicle_id = "EV12345"  # Example vehicle ID
    battery_type = "Type-A"  # Example battery type, could be Type-A, Type-B, etc.
    # publish(client)
    ev_simulation(client,vehicle_id,battery_type)
    client.disconnect()
    #client.loop_forever()

if __name__ == '__main__':
    run()