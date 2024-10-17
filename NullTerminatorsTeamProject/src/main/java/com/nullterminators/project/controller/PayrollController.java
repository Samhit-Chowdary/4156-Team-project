package com.nullterminators.project.controller;

import com.nullterminators.project.service.PayrollService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Endpoints for Payroll.
 */
@RestController
@RequestMapping("/payroll")
public class PayrollController {

  private final PayrollService payrollService;

  @Autowired
  public PayrollController(PayrollService payrollService) {
    this.payrollService = payrollService;
  }

  /**
   * Get Payroll by employee id.
   *
   * @param employeeId    A (@code int) representing the employee id the user wishes to get
   *                      the payrolls for.
   *
   * @return              A (@code ResponseEntity) object containing either a list of required
   *                      details and an HTTP 200 response or, an appropriate message
   *                      indicating the proper response.
   */
  @GetMapping(value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getPayrollByEmployeeId(@PathVariable("employeeId") Integer employeeId) {
    try {
      List<Map<String, Object>> result = payrollService.getPayrollByEmployeeId(employeeId);
      if (result.isEmpty()) {
        return new ResponseEntity<>(Map.of("response", "Details Not Found"), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to mark a payroll as paid.
   *
   * @param employeeId    A (@code int) representing the employee id the user wishes to get
   *                      the payrolls for.
   * @param updates       A (@code Map) of details that the user provides to update the
   *                      payroll for a particular month and year.
   *
   * @return              A (@code ResponseEntity) object containing either a response and
   *                      an HTTP 200 response or, an appropriate message indicating the
   *                      proper response.
   */
  @PatchMapping(value = "/{employeeId}/markPaid", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> markAsPaid(@PathVariable("employeeId") Integer employeeId,
                                      @RequestBody Map<String, Object> updates) {
    try {
      PayrollService.PayrollStatus result = payrollService.markAsPaid(employeeId, updates);
      return switch (result) {
        case INVALID_DATA -> new ResponseEntity<>(Map.of("response", "Invalid month or year"),
                HttpStatus.BAD_REQUEST);
        case INVALID_FORMAT -> new ResponseEntity<>(Map.of("response",
                "Invalid format for month or year"), HttpStatus.BAD_REQUEST);
        case NOT_FOUND -> new ResponseEntity<>(Map.of("response",
                "Payroll for this month and year Not Found"),
                HttpStatus.NOT_FOUND);
        case ALREADY_COMPLETED -> new ResponseEntity<>(Map.of("response",
                "Employee has already been marked as Paid"),
                HttpStatus.BAD_REQUEST);
        case SUCCESS -> new ResponseEntity<>(Map.of("response",
                "Attribute was updated successfully"), HttpStatus.OK);
        default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      };
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to mark a payroll as unpaid.
   *
   * @param employeeId    A (@code int) representing the employee id the user wishes to update
   *                      the payroll for.
   * @param updates       A (@code Map) of details that the user provides to update the
   *                      payroll for a particular month and year.
   *
   * @return              A (@code ResponseEntity) object containing either a response and
   *                      an HTTP 200 response or, an appropriate message indicating the
   *                      proper response.
   */
  @PatchMapping(value = "/{employeeId}/markUnpaid", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> markAsUnpaid(@PathVariable("employeeId") Integer employeeId,
                                        @RequestBody Map<String, Object> updates) {
    try {
      PayrollService.PayrollStatus result = payrollService.markAsUnpaid(employeeId, updates);
      return switch (result) {
        case INVALID_DATA -> new ResponseEntity<>(Map.of("response", "Invalid month or year"),
                HttpStatus.BAD_REQUEST);
        case INVALID_FORMAT -> new ResponseEntity<>(Map.of("response",
                "Invalid format for month or year"), HttpStatus.BAD_REQUEST);
        case NOT_FOUND -> new ResponseEntity<>(Map.of("response",
                "Payroll for this month and year Not Found"),
                HttpStatus.NOT_FOUND);
        case ALREADY_COMPLETED -> new ResponseEntity<>(Map.of("response",
                "Employee has already been marked as Not Paid"),
                HttpStatus.BAD_REQUEST);
        case SUCCESS -> new ResponseEntity<>(Map.of("response",
                "Attribute was updated successfully"), HttpStatus.OK);
        default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      };
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to delete a payroll entry.
   *
   * @param employeeId    A (@code int) representing the employee id the user wishes to delete
   *                      the payroll for.
   * @param updates       A (@code Map) of details that the user provides to update the
   *                      payroll for a particular month and year.
   *
   * @return              A (@code ResponseEntity) object containing either a response and
   *                      an HTTP 200 response or, an appropriate message indicating the
   *                      proper response.
   */
  @DeleteMapping(value = "/{employeeId}/deletePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deletePayrollByEmployeeId(@PathVariable("employeeId") Integer employeeId,
                                                     @RequestBody Map<String, Object> updates) {
    try {
      PayrollService.PayrollStatus result =
              payrollService.deletePayrollByEmployeeId(employeeId, updates);
      return switch (result) {
        case INVALID_DATA -> new ResponseEntity<>(Map.of("response", "Invalid month or year"),
                HttpStatus.BAD_REQUEST);
        case INVALID_FORMAT -> new ResponseEntity<>(Map.of("response",
                "Invalid format for month or year"), HttpStatus.BAD_REQUEST);
        case NOT_FOUND -> new ResponseEntity<>(Map.of("response",
                "Payroll for this month and year Not Found"),
                HttpStatus.NOT_FOUND);
        case SUCCESS -> new ResponseEntity<>(Map.of("response",
                "Attribute was deleted successfully"), HttpStatus.OK);
        default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      };
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to add a payroll entry.
   *
   * @param employeeId    A (@code int) representing the employee id the user wishes to add
   *                      the payrolls for.
   * @param updates       A (@code Map) of details that the user provides to add the
   *                      payroll for a particular month and year including details about
   *                      day of payment and salary.
   *
   * @return              A (@code ResponseEntity) object containing either a response and
   *                      an HTTP 200 response or, an appropriate message indicating
   *                      the proper response.
   */
  @PostMapping(value = "/{employeeId}/addPayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createPayroll(@PathVariable("employeeId") Integer employeeId,
                                         @RequestBody Map<String, Object> updates) {
    try {
      PayrollService.PayrollStatus result =
              payrollService.addPayrollByEmployeeId(employeeId, updates);
      return switch (result) {
        case INVALID_DATA -> new ResponseEntity<>(Map.of("response", "Invalid month or year"),
                HttpStatus.BAD_REQUEST);
        case INVALID_FORMAT -> new ResponseEntity<>(Map.of("response",
                "Invalid format for month or year"), HttpStatus.BAD_REQUEST);
        case ALREADY_EXISTS -> new ResponseEntity<>(Map.of("response",
                "Payroll for this month and year already exists"),
                HttpStatus.CONFLICT);
        case SUCCESS -> new ResponseEntity<>(Map.of("response",
                "Attribute was created successfully"), HttpStatus.OK);
        default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      };
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to adjust the salary of an employee.
   *
   * @param employeeId    A (@code int) representing the employee id the user wishes to adjust
   *                      the salary for.
   * @param updates       A (@code Map) of details that the user provides to adjust the
   *                      salary for a particular month and year.
   *
   * @return              A (@code ResponseEntity) object containing either a response and
   *                      an HTTP 200 response or, an appropriate message indicating the
   *                      proper response.
   */
  @PatchMapping(value = "/{employeeId}/adjustSalary", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> adjustSalary(@PathVariable("employeeId") Integer employeeId,
                                        @RequestBody Map<String, Object> updates) {
    try {
      PayrollService.PayrollStatus result =
              payrollService.adjustSalaryByEmployeeId(employeeId, updates);
      return switch (result) {
        case INVALID_DATA -> new ResponseEntity<>(Map.of("response",
                "Invalid month or year or salary"), HttpStatus.BAD_REQUEST);
        case INVALID_FORMAT -> new ResponseEntity<>(Map.of("response",
                "Invalid format for month or year or salary"),
                HttpStatus.BAD_REQUEST);
        case NOT_FOUND -> new ResponseEntity<>(Map.of("response",
                "Payroll for this month and year Not Found"),
                HttpStatus.NOT_FOUND);
        case SUCCESS -> new ResponseEntity<>(Map.of("response",
                "Attribute was updated successfully"), HttpStatus.OK);
        default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      };
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to adjust the day of payment of an employee.
   *
   * @param employeeId    A (@code int) representing the employee id the user wishes to adjust
   *                      the day of payment for.
   * @param updates       A (@code Map) of details that the user provides to adjust the
   *                      day of payment for a particular month and year.
   *
   * @return              A (@code ResponseEntity) object containing either a response and
   *                      an HTTP 200 response or, an appropriate message indicating the
   *                      proper response.
   */
  @PatchMapping(value = "/{employeeId}/adjustDay", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> adjustDay(@PathVariable("employeeId") Integer employeeId,
                                     @RequestBody Map<String, Object> updates) {
    try {
      PayrollService.PayrollStatus result =
              payrollService.adjustPaymentDayByEmployeeId(employeeId, updates);
      return switch (result) {
        case INVALID_DATA -> new ResponseEntity<>(Map.of("response",
                "Invalid day or month or year"), HttpStatus.BAD_REQUEST);
        case INVALID_FORMAT -> new ResponseEntity<>(Map.of("response",
                "Invalid format for day or month or year"),
                HttpStatus.BAD_REQUEST);
        case NOT_FOUND -> new ResponseEntity<>(Map.of("response",
                "Payroll for this month and year Not Found"),
                HttpStatus.NOT_FOUND);
        case SUCCESS -> new ResponseEntity<>(Map.of("response",
                "Attribute was updated successfully"), HttpStatus.OK);
        default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      };
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to generate a payroll for the entire company.
   *
   * @param updates       A (@code Map) of details that the user provides to generate
   *                      the payrolls for a particular month and year.
   *
   * @return              A (@code ResponseEntity) object containing either a response and
   *                      an HTTP 200 response or, an appropriate message indicating the
   *                      proper response.
   */
  @PostMapping(value = "/generatePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> generatePayroll(@RequestBody Map<String, Object> updates) {
    try {
      Map<String, Object> result = payrollService.generatePayroll(updates);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to delete a payroll for the entire company.
   *
   * @param updates       A (@code Map) of details that the user provides to delete
   *                      the payrolls for a particular month and year.
   *
   * @return              A (@code ResponseEntity) object containing either a response and
   *                      an HTTP 200 response or, an appropriate message indicating the
   *                      proper response.
   */
  @DeleteMapping(value = "/deletePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deletePayroll(@RequestBody Map<String, Object> updates) {
    try {
      Map<String, Object> result = payrollService.deletePayroll(updates);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<?> handleException(Exception e) {
    return new ResponseEntity<>(Map.of("response", e.toString()),
            HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
