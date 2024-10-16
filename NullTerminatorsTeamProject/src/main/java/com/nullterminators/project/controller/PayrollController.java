package com.nullterminators.project.controller;

import com.nullterminators.project.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    @Autowired
    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @GetMapping(value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPayrollByEmployeeId(@PathVariable("employeeId") Integer employeeId) {
        try{
            List<Map<String, Object>> result = payrollService.getPayrollByEmployeeId(employeeId);
            if (result.isEmpty()) {
                return new ResponseEntity<>(Map.of("response", "Details Not Found"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping(value = "/{employeeId}/markPaid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> markAsPaid(@PathVariable("employeeId") Integer employeeId, @RequestBody Map<String, Object> updates) {
        try{
            Integer result = payrollService.markAsPaid(employeeId, updates);
            return switch (result) {
                case 1 -> new ResponseEntity<>(Map.of("response", "Invalid month or year"), HttpStatus.BAD_REQUEST);
                case 2 -> new ResponseEntity<>(Map.of("response", "Invalid format for month or year"), HttpStatus.BAD_REQUEST);
                case 3 -> new ResponseEntity<>(Map.of("response", "Payroll for this month and year Not Found"), HttpStatus.NOT_FOUND);
                case 4 -> new ResponseEntity<>(Map.of("response", "Employee has already been marked as Paid"), HttpStatus.BAD_REQUEST);
                case 5 -> new ResponseEntity<>(Map.of("response", "Attribute was updated successfully"), HttpStatus.OK);
                default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping(value = "/{employeeId}/markUnpaid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> markAsUnpaid(@PathVariable("employeeId") Integer employeeId, @RequestBody Map<String, Object> updates) {
        try{
            Integer result = payrollService.markAsUnpaid(employeeId, updates);
            return switch (result) {
                case 1 -> new ResponseEntity<>(Map.of("response", "Invalid month or year"), HttpStatus.BAD_REQUEST);
                case 2 -> new ResponseEntity<>(Map.of("response", "Invalid format for month or year"), HttpStatus.BAD_REQUEST);
                case 3 -> new ResponseEntity<>(Map.of("response", "Payroll for this month and year Not Found"), HttpStatus.NOT_FOUND);
                case 4 -> new ResponseEntity<>(Map.of("response", "Employee has already been marked as Not Paid"), HttpStatus.BAD_REQUEST);
                case 5 -> new ResponseEntity<>(Map.of("response", "Attribute was updated successfully"), HttpStatus.OK);
                default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping(value = "/{employeeId}/deletePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePayrollByEmployeeId(@PathVariable("employeeId") Integer employeeId, @RequestBody Map<String, Object> updates) {
        try{
            Integer result = payrollService.deletePayrollByEmployeeId(employeeId, updates);
            return switch (result) {
                case 1 -> new ResponseEntity<>(Map.of("response", "Invalid month or year"), HttpStatus.BAD_REQUEST);
                case 2 -> new ResponseEntity<>(Map.of("response", "Invalid format for month or year"), HttpStatus.BAD_REQUEST);
                case 3 -> new ResponseEntity<>(Map.of("response", "Payroll for this month and year Not Found"), HttpStatus.NOT_FOUND);
                case 4 -> new ResponseEntity<>(Map.of("response", "Attribute was deleted successfully"), HttpStatus.OK);
                default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping(value = "/{employeeId}/addPayroll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPayroll(@PathVariable("employeeId") Integer employeeId, @RequestBody Map<String, Object> updates) {
        try{
            Integer result = payrollService.addPayrollByEmployeeId(employeeId, updates);
            return switch (result) {
                case 1 -> new ResponseEntity<>(Map.of("response", "Invalid month or year"), HttpStatus.BAD_REQUEST);
                case 2 -> new ResponseEntity<>(Map.of("response", "Invalid format for month or year"), HttpStatus.BAD_REQUEST);
                case 3 -> new ResponseEntity<>(Map.of("response", "Payroll for this month and year already exists"), HttpStatus.CONFLICT);
                case 4 -> new ResponseEntity<>(Map.of("response", "Attribute was created successfully"), HttpStatus.OK);
                default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping(value = "/{employeeId}/adjustSalary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> adjustSalary(@PathVariable("employeeId") Integer employeeID, @RequestBody Map<String, Object> updates) {
        try{
            Integer result = payrollService.adjustSalaryByEmployeeId(employeeID, updates);
            return switch (result) {
                case 1 -> new ResponseEntity<>(Map.of("response", "Invalid month or year or salary"), HttpStatus.BAD_REQUEST);
                case 2 -> new ResponseEntity<>(Map.of("response", "Invalid format for month or year or salary"), HttpStatus.BAD_REQUEST);
                case 3 -> new ResponseEntity<>(Map.of("response", "Payroll for this month and year Not Found"), HttpStatus.NOT_FOUND);
                case 4 -> new ResponseEntity<>(Map.of("response", "Attribute was updated successfully"), HttpStatus.OK);
                default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping(value = "/{employeeId}/adjustDay", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> adjustDay(@PathVariable("employeeId") Integer employeeID, @RequestBody Map<String, Object> updates) {
        try{
            Integer result = payrollService.adjustPaymentDayByEmployeeId(employeeID, updates);
            return switch (result) {
                case 1 -> new ResponseEntity<>(Map.of("response", "Invalid day or month or year"), HttpStatus.BAD_REQUEST);
                case 2 -> new ResponseEntity<>(Map.of("response", "Invalid format for day or month or year"), HttpStatus.BAD_REQUEST);
                case 3 -> new ResponseEntity<>(Map.of("response", "Payroll for this month and year Not Found"), HttpStatus.NOT_FOUND);
                case 4 -> new ResponseEntity<>(Map.of("response", "Attribute was updated successfully"), HttpStatus.OK);
                default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping(value = "/generatePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generatePayroll(@RequestBody Map<String, Object> updates) {
        try{
            Map<String, Object> result = payrollService.generatePayroll(updates);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping(value = "/deletePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePayroll(@RequestBody Map<String, Object> updates) {
        try{
            Map<String, Object> result = payrollService.deletePayroll(updates);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<?> handleException(Exception e) {
        System.out.println(e.toString());
        return new ResponseEntity<>(Map.of("response", "An Error has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
