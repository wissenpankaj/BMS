package com.wissen.bms.reportingAPI.controller;

import com.wissen.bms.reportingAPI.model.FaultLogModel;
import com.wissen.bms.reportingAPI.service.FaultLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FaultLogController.class)
public class FaultLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FaultLogService faultLogService;

    @Test
    public void testGetAllFaultLogs() throws Exception {
        // Mock data
        List<FaultLogModel> mockFaultLogs = Arrays.asList(
                createFaultLog(1, "Overload", 101, 1, "Battery overload detected"),
                createFaultLog(2, "Voltage Drop", 102, 2, "Voltage dropped below threshold")
        );

        when(faultLogService.getAllFaultLogs()).thenReturn(mockFaultLogs);

        mockMvc.perform(get("/api/faultlogs/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].faultid").value(1))
                .andExpect(jsonPath("$[0].faulttype").value("Overload"));
    }

    @Test
    public void testGetFaultLogByFaultId() throws Exception {
        // Mock data
        FaultLogModel mockFaultLog = createFaultLog(1, "Overload", 101, 1, "Battery overload detected");

        when(faultLogService.getFaultLogByFaultId(1)).thenReturn(mockFaultLog);

        mockMvc.perform(get("/api/faultlogs/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.faultid").value(1))
                .andExpect(jsonPath("$.faulttype").value("Overload"));
    }

    @Test
    public void testGetFaultLogByFaultId_NotFound() throws Exception {
        when(faultLogService.getFaultLogByFaultId(1)).thenReturn(null);

        mockMvc.perform(get("/api/faultlogs/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetFaultLogsByBatteryId() throws Exception {
        // Mock data
        List<FaultLogModel> mockFaultLogs = Arrays.asList(
                createFaultLog(1, "Overload", 101, 1, "Battery overload detected")
        );

        when(faultLogService.getFaultLogsByBatteryId(101)).thenReturn(mockFaultLogs);

        mockMvc.perform(get("/api/faultlogs/battery/101"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].batteryid").value(101));
    }

    @Test
    public void testGetFaultLogsByCreateDate() throws Exception {
        // Mock data
        List<FaultLogModel> mockFaultLogs = Arrays.asList(
                createFaultLog(1, "Overload", 101, 1, "Battery overload detected")
        );

        when(faultLogService.getFaultLogsByCreatedAt("2025-01-01")).thenReturn(mockFaultLogs);

        mockMvc.perform(get("/api/faultlogs/date").param("createDate", "2025-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].createdat").value("2025-01-01T00:00:00.000+00:00"));
    }

    @Test
    public void testGetFaultLogsByFaultType() throws Exception {
        // Mock data
        List<FaultLogModel> mockFaultLogs = Arrays.asList(
                createFaultLog(1, "Overload", 101, 1, "Battery overload detected")
        );

        when(faultLogService.getFaultLogsByFaultType("Overload")).thenReturn(mockFaultLogs);

        mockMvc.perform(get("/api/faultlogs/type").param("faultType", "Overload"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].faulttype").value("Overload"));
    }

    // Helper method to create FaultLogModel objects
    private FaultLogModel createFaultLog(Integer faultId, String faultType, Integer batteryId, Integer serviceStationId, String description) {
        FaultLogModel faultLog = new FaultLogModel();
        faultLog.setFaultid(faultId);
        faultLog.setCreatedat(Timestamp.from(Instant.parse("2025-01-01T00:00:00Z")));
        faultLog.setBatteryid(batteryId);
        faultLog.setServicestationid(serviceStationId);
        faultLog.setFaulttype(faultType);
        faultLog.setDescription(description);
        return faultLog;
    }
}
