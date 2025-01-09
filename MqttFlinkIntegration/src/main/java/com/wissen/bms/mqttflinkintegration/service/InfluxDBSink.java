package com.wissen.bms.mqttflinkintegration.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.wissen.bms.mqttflinkintegration.model.TelemetryData;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

@Slf4j
public class InfluxDBSink extends RichSinkFunction<TelemetryData> {

    private transient InfluxDBService influxDBService;

    private String influxDbUrl = "http://localhost:8086";

    private String influxDbToken="uSrgdObyNy5FyAzbD3e_7ZZo34VW1QpVS7FkAbFGOxlKVd_bnbmnT02_caqwHEPvhB3sZeoGcorNHUKuLg9KdQ==";

    private String influxDbOrg="05ba4da3f49978a1";

    private String influxDbBucket="Telemetry_Data";


    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        // Initialize InfluxDBClient
        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(influxDbUrl, influxDbToken.toCharArray(), influxDbOrg);

        // Instantiate the InfluxDBService with the required parameters
        influxDBService = new InfluxDBService(influxDBClient, influxDbOrg, influxDbBucket);
    }

    @Override
    public void invoke(TelemetryData value, Context context) {
        if (influxDBService != null) {
            influxDBService.writeData(value);
        } else {
            System.err.println("InfluxDBService is not initialized.");
        }
    }

    @Override
    public void close() throws Exception {
        super.close();
        // Clean up resources if necessary (close the InfluxDBClient)
        if (influxDBService != null) {
            influxDBService.getInfluxDBClient().close();
        }
    }
}
