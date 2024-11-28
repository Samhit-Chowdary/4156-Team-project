package com.nullterminators.project.controller;

import com.nullterminators.project.enums.LeaveStatus;
import com.nullterminators.project.model.TimeOff;
import com.nullterminators.project.service.EmployeeProfileService;
import com.nullterminators.project.service.TimeOffService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** API endpoints for managing time-off requests. */
@RestController
@RequestMapping("/timeoff")
public class TimeOffController {

  private final TimeOffService timeOffService;
  private final EmployeeProfileService employeeProfileService;

  @Autowired
  public TimeOffController(
      TimeOffService timeOffService, EmployeeProfileService employeeProfileService) {
    this.timeOffService = timeOffService;
    this.employeeProfileService = employeeProfileService;
  }

  /**
   * GET /timeoff/{employeeId} - retrieves all time-off requests for a specific employee.
   *
   * @param employeeId the ID of the employee
   * @return ResponseEntity with appropriate status and time-off records or error message
   */
  @GetMapping(value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getTimeOffByEmployeeId(@PathVariable("employeeId") Integer employeeId) {
    if (!employeeProfileService.doesEmployeeExist(employeeId)) {
      return new ResponseEntity<>("Employee does not exist", HttpStatus.NOT_FOUND);
    }

    try {
      List<TimeOff> timeOffList = timeOffService.getTimeOffByEmployeeId(employeeId);
      if (timeOffList.isEmpty()) {
        return new ResponseEntity<>(
            "No time-off records found for the employee", HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(timeOffList, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * GET /timeoff/{employeeId}/range - retrieves time-off requests for a specific employee within a
   * date range.
   *
   * @param employeeId the ID of the employee
   * @param startDateStr the start date in String format
   * @param endDateStr the end date in String format
   * @return ResponseEntity with appropriate status and time-off records or error message
   */
  @GetMapping(value = "/{employeeId}/range", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getTimeOffInRange(
      @PathVariable("employeeId") Integer employeeId,
      @RequestParam("startDate") String startDateStr,
      @RequestParam("endDate") String endDateStr) {
    if (!employeeProfileService.doesEmployeeExist(employeeId)) {
      return new ResponseEntity<>("Employee does not exist", HttpStatus.NOT_FOUND);
    }

    if (startDateStr == null
        || endDateStr == null
        || startDateStr.isEmpty()
        || endDateStr.isEmpty()) {
      return new ResponseEntity<>("Start date and end date are required", HttpStatus.BAD_REQUEST);
    }

    try {
      LocalDate startDate = LocalDate.parse(startDateStr);
      LocalDate endDate = LocalDate.parse(endDateStr);

      if (!startDate.isBefore(endDate)) {
        return new ResponseEntity<>("Start date must be before end date", HttpStatus.BAD_REQUEST);
      }

      List<TimeOff> timeOffList =
          timeOffService.getTimeOffByEmployeeIdWithDateRange(employeeId, startDate, endDate);
      if (timeOffList.isEmpty()) {
        return new ResponseEntity<>(
            "No time-off requests found in the specified date range", HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(timeOffList, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * POST /timeoff/create - creates a new time-off request for a specific employee.
   *
   * @param timeOff the TimeOff object containing the request details
   * @return ResponseEntity with appropriate status and the created time-off request or error
   *     message
   */
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createTimeOffRequest(@RequestBody TimeOff timeOff) {
    if (!employeeProfileService.doesEmployeeExist(timeOff.getEmployeeId())) {
      return new ResponseEntity<>("Employee does not exist", HttpStatus.NOT_FOUND);
    }

    try {
      timeOff.setStatus(LeaveStatus.PENDING);
      timeOff.setRequestDate(LocalDate.now());
      TimeOff newTimeOff = timeOffService.createTimeOffRequest(timeOff);
      return new ResponseEntity<>(newTimeOff, HttpStatus.CREATED);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * PUT /timeoff/{employeeId}/{timeOffId}/update-status - updates the status of a specific time-off
   * request.
   *
   * @param employeeId the ID of the employee
   * @param timeOffId the ID of the time-off request
   * @param action the action to be performed (approve/reject/cancel)
   * @return ResponseEntity with appropriate status and message
   */
  @PutMapping(
      value = "/{employeeId}/{timeOffId}/update-status",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateTimeOffStatus(
      @PathVariable("employeeId") Integer employeeId,
      @PathVariable("timeOffId") Integer timeOffId,
      @RequestParam("action") String action) {
    if (!employeeProfileService.doesEmployeeExist(employeeId)) {
      return new ResponseEntity<>("Employee does not exist", HttpStatus.NOT_FOUND);
    }

    if (timeOffId == null || timeOffId <= 0) {
      return new ResponseEntity<>("Invalid time-off request ID", HttpStatus.BAD_REQUEST);
    }

    if (action == null || action.isEmpty()) {
      return new ResponseEntity<>("Action is required", HttpStatus.BAD_REQUEST);
    }

    try {
      boolean isUpdated = timeOffService.updateTimeOffStatus(employeeId, timeOffId, action);
      if (isUpdated) {
        return new ResponseEntity<>("Time-off request status updated successfully", HttpStatus.OK);
      }
      return new ResponseEntity<>(
          "Time-off request not found or action not applicable", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * DELETE /timeoff/{employeeId}/{timeOffId} - deletes a specific time-off request for an employee.
   *
   * @param employeeId the ID of the employee
   * @param timeOffId the ID of the time-off request
   * @return ResponseEntity with appropriate status and message
   */
  @DeleteMapping(value = "/{employeeId}/{timeOffId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteTimeOffRequest(
          @PathVariable("employeeId") Integer employeeId,
          @PathVariable("timeOffId") Integer timeOffId) {
    if (!employeeProfileService.doesEmployeeExist(employeeId)) {
      return new ResponseEntity<>("Employee does not exist", HttpStatus.NOT_FOUND);
    }

    if (timeOffId == null || timeOffId <= 0) {
      return new ResponseEntity<>("Invalid time-off request ID", HttpStatus.BAD_REQUEST);
    }

    try {
      boolean isDeleted = timeOffService.deleteTimeOffRequest(employeeId, timeOffId);
      if (isDeleted) {
        return new ResponseEntity<>("Time-off request deleted successfully", HttpStatus.OK);
      }
      return new ResponseEntity<>(
              "Time-off request not found or already deleted", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }


  private ResponseEntity<?> handleException(Exception e) {
    System.out.println(e.toString());
    return new ResponseEntity<>(
        "An error has occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
