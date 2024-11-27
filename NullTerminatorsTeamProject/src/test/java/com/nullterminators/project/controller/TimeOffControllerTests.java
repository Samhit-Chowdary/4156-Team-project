package com.nullterminators.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nullterminators.project.enums.LeaveStatus;
import com.nullterminators.project.model.TimeOff;
import com.nullterminators.project.service.EmployeeProfileService;
import com.nullterminators.project.service.TimeOffService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Tests for timeoff controller methods. */
class TimeOffControllerTests {

  @InjectMocks private TimeOffController timeOffController;

  @Mock private TimeOffService timeOffService;

  @Mock private EmployeeProfileService employeeProfileService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetTimeOffByEmployeeIdEmployeeNotFound() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(false);

    ResponseEntity<?> response = timeOffController.getTimeOffByEmployeeId(1);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Employee does not exist", response.getBody());
  }

  @Test
  void testGetTimeOffByEmployeeIdNoRecordsFound() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(true);
    when(timeOffService.getTimeOffByEmployeeId(1)).thenReturn(Collections.emptyList());

    ResponseEntity<?> response = timeOffController.getTimeOffByEmployeeId(1);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("No time-off records found for the employee", response.getBody());
  }

  @Test
  void testGetTimeOffByEmployeeIdSuccess() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(true);
    TimeOff timeOff = new TimeOff();
    timeOff.setEmployeeId(1);
    List<TimeOff> timeOffList = List.of(timeOff);
    when(timeOffService.getTimeOffByEmployeeId(1)).thenReturn(timeOffList);

    ResponseEntity<?> response = timeOffController.getTimeOffByEmployeeId(1);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(timeOffList, response.getBody());
  }

  @Test
  void testGetTimeOffInRangeEmployeeNotFound() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(false);

    ResponseEntity<?> response = timeOffController.getTimeOffInRange(1, "2024-01-01", "2024-01-10");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Employee does not exist", response.getBody());
  }

  @Test
  void testGetTimeOffInRangeInvalidDateRange() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(true);

    ResponseEntity<?> response = timeOffController.getTimeOffInRange(1, "2024-01-10", "2024-01-01");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Start date must be before end date", response.getBody());
  }

  @Test
  void testGetTimeOffInRangeNoRecordsFound() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(true);
    when(timeOffService.getTimeOffByEmployeeIdWithDateRange(
            1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10")))
        .thenReturn(Collections.emptyList());

    ResponseEntity<?> response = timeOffController.getTimeOffInRange(1, "2024-01-01", "2024-01-10");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("No time-off requests found in the specified date range", response.getBody());
  }

  @Test
  void testGetTimeOffInRangeSuccess() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(true);
    TimeOff timeOff = new TimeOff();
    timeOff.setEmployeeId(1);
    List<TimeOff> timeOffList = List.of(timeOff);
    when(timeOffService.getTimeOffByEmployeeIdWithDateRange(
            1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10")))
        .thenReturn(timeOffList);

    ResponseEntity<?> response = timeOffController.getTimeOffInRange(1, "2024-01-01", "2024-01-10");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(timeOffList, response.getBody());
  }

  @Test
  void testCreateTimeOffRequestEmployeeNotFound() {
    TimeOff timeOff = new TimeOff();
    timeOff.setEmployeeId(1);
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(false);

    ResponseEntity<?> response = timeOffController.createTimeOffRequest(timeOff);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Employee does not exist", response.getBody());
  }

  @Test
  void testCreateTimeOffRequestSuccess() {
    TimeOff timeOff = new TimeOff();
    timeOff.setEmployeeId(1);
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(true);
    when(timeOffService.createTimeOffRequest(any(TimeOff.class)))
        .thenAnswer(
            invocation -> {
              TimeOff newTimeOff = invocation.getArgument(0);
              newTimeOff.setId(1);
              return newTimeOff;
            });

    ResponseEntity<?> response = timeOffController.createTimeOffRequest(timeOff);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    TimeOff createdTimeOff = (TimeOff) response.getBody();
    assertEquals(1, createdTimeOff.getId());
    assertEquals(LeaveStatus.PENDING, createdTimeOff.getStatus());
  }

  @Test
  void testUpdateTimeOffStatusEmployeeNotFound() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(false);

    ResponseEntity<?> response = timeOffController.updateTimeOffStatus(1, 1, "approve");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Employee does not exist", response.getBody());
  }

  @Test
  void testUpdateTimeOffStatusInvalidTimeOffId() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(true);

    ResponseEntity<?> response = timeOffController.updateTimeOffStatus(1, 0, "approve");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid time-off request ID", response.getBody());
  }

  @Test
  void testUpdateTimeOffStatusInvalidAction() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(true);

    ResponseEntity<?> response = timeOffController.updateTimeOffStatus(1, 1, "");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Action is required", response.getBody());
  }

  @Test
  void testUpdateTimeOffStatusSuccess() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(true);
    when(timeOffService.updateTimeOffStatus(1, 1, "approve")).thenReturn(true);

    ResponseEntity<?> response = timeOffController.updateTimeOffStatus(1, 1, "approve");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Time-off request status updated successfully", response.getBody());
  }

  @Test
  void testUpdateTimeOffStatusTimeOffNotFound() {
    when(employeeProfileService.doesEmployeeExist(1)).thenReturn(true);
    when(timeOffService.updateTimeOffStatus(1, 1, "approve")).thenReturn(false);

    ResponseEntity<?> response = timeOffController.updateTimeOffStatus(1, 1, "approve");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Time-off request not found or action not applicable", response.getBody());
  }

  @Test
  void deleteTimeOffRequest_Success() {
    Integer employeeId = 1;
    Integer timeOffId = 101;
    when(employeeProfileService.doesEmployeeExist(employeeId)).thenReturn(true);
    when(timeOffService.deleteTimeOffRequest(employeeId, timeOffId)).thenReturn(true);

    ResponseEntity<?> response = timeOffController.deleteTimeOffRequest(employeeId, timeOffId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void deleteTimeOffRequest_NotFound() {
    Integer employeeId = 1;
    Integer timeOffId = 101;
    when(employeeProfileService.doesEmployeeExist(employeeId)).thenReturn(true);
    when(timeOffService.deleteTimeOffRequest(employeeId, timeOffId)).thenReturn(false);

    ResponseEntity<?> response = timeOffController.deleteTimeOffRequest(employeeId, timeOffId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Time-off request not found or already deleted", response.getBody());
  }

}
