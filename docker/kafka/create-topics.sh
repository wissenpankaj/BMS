#!/bin/bash

# Wait for the Kafka broker to start
sleep 30

# Create topics
kafka-topics --create --bootstrap-server kafka-broker-1:9092 --replication-factor 1 --partitions 1 --topic telemetry
kafka-topics --create --bootstrap-server kafka-broker-1:9092 --replication-factor 1 --partitions 1 --topic faultalert
kafka-topics --create --bootstrap-server kafka-broker-1:9092 --replication-factor 1 --partitions 1 --topic nearstation
