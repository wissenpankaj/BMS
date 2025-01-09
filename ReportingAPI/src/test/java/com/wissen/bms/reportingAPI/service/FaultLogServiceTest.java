package com.wissen.bms.reportingAPI.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.wissen.bms.reportingAPI.model.FaultLogModel;
import com.wissen.bms.reportingAPI.repo.FaultLogRepo;
import com.wissen.bms.reportingAPI.service.impl.FaultLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FaultLogServiceTest {

    @Mock
    private FaultLogRepo faultLogRepo;

    @InjectMocks
    private FaultLogServiceImpl faultLogService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllFaultLogs() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        FaultLogModel faultLog1 = new FaultLogModel();
        faultLog1.setFaultid(1);
        faultLog1.setCreatedat(timestamp);
        faultLog1.setBatteryid(123);
        faultLog1.setServicestationid(1);
        faultLog1.setFaulttype("Type1");

        FaultLogModel faultLog2 = new FaultLogModel();
        faultLog2.setFaultid(2);
        faultLog2.setCreatedat(timestamp);
        faultLog2.setBatteryid(124);
        faultLog2.setServicestationid(1);
        faultLog2.setFaulttype("Type2");

        when(faultLogRepo.findAll()).thenReturn(Arrays.asList(faultLog1, faultLog2));

        List<FaultLogModel> result = faultLogService.getAllFaultLogs();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(faultLogRepo, times(1)).findAll();
    }

    @Test
    public void testGetFaultLogByFaultId() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        FaultLogModel faultLog = new FaultLogModel();
        faultLog.setFaultid(1);
        faultLog.setCreatedat(timestamp);
        faultLog.setBatteryid(123);
        faultLog.setServicestationid(1);
        faultLog.setFaulttype("Type1");

        when(faultLogRepo.findById(1)).thenReturn(Optional.of(faultLog));

        FaultLogModel result = faultLogService.getFaultLogByFaultId(1);

        assertNotNull(result);
        assertEquals(1, result.getFaultid());
        verify(faultLogRepo, times(1)).findById(1);
    }

    @Test
    public void testGetFaultLogByFaultId_NotFound() {
        when(faultLogRepo.findById(1)).thenReturn(Optional.empty());

        FaultLogModel result = faultLogService.getFaultLogByFaultId(1);

        assertNull(result);
        verify(faultLogRepo, times(1)).findById(1);
    }
    @Test
    public void testGetFaultLogsByBatteryId() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        FaultLogModel faultLog = new FaultLogModel();
        faultLog.setFaultid(1);
        faultLog.setCreatedat(timestamp);
        faultLog.setBatteryid(123);
        faultLog.setServicestationid(1);
        faultLog.setFaulttype("Type1");

        when(faultLogRepo.findByBatteryid(123)).thenReturn(List.of(faultLog));

        List<FaultLogModel> result = faultLogService.getFaultLogsByBatteryId(123);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(faultLogRepo, times(1)).findByBatteryid(123);
    }
    @Test
    public void testGetFaultLogsByCreatedAt() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        FaultLogModel faultLog = new FaultLogModel();
        faultLog.setFaultid(1);
        faultLog.setCreatedat(timestamp);
        faultLog.setBatteryid(123);
        faultLog.setServicestationid(1);
        faultLog.setFaulttype("Type1");

        String date = "2025-01-09";
        LocalDateTime startOfDay = LocalDate.parse(date).atStartOfDay();
        LocalDateTime endOfDay = LocalDate.parse(date).atTime(23, 59, 59);

        when(faultLogRepo.findByCreatedatBetween(startOfDay, endOfDay)).thenReturn(List.of(faultLog));

        List<FaultLogModel> result = faultLogService.getFaultLogsByCreatedAt(date);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(faultLogRepo, times(1)).findByCreatedatBetween(startOfDay, endOfDay);
    }
    @Test
    public void testGetFaultLogsByFaultType() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        FaultLogModel faultLog = new FaultLogModel();
        faultLog.setFaultid(1);
        faultLog.setCreatedat(timestamp);
        faultLog.setBatteryid(123);
        faultLog.setServicestationid(1);
        faultLog.setFaulttype("Type1");

        when(faultLogRepo.findByFaulttype("Type1")).thenReturn(List.of(faultLog));

        List<FaultLogModel> result = faultLogService.getFaultLogsByFaultType("Type1");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(faultLogRepo, times(1)).findByFaulttype("Type1");
    }

}
