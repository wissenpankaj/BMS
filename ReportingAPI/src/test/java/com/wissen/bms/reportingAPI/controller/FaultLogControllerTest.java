package com.wissen.bms.reportingAPI.controller;

import com.wissen.bms.reportingAPI.model.FaultLogModel;
import com.wissen.bms.reportingAPI.service.FaultLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
public class FaultLogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FaultLogService faultLogService;

    @InjectMocks
    private FaultLogController faultLogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(faultLogController).build();
    }

    @Test
    void testGetAllFaultLogs() throws Exception {
        // Prepare mock data
        FaultLogModel faultLog1 = new FaultLogModel();
        faultLog1.setFaultId("1");
        faultLog1.setBatteryId("battery1");
        faultLog1.setFaultType("type1");
        faultLog1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        faultLog1.setDescription("description1");

        FaultLogModel faultLog2 = new FaultLogModel();
        faultLog2.setFaultId("2");
        faultLog2.setBatteryId("battery2");
        faultLog2.setFaultType("type2");
        faultLog2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        faultLog2.setDescription("description2");

        List<FaultLogModel> faultLogs = Arrays.asList(faultLog1, faultLog2);

        when(faultLogService.getAllFaultLogs()).thenReturn(faultLogs);

        mockMvc.perform(get("/api/faultlogs/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].faultId").value("1"))
                .andExpect(jsonPath("$[1].faultId").value("2"));

        verify(faultLogService, times(1)).getAllFaultLogs();
    }

    @Test
    void testGetFaultLogByFaultId() throws Exception {
        // Prepare mock data
        FaultLogModel faultLog = new FaultLogModel();
        faultLog.setFaultId("1");
        faultLog.setBatteryId("battery1");
        faultLog.setFaultType("type1");
        faultLog.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        faultLog.setDescription("description1");

        when(faultLogService.getFaultLogByFaultId("1")).thenReturn(faultLog);

        mockMvc.perform(get("/api/faultlogs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.faultId").value("1"))
                .andExpect(jsonPath("$.batteryId").value("battery1"));

        verify(faultLogService, times(1)).getFaultLogByFaultId("1");
    }

    @Test
    void testGetFaultLogByFaultIdNotFound() throws Exception {
        when(faultLogService.getFaultLogByFaultId("non-existent-id")).thenReturn(null);

        mockMvc.perform(get("/api/faultlogs/non-existent-id"))
                .andExpect(status().isNotFound());

        verify(faultLogService, times(1)).getFaultLogByFaultId("non-existent-id");
    }

    @Test
    void testGetFaultLogsByBatteryId() throws Exception {
        // Prepare mock data
        FaultLogModel faultLog1 = new FaultLogModel();
        faultLog1.setFaultId("1");
        faultLog1.setBatteryId("battery1");
        faultLog1.setFaultType("type1");
        faultLog1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        faultLog1.setDescription("description1");

        FaultLogModel faultLog2 = new FaultLogModel();
        faultLog2.setFaultId("2");
        faultLog2.setBatteryId("battery1");
        faultLog2.setFaultType("type2");
        faultLog2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        faultLog2.setDescription("description2");

        List<FaultLogModel> faultLogs = Arrays.asList(faultLog1, faultLog2);

        when(faultLogService.getFaultLogsByBatteryId("battery1")).thenReturn(faultLogs);

        mockMvc.perform(get("/api/faultlogs/battery/battery1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].faultId").value("1"))
                .andExpect(jsonPath("$[1].faultId").value("2"));

        verify(faultLogService, times(1)).getFaultLogsByBatteryId("battery1");
    }

    @Test
    void testGetFaultLogsByCreateDate() throws Exception {
        // Prepare mock data
        FaultLogModel faultLog1 = new FaultLogModel();
        faultLog1.setFaultId("1");
        faultLog1.setBatteryId("battery1");
        faultLog1.setFaultType("type1");
        faultLog1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        faultLog1.setDescription("description1");

        List<FaultLogModel> faultLogs = List.of(faultLog1);

        when(faultLogService.getFaultLogsByCreatedAt("2025-01-10")).thenReturn(faultLogs);

        mockMvc.perform(get("/api/faultlogs/date")
                        .param("createDate", "2025-01-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].faultId").value("1"));

        verify(faultLogService, times(1)).getFaultLogsByCreatedAt("2025-01-10");
    }

    @Test
    void testGetFaultLogsByFaultType() throws Exception {
        // Prepare mock data
        FaultLogModel faultLog1 = new FaultLogModel();
        faultLog1.setFaultId("1");
        faultLog1.setBatteryId("battery1");
        faultLog1.setFaultType("type1");
        faultLog1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        faultLog1.setDescription("description1");

        List<FaultLogModel> faultLogs = List.of(faultLog1);

        when(faultLogService.getFaultLogsByFaultType("type1")).thenReturn(faultLogs);

        mockMvc.perform(get("/api/faultlogs/type")
                        .param("faultType", "type1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].faultId").value("1"));

        verify(faultLogService, times(1)).getFaultLogsByFaultType("type1");
    }
}
