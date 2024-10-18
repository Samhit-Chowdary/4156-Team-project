package com.nullterminators.project.controller;

import com.nullterminators.project.enums.PayrollStatus;
import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.PayrollRepository;
import com.nullterminators.project.service.PayrollService;
import com.nullterminators.project.util.pdf.PdfGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration
public class PayrollControllerTests {

    @Mock
    private PayrollService payrollService;

    private PayrollController payrollController;

    @BeforeEach
    void setUp() {
        payrollController = new PayrollController(payrollService);
    }

    @Test
    void testGetPayrollByEmployeeIdEmployeeNotFound() {
        when(payrollService.getPayrollByEmployeeId(1)).thenReturn(Pair.of(PayrollStatus.EMPLOYEE_NOT_FOUND, new ArrayList<>()));
        ResponseEntity<?> result = payrollController.getPayrollByEmployeeId(1);
        assertEquals(Map.of("response", "Employee Not Found in Company"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetPayrollByEmployeeIdSuccess() {
        Map<String, Object> payrollInfo = Map.of("employeeId", 1, "date", LocalDate.of(2024, 10, 17), "salary", 10000, "tax", 3000, "payslip", "N/A", "netSalary", 7000);
        when(payrollService.getPayrollByEmployeeId(1)).thenReturn(Pair.of(PayrollStatus.SUCCESS, List.of(payrollInfo)));
        ResponseEntity<?> result = payrollController.getPayrollByEmployeeId(1);
        assertEquals(List.of(payrollInfo), result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testMarkAsPaidInvalidData() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsPaid(1, updates)).thenReturn(PayrollStatus.INVALID_DATA);
        ResponseEntity<?> result = payrollController.markAsPaid(1, updates);
        assertEquals(Map.of("response", "Invalid month or year"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testMarkAsPaidInvalidFormat() {
        Map<String, Object> updates = Map.of("month", "10", "year", "2024");
        when(payrollService.markAsPaid(1, updates)).thenReturn(PayrollStatus.INVALID_FORMAT);
        ResponseEntity<?> result = payrollController.markAsPaid(1, updates);
        assertEquals(Map.of("response", "Invalid format for month or year"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testMarkAsPaidEmployeeNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsPaid(1, updates)).thenReturn(PayrollStatus.EMPLOYEE_NOT_FOUND);
        ResponseEntity<?> result = payrollController.markAsPaid(1, updates);
        assertEquals(Map.of("response", "Employee Not Found in Company"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testMarkAsPaidNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsPaid(1, updates)).thenReturn(PayrollStatus.NOT_FOUND);
        ResponseEntity<?> result = payrollController.markAsPaid(1, updates);
        assertEquals(Map.of("response", "Payroll for this month and year Not Found"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testMarkAsPaidAlreadyCompleted() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsPaid(1, updates)).thenReturn(PayrollStatus.ALREADY_COMPLETED);
        ResponseEntity<?> result = payrollController.markAsPaid(1, updates);
        assertEquals(Map.of("response", "Employee has already been marked as Paid"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testMarkAsPaidSuccess() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsPaid(1, updates)).thenReturn(PayrollStatus.SUCCESS);
        ResponseEntity<?> result = payrollController.markAsPaid(1, updates);
        assertEquals(Map.of("response", "Attribute was updated successfully"), result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testMarkAsPaidDefault() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsPaid(1, updates)).thenReturn(PayrollStatus.OK);
        ResponseEntity<?> result = payrollController.markAsPaid(1, updates);
        assertEquals(Map.of("response", "An Error has occurred"), result.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void testMarkAsUnpaidInvalidData() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsUnpaid(1, updates)).thenReturn(PayrollStatus.INVALID_DATA);
        ResponseEntity<?> result = payrollController.markAsUnpaid(1, updates);
        assertEquals(Map.of("response", "Invalid month or year"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testMarkAsUnpaidInvalidFormat() {
        Map<String, Object> updates = Map.of("month", "10", "year", "2024");
        when(payrollService.markAsUnpaid(1, updates)).thenReturn(PayrollStatus.INVALID_FORMAT);
        ResponseEntity<?> result = payrollController.markAsUnpaid(1, updates);
        assertEquals(Map.of("response", "Invalid format for month or year"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testMarkAsUnpaidEmployeeNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsUnpaid(1, updates)).thenReturn(PayrollStatus.EMPLOYEE_NOT_FOUND);
        ResponseEntity<?> result = payrollController.markAsUnpaid(1, updates);
        assertEquals(Map.of("response", "Employee Not Found in Company"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testMarkAsUnpaidNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsUnpaid(1, updates)).thenReturn(PayrollStatus.NOT_FOUND);
        ResponseEntity<?> result = payrollController.markAsUnpaid(1, updates);
        assertEquals(Map.of("response", "Payroll for this month and year Not Found"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testMarkAsUnpaidAlreadyCompleted() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsUnpaid(1, updates)).thenReturn(PayrollStatus.ALREADY_COMPLETED);
        ResponseEntity<?> result = payrollController.markAsUnpaid(1, updates);
        assertEquals(Map.of("response", "Employee has already been marked as Not Paid"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testMarkAsUnpaidSuccess() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsUnpaid(1, updates)).thenReturn(PayrollStatus.SUCCESS);
        ResponseEntity<?> result = payrollController.markAsUnpaid(1, updates);
        assertEquals(Map.of("response", "Attribute was updated successfully"), result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testMarkAsUnpaidDefault() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.markAsUnpaid(1, updates)).thenReturn(PayrollStatus.OK);
        ResponseEntity<?> result = payrollController.markAsUnpaid(1, updates);
        assertEquals(Map.of("response", "An Error has occurred"), result.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void testAdjustSalaryInvalidData() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.adjustSalaryByEmployeeId(1, updates)).thenReturn(PayrollStatus.INVALID_DATA);
        ResponseEntity<?> result = payrollController.adjustSalary(1, updates);
        assertEquals(Map.of("response", "Invalid month or year or salary"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testAdjustSalaryInvalidFormat() {
        Map<String, Object> updates = Map.of("month", "10", "year", "2024");
        when(payrollService.adjustSalaryByEmployeeId(1, updates)).thenReturn(PayrollStatus.INVALID_FORMAT);
        ResponseEntity<?> result = payrollController.adjustSalary(1, updates);
        assertEquals(Map.of("response", "Invalid format for month or year or salary"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testAdjustSalaryEmployeeNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.adjustSalaryByEmployeeId(1, updates)).thenReturn(PayrollStatus.EMPLOYEE_NOT_FOUND);
        ResponseEntity<?> result = payrollController.adjustSalary(1, updates);
        assertEquals(Map.of("response", "Employee Not Found in Company"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testAdjustSalaryNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.adjustSalaryByEmployeeId(1, updates)).thenReturn(PayrollStatus.NOT_FOUND);
        ResponseEntity<?> result = payrollController.adjustSalary(1, updates);
        assertEquals(Map.of("response", "Payroll for this month and year Not Found"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testAdjustSalarySuccess() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.adjustSalaryByEmployeeId(1, updates)).thenReturn(PayrollStatus.SUCCESS);
        ResponseEntity<?> result = payrollController.adjustSalary(1, updates);
        assertEquals(Map.of("response", "Attribute was updated successfully"), result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testAdjustSalaryDefault() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.adjustSalaryByEmployeeId(1, updates)).thenReturn(PayrollStatus.OK);
        ResponseEntity<?> result = payrollController.adjustSalary(1, updates);
        assertEquals(Map.of("response", "An Error has occurred"), result.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void testAdjustDayInvalidData() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.adjustPaymentDayByEmployeeId(1, updates)).thenReturn(PayrollStatus.INVALID_DATA);
        ResponseEntity<?> result = payrollController.adjustDay(1, updates);
        assertEquals(Map.of("response", "Invalid day or month or year"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testAdjustDayInvalidFormat() {
        Map<String, Object> updates = Map.of("month", "10", "year", "2024");
        when(payrollService.adjustPaymentDayByEmployeeId(1, updates)).thenReturn(PayrollStatus.INVALID_FORMAT);
        ResponseEntity<?> result = payrollController.adjustDay(1, updates);
        assertEquals(Map.of("response", "Invalid format for day or month or year"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testAdjustDayEmployeeNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.adjustPaymentDayByEmployeeId(1, updates)).thenReturn(PayrollStatus.EMPLOYEE_NOT_FOUND);
        ResponseEntity<?> result = payrollController.adjustDay(1, updates);
        assertEquals(Map.of("response", "Employee Not Found in Company"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testAdjustDayNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.adjustPaymentDayByEmployeeId(1, updates)).thenReturn(PayrollStatus.NOT_FOUND);
        ResponseEntity<?> result = payrollController.adjustDay(1, updates);
        assertEquals(Map.of("response", "Payroll for this month and year Not Found"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testAdjustDaySuccess() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.adjustPaymentDayByEmployeeId(1, updates)).thenReturn(PayrollStatus.SUCCESS);
        ResponseEntity<?> result = payrollController.adjustDay(1, updates);
        assertEquals(Map.of("response", "Attribute was updated successfully"), result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testAdjustDayDefault() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.adjustPaymentDayByEmployeeId(1, updates)).thenReturn(PayrollStatus.OK);
        ResponseEntity<?> result = payrollController.adjustDay(1, updates);
        assertEquals(Map.of("response", "An Error has occurred"), result.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void testAddPayrollByEmployeeIdInvalidData() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.addPayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.INVALID_DATA);
        ResponseEntity<?> result = payrollController.createPayroll(1, updates);
        assertEquals(Map.of("response", "Invalid month or year"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testAddPayrollByEmployeeIdInvalidFormat() {
        Map<String, Object> updates = Map.of("month", "10", "year", "2024");
        when(payrollService.addPayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.INVALID_FORMAT);
        ResponseEntity<?> result = payrollController.createPayroll(1, updates);
        assertEquals(Map.of("response", "Invalid format for month or year"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testAddPayrollByEmployeeIdEmployeeNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.addPayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.EMPLOYEE_NOT_FOUND);
        ResponseEntity<?> result = payrollController.createPayroll(1, updates);
        assertEquals(Map.of("response", "Employee Not Found in Company"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testAddPayrollByEmployeeIdAlreadyExists() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.addPayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.ALREADY_EXISTS);
        ResponseEntity<?> result = payrollController.createPayroll(1, updates);
        assertEquals(Map.of("response", "Payroll for this month and year already exists"), result.getBody());
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    void testAddPayrollByEmployeeIdSuccess() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.addPayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.SUCCESS);
        ResponseEntity<?> result = payrollController.createPayroll(1, updates);
        assertEquals(Map.of("response", "Attribute was created successfully"), result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testAddPayrollByEmployeeIdDefault() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.addPayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.OK);
        ResponseEntity<?> result = payrollController.createPayroll(1, updates);
        assertEquals(Map.of("response", "An Error has occurred"), result.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void testDeletePayrollByEmployeeIdInvalidData() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.deletePayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.INVALID_DATA);
        ResponseEntity<?> result = payrollController.deletePayrollByEmployeeId(1, updates);
        assertEquals(Map.of("response", "Invalid month or year"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testDeletePayrollByEmployeeIdInvalidFormat() {
        Map<String, Object> updates = Map.of("month", "10", "year", "2024");
        when(payrollService.deletePayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.INVALID_FORMAT);
        ResponseEntity<?> result = payrollController.deletePayrollByEmployeeId(1, updates);
        assertEquals(Map.of("response", "Invalid format for month or year"), result.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testDeletePayrollByEmployeeIdEmployeeNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.deletePayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.EMPLOYEE_NOT_FOUND);
        ResponseEntity<?> result = payrollController.deletePayrollByEmployeeId(1, updates);
        assertEquals(Map.of("response", "Employee Not Found in Company"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testDeletePayrollByEmployeeIdNotFound() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.deletePayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.NOT_FOUND);
        ResponseEntity<?> result = payrollController.deletePayrollByEmployeeId(1, updates);
        assertEquals(Map.of("response", "Payroll for this month and year Not Found"), result.getBody());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testDeletePayrollByEmployeeIdSuccess() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.deletePayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.SUCCESS);
        ResponseEntity<?> result = payrollController.deletePayrollByEmployeeId(1, updates);
        assertEquals(Map.of("response", "Attribute was deleted successfully"), result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testDeletePayrollByEmployeeIdDefault() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.deletePayrollByEmployeeId(1, updates)).thenReturn(PayrollStatus.OK);
        ResponseEntity<?> result = payrollController.deletePayrollByEmployeeId(1, updates);
        assertEquals(Map.of("response", "An Error has occurred"), result.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void testGeneratePayroll() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.generatePayroll(updates)).thenReturn(updates);
        ResponseEntity<?> result = payrollController.generatePayroll(updates);
        assertEquals(updates, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testDeletePayroll() {
        Map<String, Object> updates = Map.of("month", 10, "year", 2024);
        when(payrollService.deletePayroll(updates)).thenReturn(updates);
        ResponseEntity<?> result = payrollController.deletePayroll(updates);
        assertEquals(updates, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

}
