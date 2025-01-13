
package com.wissen.bms.mqttflinkintegration.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.wissen.bms.common.model.BatteryFault;
import com.wissen.bms.common.model.TelemetryData;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

@Slf4j
public class InfluxDBBatteryFaulSink extends RichSinkFunction<BatteryFault> {

    private transient InfluxDBService influxDBService;

    private String influxDbUrl = "http://localhost:8086";

    private String influxDbToken="1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef";

    private String influxDbOrg="Wissen";

    private String influxDbBucket="EV";


    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        // Initialize InfluxDBClient
        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(influxDbUrl, influxDbToken.toCharArray(), influxDbOrg);

        // Instantiate the InfluxDBService with the required parameters
        influxDBService = new InfluxDBService(influxDBClient, influxDbOrg, influxDbBucket);
    }

    @Override
    public void invoke(BatteryFault value, Context context) {
        if (influxDBService != null) {
            influxDBService.writeFaultData(value);
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
