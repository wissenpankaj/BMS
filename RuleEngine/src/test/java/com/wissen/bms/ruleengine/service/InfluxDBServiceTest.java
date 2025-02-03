package com.wissen.bms.ruleengine.service;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.wissen.bms.common.model.TelemetryData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class InfluxDBServiceTest {
    @InjectMocks
    InfluxDBService influxDBService;
    @Mock
    InfluxDBClient influxDBClient;

    @Test
    void writeDataTest() {
        WriteApiBlocking writeApi = Mockito.mock(WriteApiBlocking.class);
        Mockito.when(influxDBClient.getWriteApiBlocking()).thenReturn(writeApi);
        Mockito.doNothing().when(writeApi).writePoint(any(), any(), any());
       Assertions.assertDoesNotThrow(()-> influxDBService.writeData(getTelemetryDataTestObj()));
    }

    @Test
    void writeDataNullTelemetryDataObjTest() {
        influxDBService.writeData(null);
    }

    @Test
    void writeDataForExceptionCatchTest() {
        TelemetryData telemetryDataTestObj = getTelemetryDataTestObj();
        telemetryDataTestObj.setBatteryId(null);
        Assertions.assertDoesNotThrow(() -> influxDBService.writeData(telemetryDataTestObj));
    }

    private TelemetryData getTelemetryDataTestObj() {
        TelemetryData telemetry1 = new TelemetryData();
        telemetry1.setBatteryId("1");
        telemetry1.setVehicleId("v1");
        telemetry1.setVoltage(35.6);
        telemetry1.setCurrent(12.2);
        telemetry1.setTemperature(28);
        telemetry1.setInternalResistance(2);
        telemetry1.setSoc(25.0);
        telemetry1.setSoh(11.2);
        telemetry1.setCycleCount(2);
        telemetry1.setEnergyThroughput(21.3);
        telemetry1.setChargingTime(12.0);
        telemetry1.setGps("gps");
        telemetry1.setTime("2");
        telemetry1.setRiskLevel("12");
        return telemetry1;
    }
}