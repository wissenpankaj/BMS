#!/bin/bash

# Define InfluxDB parameters
INFLUX_URL="http://localhost:8086"
INFLUX_TOKEN="1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef"  # Replace with your InfluxDB token
ORG="Wissen"  # Replace with your InfluxDB organization
BUCKET="EV"   # Bucket name where the data is stored

# Line protocol with tags and fields (no actual values inserted)
# This line defines the measurement with tags and fields (no data inserted, just schema).
LINE_PROTOCOL="battery_metrics,gps=unknown,vehicle_id=unknown,battery_id=unknown,data_type=unknown voltage=0.0,current=0.0,soc=0.0,temperature=0.0,internal_resistance=0.0,cycle_count=0,energy_throughput=0.0,charging_time=0.0,soh=0.0"

# Output some info for debugging purposes
echo "Creating schema for battery_metrics measurement..."

# Create the schema by sending line protocol to InfluxDB
curl -XPOST "$INFLUX_URL/api/v2/write?org=$ORG&bucket=$BUCKET&precision=s" \
     -H "Authorization: Token $INFLUX_TOKEN" \
     -H "Content-Type: text/plain; charset=utf-8" \
     --data-binary "$LINE_PROTOCOL"

echo "Schema for 'battery_metrics' measurement created successfully!"

# Line protocol with tags and fields (no actual values inserted)
# This line defines the measurement with tags and fields (no data inserted, just schema).
LINE_PROTOCOL="battery_risks,vehicle_id=unknown,classification=unknown risk_score=0"

# Output some info for debugging purposes
echo "Creating schema for battery_risks measurement..."

# Create the schema by sending line protocol to InfluxDB
curl -XPOST "$INFLUX_URL/api/v2/write?org=$ORG&bucket=$BUCKET&precision=s" \
     -H "Authorization: Token $INFLUX_TOKEN" \
     -H "Content-Type: text/plain; charset=utf-8" \
     --data-binary "$LINE_PROTOCOL"

echo "Schema for 'battery_risks' measurement created successfully!"

# Line protocol with tags and fields (no actual values inserted)
# This line defines the measurement with tags and fields (no data inserted, just schema).
LINE_PROTOCOL="battery_faults,gps=unknown,vehicle_id=unknown,battery_id=unknown fault_reason=\"unknown\",recommendation=\"unknown\""

# Output some info for debugging purposes
echo "Creating schema for battery_faults measurement..."

# Create the schema by sending line protocol to InfluxDB
curl -XPOST "$INFLUX_URL/api/v2/write?org=$ORG&bucket=$BUCKET&precision=s" \
     -H "Authorization: Token $INFLUX_TOKEN" \
     -H "Content-Type: text/plain; charset=utf-8" \
     --data-binary "$LINE_PROTOCOL"

echo "Schema for 'battery_faults' measurement created successfully!"

