#!/bin/bash

# Define InfluxDB parameters
INFLUX_URL="http://localhost:8086"
INFLUX_TOKEN="1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef"  # Replace with your InfluxDB token
ORG="Wissen"  # Replace with your InfluxDB organization
BUCKET="EV"   # Bucket name where the data is stored

# Updating the 'battery_metrics' measurement
echo "Updating schema for 'battery_metrics' measurement..."
LINE_PROTOCOL="battery_metrics,vehicle_id=unknown,battery_id=unknown longitude=0.0,latitude=0.0,voltage=0.0,current=0.0,soc=0.0,temperature=0.0,internal_resistance=0.0,cycle_count=0,energy_throughput=0.0,charging_time=0.0,soh=0.0"
curl -XPOST "$INFLUX_URL/api/v2/write?org=$ORG&bucket=$BUCKET&precision=s" \
     -H "Authorization: Token $INFLUX_TOKEN" \
     -H "Content-Type: text/plain; charset=utf-8" \
     --data-binary "$LINE_PROTOCOL"
echo "Schema for 'battery_metrics' measurement updated successfully!"