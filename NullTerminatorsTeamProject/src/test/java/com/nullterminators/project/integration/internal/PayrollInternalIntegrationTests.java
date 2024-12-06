package com.nullterminators.project.integration.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nullterminators.project.controller.PayrollController;
import com.nullterminators.project.model.CompanyEmployees;
import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.CompanyEmployeesRepository;
import com.nullterminators.project.repository.EmployeeProfileRepository;
import com.nullterminators.project.repository.PayrollRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/** Internal integration tests for payroll module. */
@SpringBootTest
public class PayrollInternalIntegrationTests {

  @MockBean
  private PayrollRepository payrollRepository;

  @MockBean
  private EmployeeProfileRepository employeeProfileRepository;

  @MockBean
  private CompanyEmployeesRepository companyEmployeesRepository;

  @Autowired
  private PayrollController payrollController;

  /**
   * Setting up payroll, company and employee objects and mocking external database calls.
   */
  @BeforeEach
  public void setUp() {

    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("testCompany", null));

    CompanyEmployees mockEmployee = new CompanyEmployees();
    mockEmployee.setId(1);
    mockEmployee.setCompanyUsername("testCompany");
    mockEmployee.setEmployeeId(1);

    EmployeeProfile mockProfile = new EmployeeProfile();
    mockProfile.setId(1);
    mockProfile.setName("Employee One");
    mockProfile.setAge(50);
    mockProfile.setEmail("employeeOne@email.com");
    mockProfile.setDesignation("Manager");
    mockProfile.setPhoneNumber("+1-123-456-7904");
    mockProfile.setGender("Female");
    mockProfile.setStartDate(LocalDate.now());
    mockProfile.setEmergencyContactNumber("9876543210");
    mockProfile.setBaseSalary(233445);

    Payroll payroll = new Payroll();
    payroll.setEmployeeId(1);
    payroll.setPaymentDate(LocalDate.of(2024, 10, 17));
    payroll.setSalary(10000);
    payroll.setTax(3000);
    payroll.setPaid(0);
    payroll.setPayslip("N/A");

    when(companyEmployeesRepository.findAllByCompanyUsernameAndEmployeeId("testCompany", 1))
        .thenReturn(List.of(mockEmployee));
    when(companyEmployeesRepository.findAllByCompanyUsername("testCompany"))
        .thenReturn(List.of(mockEmployee));
    when(employeeProfileRepository.findById(1)).thenReturn(Optional.of(mockProfile));
    when(payrollRepository.findAllByEmployeeIdOrderByPaymentDateDesc(1)).thenReturn(
        List.of(payroll));
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2024))
        .thenReturn(payroll);
  }


  @Test
  void testGetPayrollByEmployeeIdSuccess() {
    Map<String, Object> payrollInfo = Map.of("employeeId", 1, "date",
        LocalDate.of(2024, 10, 17),
        "salary", 10000, "tax", 3000,
        "payslip", "N/A", "netSalary", 7000);

    ResponseEntity<?> result = payrollController.getPayrollByEmployeeId(1);
    assertEquals(List.of(payrollInfo), result.getBody());
    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  void testMarkAsPaidSuccess() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    ResponseEntity<?> result = payrollController.markAsPaid(1, updates);
    assertEquals(Map.of("response", "Attribute was updated successfully"), result.getBody());
    assertEquals(HttpStatus.OK, result.getStatusCode());
    verify(payrollRepository).save(any(Payroll.class));
  }

  @Test
  void testMarkAsUnpaidAlreadyCompleted() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    ResponseEntity<?> result = payrollController.markAsUnpaid(1, updates);
    assertEquals(Map.of("response", "Employee has already been marked as Not Paid"),
        result.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  void testAdjustSalarySuccess() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024, "salary", 12000);
    ResponseEntity<?> result = payrollController.adjustSalary(1, updates);
    assertEquals(Map.of("response", "Attribute was updated successfully"), result.getBody());
    assertEquals(HttpStatus.OK, result.getStatusCode());
    verify(payrollRepository).save(any(Payroll.class));
  }

  @Test
  void testAdjustDaySuccess() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024, "day", 12);
    ResponseEntity<?> result = payrollController.adjustDay(1, updates);
    assertEquals(Map.of("response", "Attribute was updated successfully"), result.getBody());
    assertEquals(HttpStatus.OK, result.getStatusCode());
    verify(payrollRepository).save(any(Payroll.class));
  }

  @Test
  void testDeletePayrollByEmployeeIdSuccess() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    ResponseEntity<?> result = payrollController.deletePayrollByEmployeeId(1, updates);
    assertEquals(Map.of("response", "Attribute was deleted successfully"), result.getBody());
    assertEquals(HttpStatus.OK, result.getStatusCode());
    verify(payrollRepository).delete(any(Payroll.class));
  }

  @Test
  void testAddPayrollByEmployeeIdInvalidFormat() {
    Map<String, Object> updates = Map.of("month", "10", "year", "2024");
    ResponseEntity<?> result = payrollController.createPayroll(1, updates);
    assertEquals(Map.of("response", "Invalid month or year"), result.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  void testDeletePayroll() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    Map<String, Object> answer = Map.of("response",
            "Payroll for this month and year has been deleted");
    ResponseEntity<?> result = payrollController.deletePayroll(updates);
    assertEquals(answer, result.getBody());
    assertEquals(HttpStatus.OK, result.getStatusCode());
    verify(payrollRepository).delete(any(Payroll.class));
  }
}
