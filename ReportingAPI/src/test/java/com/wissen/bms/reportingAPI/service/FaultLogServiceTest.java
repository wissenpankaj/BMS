package com.wissen.bms.reportingAPI.service;

import com.wissen.bms.reportingAPI.model.FaultLogModel;
import com.wissen.bms.reportingAPI.repo.FaultLogRepo;
import com.wissen.bms.reportingAPI.service.impl.FaultLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class FaultLogServiceTest {

    @Mock
    private FaultLogRepo faultLogRepo;  // Mock the FaultLogRepo

    @InjectMocks
    private FaultLogServiceImpl faultLogServiceImpl;  // Service being tested

    private FaultLogModel faultLog1;
    private FaultLogModel faultLog2;

    @BeforeEach
    void setUp() {
        // Initialize mocks and prepare the objects for the tests
        MockitoAnnotations.openMocks(this);

        faultLog1 = new FaultLogModel();
        faultLog1.setFaultId("1");
        faultLog1.setBatteryId("battery1");
        faultLog1.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        faultLog1.setFaultType("BatteryFault");
        faultLog1.setDescription("Fault in battery");

        faultLog2 = new FaultLogModel();
        faultLog2.setFaultId("2");
        faultLog2.setBatteryId("battery2");
        faultLog2.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        faultLog2.setFaultType("ChargingFault");
        faultLog2.setDescription("Charging issue");
    }

    @Test
    void testGetAllFaultLogs() {
        // Arrange: mock the repo to return a list of fault logs
        when(faultLogRepo.findAll()).thenReturn(Arrays.asList(faultLog1, faultLog2));

        // Act: call the service method
        List<FaultLogModel> faultLogs = faultLogServiceImpl.getAllFaultLogs();

        // Assert: verify the result
        assertNotNull(faultLogs);
        assertEquals(2, faultLogs.size());
        verify(faultLogRepo, times(1)).findAll();
    }

    @Test
    void testGetFaultLogByFaultId() {
        // Arrange: mock the repo to return a specific fault log
        when(faultLogRepo.findById("1")).thenReturn(Optional.of(faultLog1));

        // Act: call the service method
        FaultLogModel faultLog = faultLogServiceImpl.getFaultLogByFaultId("1");

        // Assert: verify the result
        assertNotNull(faultLog);
        assertEquals("1", faultLog.getFaultId());
        verify(faultLogRepo, times(1)).findById("1");
    }

    @Test
    void testGetFaultLogByFaultId_NotFound() {
        // Arrange: mock the repo to return empty
        when(faultLogRepo.findById("999")).thenReturn(Optional.empty());

        // Act: call the service method
        FaultLogModel faultLog = faultLogServiceImpl.getFaultLogByFaultId("999");

        // Assert: verify the result
        assertNull(faultLog);
        verify(faultLogRepo, times(1)).findById("999");
    }

    @Test
    void testGetFaultLogsByBatteryId() {
        // Arrange: mock the repo to return fault logs for a specific battery
        when(faultLogRepo.findByBatteryId("battery1")).thenReturn(Collections.singletonList(faultLog1));

        // Act: call the service method
        List<FaultLogModel> faultLogs = faultLogServiceImpl.getFaultLogsByBatteryId("battery1");

        // Assert: verify the result
        assertNotNull(faultLogs);
        assertEquals(1, faultLogs.size());
        assertEquals("battery1", faultLogs.get(0).getBatteryId());
        verify(faultLogRepo, times(1)).findByBatteryId("battery1");
    }

    /*@Test
    void testGetFaultLogsByCreatedAt() {
        // Arrange: Set the expected date range and create a fault log with createdAt matching the range.
        LocalDateTime date = LocalDateTime.of(2025, 1, 10, 10, 0, 0, 0);  // Set a date within the rang

        // Create the start and end of the day for January 10th, 2025
        LocalDateTime startOfDay = LocalDateTime.of(2025, 1, 10, 0, 0, 0, 0);
        LocalDateTime endOfDay = LocalDateTime.of(2025, 1, 10, 23, 59, 59, 999000000);

        System.out.println("startOfDay: " + startOfDay);
        System.out.println("endOfDay: " + endOfDay);

        faultLog1.setCreatedAt(Timestamp.valueOf(date));  // Set the mock faultLog1's createdAt

        // Mock the repo to return faultLog1 when findByCreatedAtBetween is called
        when(faultLogRepo.findByCreatedAtBetween(startOfDay, endOfDay)).thenReturn(Arrays.asList(faultLog1));

        faultLog1.setCreatedAt(Timestamp.valueOf(date));  // Confirm this date is being set properly

        // Act: Call the service method with the test date
        List<FaultLogModel> faultLogs = faultLogServiceImpl.getFaultLogsByCreatedAt("2025-01-10");

        // Print the result to check what was returned
        System.out.println("Returned Fault Logs: " + faultLogs.size());
        faultLogs.forEach(f -> System.out.println("Fault Log createdAt: " + f.getCreatedAt()));

        // Assert: Verify that the returned list contains the expected fault log
        assertNotNull(faultLogs);
        assertEquals(1, faultLogs.size());  // This should pass if everything is correct
        assertEquals("2025-01-10", faultLogs.get(0).getCreatedAt().toLocalDateTime().toString());  // Verify the date

        // Verify the repository call
        verify(faultLogRepo, times(1)).findByCreatedAtBetween(startOfDay, endOfDay);
    }
*/
    @Test
    public void testGetFaultLogsByCreatedAt() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        FaultLogModel faultLog = new FaultLogModel();
        faultLog.setFaultId("1");
        faultLog.setCreatedAt(timestamp);
        faultLog.setBatteryId("123");
        faultLog.setServiceStationId("1");
        faultLog.setFaultType("Type1");
        String date = "2025-01-09";
        LocalDateTime startOfDay = LocalDate.parse(date).atStartOfDay();
        LocalDateTime endOfDay = LocalDate.parse(date).atTime(23, 59, 59);
        when(faultLogRepo.findByCreatedAtBetween(startOfDay, endOfDay)).thenReturn(List.of(faultLog));
        List<FaultLogModel> result = faultLogServiceImpl.getFaultLogsByCreatedAt(date);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(faultLogRepo, times(1)).findByCreatedAtBetween(startOfDay, endOfDay);
    }

    @Test
    void testGetFaultLogsByFaultType() {
        // Arrange: mock the repo to return fault logs for a specific fault type
        when(faultLogRepo.findByFaultType("BatteryFault")).thenReturn(Collections.singletonList(faultLog1));

        // Act: call the service method
        List<FaultLogModel> faultLogs = faultLogServiceImpl.getFaultLogsByFaultType("BatteryFault");

        // Assert: verify the result
        assertNotNull(faultLogs);
        assertEquals(1, faultLogs.size());
        assertEquals("BatteryFault", faultLogs.get(0).getFaultType());
        verify(faultLogRepo, times(1)).findByFaultType("BatteryFault");
    }
}
