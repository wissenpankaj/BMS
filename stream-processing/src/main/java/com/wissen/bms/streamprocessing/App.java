package com.wissen.bms.streamprocessing;

import com.wissen.bms.streamprocessing.service.FlinkMqttIntegrationService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        FlinkMqttIntegrationService flinkMqttIntegrationService = new FlinkMqttIntegrationService();
        flinkMqttIntegrationService.process();
    }
}
