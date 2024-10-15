package com.nullterminators.project.controller;

import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.service.PayrollService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
            String result = payrollService.getPayrollByEmployeeId(employeeId);
            if (result.isEmpty()) {
                return new ResponseEntity<>("Employee Not Found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping(value = "/{employeeId}/markPaid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> markAsPaid(@PathVariable("employeeId") Integer employeeId, @RequestBody Map<String, Object> updates) {
        try{
            Integer result = payrollService.markAsPaid(employeeId, (String) updates.get("paymentMonth"), (String) updates.get("paymentYear"));
            return switch (result) {
                case 0 -> new ResponseEntity<>("Employee Not Found", HttpStatus.NOT_FOUND);
                case 1 -> new ResponseEntity<>("Payroll for this month and year Not Found", HttpStatus.BAD_REQUEST);
                case 2 -> new ResponseEntity<>("Employee has already been paid", HttpStatus.BAD_REQUEST);
                case 3 -> new ResponseEntity<>("Attribute was updated successfully", HttpStatus.OK);
                default -> new ResponseEntity<>("An Error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping(value = "/{employeeId}/markUnpaid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> markAsUnpaid(@PathVariable("employeeId") Integer employeeId, @RequestBody Map<String, Object> updates) {
        try{
            Integer result = payrollService.markAsUnpaid(employeeId, (String) updates.get("paymentMonth"), (String) updates.get("paymentYear"));
            return switch (result) {
                case 0 -> new ResponseEntity<>("Employee Not Found", HttpStatus.NOT_FOUND);
                case 1 -> new ResponseEntity<>("Payroll for this month and year Not Found", HttpStatus.BAD_REQUEST);
                case 2 -> new ResponseEntity<>("Employee has not been paid", HttpStatus.BAD_REQUEST);
                case 3 -> new ResponseEntity<>("Attribute was updated successfully", HttpStatus.OK);
                default -> new ResponseEntity<>("An Error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping(value = "/{employeeId}/deletePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePayroll(@PathVariable("employeeId") Integer employeeId, @RequestBody Map<String, Object> updates) {
        try{
            Integer result = payrollService.deletePayrollByEmployeeId(employeeId, (String) updates.get("paymentMonth"), (String) updates.get("paymentYear"));
            return switch (result) {
                case 0 -> new ResponseEntity<>("Employee Not Found", HttpStatus.NOT_FOUND);
                case 1 -> new ResponseEntity<>("Payroll for this month and year Not Found", HttpStatus.BAD_REQUEST);
                case 2 -> new ResponseEntity<>("Attribute was deleted successfully", HttpStatus.OK);
                default -> new ResponseEntity<>("An Error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping(value = "/{employeeId}/addPayroll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPayroll(@PathVariable("employeeId") Integer employeeId, @RequestBody Map<String, Object> updates) {
        try{
            Integer result = payrollService.addPayrollByEmployeeId(employeeId, (String) updates.get("salary"), (String) updates.get("paymentMonth"), (String) updates.get("paymentYear"));
            return switch (result) {
                case 0 -> new ResponseEntity<>("Employee Not Found", HttpStatus.NOT_FOUND);
                case 1 -> new ResponseEntity<>("Attribute was created successfully", HttpStatus.OK);
                default -> new ResponseEntity<>("An Error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<?> handleException(Exception e) {
        System.out.println(e.toString());
        return new ResponseEntity<>("An Error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
