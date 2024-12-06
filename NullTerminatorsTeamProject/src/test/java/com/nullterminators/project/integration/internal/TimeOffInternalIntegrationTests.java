package com.nullterminators.project.integration.internal;

import com.nullterminators.project.controller.TimeOffController;
import com.nullterminators.project.enums.LeaveStatus;
import com.nullterminators.project.model.TimeOff;
import com.nullterminators.project.service.EmployeeProfileService;
import com.nullterminators.project.service.TimeOffService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * Internal integration tests for TimeOff endpoints.
 */
@SpringBootTest
public class TimeOffInternalIntegrationTests {

    private final Integer employeeId = 1;
    private final Integer timeOffId = 101;
    @Autowired
    private TimeOffController timeOffController;
    @MockBean
    private TimeOffService timeOffService;
    @MockBean
    private EmployeeProfileService employeeProfileService;
    private TimeOff mockTimeOff;

    @BeforeEach
    public void setUp() {
        mockTimeOff = new TimeOff();
        mockTimeOff.setId(timeOffId);
        mockTimeOff.setEmployeeId(employeeId);
        mockTimeOff.setStartDate(LocalDate.of(2024, 1, 1));
        mockTimeOff.setEndDate(LocalDate.of(2024, 1, 5));
        mockTimeOff.setReason("Vacation");
        mockTimeOff.setStatus(LeaveStatus.PENDING);

        when(employeeProfileService.doesEmployeeExist(employeeId)).thenReturn(true);
    }

    @Test
    public void getTimeOffByEmployeeIdSuccessTest() {
        when(timeOffService.getTimeOffByEmployeeId(employeeId)).thenReturn(List.of(mockTimeOff));

        ResponseEntity<?> response = timeOffController.getTimeOffByEmployeeId(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(mockTimeOff), response.getBody());
    }

    @Test
    public void getTimeOffByEmployeeIdNotFoundTest() {
        when(timeOffService.getTimeOffByEmployeeId(employeeId)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = timeOffController.getTimeOffByEmployeeId(employeeId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No time-off records found for the employee", response.getBody());
    }

    @Test
    public void createTimeOffRequestSuccessTest() {
        when(timeOffService.createTimeOffRequest(any(TimeOff.class))).thenReturn(mockTimeOff);

        ResponseEntity<?> response = timeOffController.createTimeOffRequest(mockTimeOff);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockTimeOff, response.getBody());
    }

    @Test
    public void createTimeOffRequestEmployeeNotFoundTest() {
        when(employeeProfileService.doesEmployeeExist(employeeId)).thenReturn(false);

        ResponseEntity<?> response = timeOffController.createTimeOffRequest(mockTimeOff);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee does not exist", response.getBody());
    }

    @Test
    public void updateTimeOffStatusSuccessTest() {
        when(timeOffService.updateTimeOffStatus(employeeId, timeOffId, "approve")).thenReturn(true);

        ResponseEntity<?> response =
                timeOffController.updateTimeOffStatus(employeeId, timeOffId, "approve");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Time-off request status updated successfully", response.getBody());
    }

    @Test
    public void updateTimeOffStatusNotFoundTest() {
        when(timeOffService.updateTimeOffStatus(employeeId, timeOffId, "approve")).thenReturn(false);

        ResponseEntity<?> response =
                timeOffController.updateTimeOffStatus(employeeId, timeOffId, "approve");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Time-off request not found or action not applicable", response.getBody());
    }

    @Test
    public void deleteTimeOffRequestSuccessTest() {
        when(timeOffService.deleteTimeOffRequest(employeeId, timeOffId)).thenReturn(true);

        ResponseEntity<?> response = timeOffController.deleteTimeOffRequest(employeeId, timeOffId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Time-off request deleted successfully", response.getBody());
    }

    @Test
    public void deleteTimeOffRequestNotFoundTest() {
        when(timeOffService.deleteTimeOffRequest(employeeId, timeOffId)).thenReturn(false);

        ResponseEntity<?> response = timeOffController.deleteTimeOffRequest(employeeId, timeOffId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Time-off request not found or already deleted", response.getBody());
    }

    @Test
    public void getTimeOffInRangeSuccessTest() {
        when(timeOffService.getTimeOffByEmployeeIdWithDateRange(
                employeeId, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)))
                .thenReturn(List.of(mockTimeOff));

        ResponseEntity<?> response =
                timeOffController.getTimeOffInRange(
                        employeeId, "2024-01-01", "2024-01-05");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(mockTimeOff), response.getBody());
    }

    @Test
    public void getTimeOffInRangeBadRequestTest() {
        ResponseEntity<?> response =
                timeOffController.getTimeOffInRange(employeeId, "2024-01-05", "2024-01-01");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Start date must be before end date", response.getBody());
    }
}
