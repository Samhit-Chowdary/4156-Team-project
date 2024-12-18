package com.nullterminators.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nullterminators.project.enums.LeaveStatus;
import com.nullterminators.project.enums.LeaveType;
import com.nullterminators.project.model.TimeOff;
import com.nullterminators.project.repository.TimeOffRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** Tests for timeoff service methods. */
class TimeOffServiceTests {

  @Mock private TimeOffRepository timeOffRepository;

  @InjectMocks private TimeOffService timeOffService;

  private TimeOff timeOff1;
  private TimeOff timeOff2;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    timeOff1 = new TimeOff();
    timeOff1.setId(1);
    timeOff1.setEmployeeId(123);
    timeOff1.setStartDate(LocalDate.of(2024, 1, 1));
    timeOff1.setEndDate(LocalDate.of(2024, 1, 10));
    timeOff1.setStatus(LeaveStatus.PENDING);

    timeOff2 = new TimeOff();
    timeOff2.setId(2);
    timeOff2.setEmployeeId(123);
    timeOff2.setStartDate(LocalDate.of(2024, 2, 1));
    timeOff2.setEndDate(LocalDate.of(2024, 2, 5));
    timeOff2.setStatus(LeaveStatus.PENDING);
  }

  @Test
  void testGetTimeOffByEmployeeIdWithDateRange() {
    when(timeOffRepository.findAllByEmployeeIdGivenDateRange(
            123, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10)))
        .thenReturn(Arrays.asList(timeOff1));

    List<TimeOff> result =
        timeOffService.getTimeOffByEmployeeIdWithDateRange(
            123, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10));

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(timeOff1, result.get(0));
  }

  @Test
  void testGetTimeOffByEmployeeId() {
    when(timeOffRepository.findAllByEmployeeIdOrderByStartDateDesc(123))
        .thenReturn(Arrays.asList(timeOff1, timeOff2));

    List<TimeOff> result = timeOffService.getTimeOffByEmployeeId(123);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(timeOff1, result.get(0)); // Most recent first
    assertEquals(timeOff2, result.get(1));
  }

  @Test
  void testUpdateTimeOffStatusApproved() {
    when(timeOffRepository.findById(1)).thenReturn(Optional.of(timeOff1));

    boolean result = timeOffService.updateTimeOffStatus(123, 1, "approve");

    assertTrue(result);
    assertEquals(LeaveStatus.APPROVED, timeOff1.getStatus());
    verify(timeOffRepository, times(1)).save(timeOff1);
  }

  @Test
  void testUpdateTimeOffStatusRejected() {
    when(timeOffRepository.findById(1)).thenReturn(Optional.of(timeOff1));

    boolean result = timeOffService.updateTimeOffStatus(123, 1, "reject");

    assertTrue(result);
    assertEquals(LeaveStatus.REJECTED, timeOff1.getStatus());
    verify(timeOffRepository, times(1)).save(timeOff1);
  }

  @Test
  void testUpdateTimeOffStatusCancelled() {
    when(timeOffRepository.findById(1)).thenReturn(Optional.of(timeOff1));

    boolean result = timeOffService.updateTimeOffStatus(123, 1, "cancel");

    assertTrue(result);
    assertEquals(LeaveStatus.CANCELLED, timeOff1.getStatus());
    verify(timeOffRepository, times(1)).save(timeOff1);
  }

  @Test
  void testUpdateTimeOffStatusInvalidAction() {
    when(timeOffRepository.findById(1)).thenReturn(Optional.of(timeOff1));

    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              timeOffService.updateTimeOffStatus(123, 1, "invalid");
            });

    assertEquals(
        "Invalid action: invalid. Please either approve/reject or cancel", exception.getMessage());
  }

  @Test
  void testUpdateTimeOffStatusNotFound() {
    when(timeOffRepository.findById(1)).thenReturn(Optional.empty());

    boolean result = timeOffService.updateTimeOffStatus(123, 1, "approve");

    assertFalse(result);
    verify(timeOffRepository, never()).save(any(TimeOff.class));
  }

  @Test
  void testUpdateTimeOffStatusEmployeeMismatch() {
    timeOff1.setEmployeeId(456); // Set a different employee ID
    when(timeOffRepository.findById(1)).thenReturn(Optional.of(timeOff1));

    boolean result = timeOffService.updateTimeOffStatus(123, 1, "approve");

    assertFalse(result);
    assertEquals(LeaveStatus.PENDING, timeOff1.getStatus()); // Status should not change
    verify(timeOffRepository, never()).save(any(TimeOff.class));
  }

  @Test
  void testDeleteTimeOffRequestSuccess() {
    when(timeOffRepository.deleteByEmployeeIdAndTimeOffId(123, 1)).thenReturn(1);

    boolean result = timeOffService.deleteTimeOffRequest(123, 1);

    assertTrue(result);
    verify(timeOffRepository, times(1)).deleteByEmployeeIdAndTimeOffId(123, 1);
  }

  @Test
  void testDeleteTimeOffRequestFailureTimeOffNotFound() {
    when(timeOffRepository.deleteByEmployeeIdAndTimeOffId(123, 1)).thenReturn(0);

    boolean result = timeOffService.deleteTimeOffRequest(123, 1);

    assertFalse(result);
    verify(timeOffRepository, times(1)).deleteByEmployeeIdAndTimeOffId(123, 1);
  }

  @Test
  void testDeleteTimeOffRequestEmployeeMismatch() {
    when(timeOffRepository.deleteByEmployeeIdAndTimeOffId(123, 1)).thenReturn(1);

    boolean result = timeOffService.deleteTimeOffRequest(456, 1);

    assertFalse(result);
    verify(timeOffRepository, times(1)).deleteByEmployeeIdAndTimeOffId(456, 1);
  }

  @Test
  void testDeleteTimeOffRequestInvalidTimeOffId() {
    when(timeOffRepository.deleteByEmployeeIdAndTimeOffId(123, 999)).thenReturn(0);

    boolean result = timeOffService.deleteTimeOffRequest(123, 999);

    assertFalse(result);
    verify(timeOffRepository, times(1)).deleteByEmployeeIdAndTimeOffId(123, 999);
  }

}
