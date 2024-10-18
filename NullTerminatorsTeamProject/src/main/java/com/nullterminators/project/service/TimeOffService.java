package com.nullterminators.project.service;

import com.nullterminators.project.enums.LeaveStatus;
import com.nullterminators.project.model.TimeOff;
import com.nullterminators.project.repository.TimeOffRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service for employees timeoff records. */
@Service
public class TimeOffService {

  private final TimeOffRepository timeOffRepository;
  @Autowired private Validator validator;

  @Autowired
  public TimeOffService(TimeOffRepository timeOffRepository) {
    this.timeOffRepository = timeOffRepository;
  }

  /**
   * Get all time-off requests for a specific employee within a given date range.
   *
   * @param employeeId the ID of the employee
   * @param startDate the start date of the range
   * @param endDate the end date of the range
   * @return a list of TimeOff requests
   */
  public List<TimeOff> getTimeOffByEmployeeIdWithDateRange(
      Integer employeeId, LocalDate startDate, LocalDate endDate) {
    return timeOffRepository.findAllByEmployeeIdGivenDateRange(employeeId, startDate, endDate);
  }

  /**
   * Get all time-off requests for a specific employee, ordered by most recent start date.
   *
   * @param employeeId the ID of the employee
   * @return a list of TimeOff requests
   */
  public List<TimeOff> getTimeOffByEmployeeId(Integer employeeId) {
    return timeOffRepository.findAllByEmployeeIdOrderByStartDateDesc(employeeId);
  }

  /**
   * Create a new time-off request.
   *
   * @param timeOff the TimeOff object to create
   * @return the created TimeOff object
   */
  public TimeOff createTimeOffRequest(TimeOff timeOff) {
    Set<ConstraintViolation<TimeOff>> violations = validator.validate(timeOff);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<TimeOff> violation : violations) {
        sb.append(violation.getMessage()).append("; ");
      }
      throw new IllegalArgumentException("Validation failed: " + sb.toString());
    } else {
      return timeOffRepository.save(timeOff);
    }
  }

  /**
   * Update the status of a time-off request.
   *
   * @param employeeId the ID of the employee
   * @param timeOffId the ID of the time-off request
   * @param action the action to perform (approve, reject, or cancel)
   * @return true if the status was updated, false otherwise
   */
  public boolean updateTimeOffStatus(Integer employeeId, Integer timeOffId, String action) {
    Optional<TimeOff> timeOffOptional = timeOffRepository.findById(timeOffId);

    if (timeOffOptional.isPresent()) {
      TimeOff timeOff = timeOffOptional.get();

      if (!timeOff.getEmployeeId().equals(employeeId)) {
        return false;
      }

      switch (action.toLowerCase(Locale.US)) {
        case "approve":
          timeOff.setStatus(LeaveStatus.APPROVED);
          break;
        case "reject":
          timeOff.setStatus(LeaveStatus.REJECTED);
          break;
        case "cancel":
          timeOff.setStatus(LeaveStatus.CANCELLED);
          break;
        default:
          throw new IllegalArgumentException(
              "Invalid action: " + action + ". Please either approve/reject or cancel");
      }

      timeOffRepository.save(timeOff);
      return true;
    } else {
      return false;
    }
  }
}
