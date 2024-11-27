package com.nullterminators.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nullterminators.project.enums.PayrollStatus;
import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.PayrollRepository;
import com.nullterminators.project.util.pdf.PdfGenerator;
import com.nullterminators.project.util.pdf.PdfUploader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ContextConfiguration;

/**
 * Service Tests for Payroll.
 */
@SpringBootTest
@ContextConfiguration
public class PayrollServiceTests {

  @Mock
  private CompanyEmployeesService companyEmployeesService;

  @Mock
  private PayrollRepository payrollRepository;

  @Mock
  private EmployeeProfileService employeeProfileService;

  @Mock
  private PdfGenerator pdfGenerator;

  @Mock
  private PdfUploader pdfUploader;

  private PayrollService payrollService;

  Payroll payroll;

  @BeforeEach
  void setUp() {
    payrollService = new PayrollService(payrollRepository,
            employeeProfileService, companyEmployeesService, pdfGenerator, pdfUploader);
    payroll = new Payroll();
    payroll.setEmployeeId(1);
    payroll.setPaymentDate(LocalDate.of(2024, 10, 17));
    payroll.setSalary(10000);
    payroll.setTax(3000);
    payroll.setPayslip("N/A");
  }

  @Test
  void testGetPayrollByEmployeeIdEmployeeNotFound() {
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(false);
    assertEquals(Pair.of(PayrollStatus.EMPLOYEE_NOT_FOUND, new ArrayList<>()),
            payrollService.getPayrollByEmployeeId(1));
  }

  @Test
  void testGetPayrollByEmployeeIdSuccess() {
    Map<String, Object> payrollInfo = Map.of("employeeId", 1, "date",
            LocalDate.of(2024, 10, 17),
            "salary", 10000, "tax", 3000,
            "payslip", "N/A", "netSalary", 7000);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findAllByEmployeeIdOrderByPaymentDateDesc(1))
            .thenReturn(List.of(payroll));
    assertEquals(Pair.of(PayrollStatus.OK, List.of(payrollInfo)),
            payrollService.getPayrollByEmployeeId(1));
  }

  @Test
  void testMarkAsPaidEmployeeNotFound() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(false);
    assertEquals(PayrollStatus.EMPLOYEE_NOT_FOUND, payrollService.markAsPaid(1, updates));
  }

  @Test
  void testMarkAsPaidNotFound() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2022))
        .thenReturn(new Payroll());
    assertEquals(PayrollStatus.NOT_FOUND, payrollService.markAsPaid(1, updates));
  }

  @Test
  void testMarkAsPaidInvalidData() {
    Map<String, Object> updates = new HashMap<>();
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    assertEquals(PayrollStatus.INVALID_DATA, payrollService.markAsPaid(1, updates));
  }

  @Test
  void testMarkAsPaidInvalidFormat() {
    Map<String, Object> updates = Map.of("month", "10", "year", "2024");
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    assertEquals(PayrollStatus.INVALID_FORMAT, payrollService.markAsPaid(1, updates));
  }

  @Test
  void testMarkAsPaidAlreadyPaid() {
    final Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    payroll.setPaid(1);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2024))
        .thenReturn(payroll);
    assertEquals(PayrollStatus.ALREADY_COMPLETED, payrollService.markAsPaid(1, updates));
  }

  @Test
  void testMarkAsPaidSuccess() {
    final Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    payroll.setPaid(0);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2024))
        .thenReturn(payroll);
    assertEquals(PayrollStatus.SUCCESS, payrollService.markAsPaid(1, updates));
  }

  @Test
  void testMarkAsUnpaidEmployeeNotFound() {
    final Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(false);
    assertEquals(PayrollStatus.EMPLOYEE_NOT_FOUND, payrollService.markAsUnpaid(1, updates));
  }

  @Test
  void testMarkAsUnpaidInvalidData() {
    final Map<String, Object> updates = new HashMap<>();
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    assertEquals(PayrollStatus.INVALID_DATA, payrollService.markAsUnpaid(1, updates));
  }

  @Test
  void testMarkAsUnpaidInvalidFormat() {
    Map<String, Object> updates = Map.of("month", 13, "year", 2024);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    assertEquals(PayrollStatus.INVALID_FORMAT, payrollService.markAsUnpaid(1, updates));
  }

  @Test
  void testMarkAsUnpaidNotFound() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2022))
        .thenReturn(new Payroll());
    assertEquals(PayrollStatus.NOT_FOUND, payrollService.markAsUnpaid(1, updates));
  }

  @Test
  void testMarkAsUnpaidAlreadyCompleted() {
    final Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    payroll.setPaid(0);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2024))
        .thenReturn(payroll);
    assertEquals(PayrollStatus.ALREADY_COMPLETED, payrollService.markAsUnpaid(1, updates));
  }

  @Test
  void testMarkAsUnpaidSuccess() {
    final Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    payroll.setPaid(1);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2024))
        .thenReturn(payroll);
    assertEquals(PayrollStatus.SUCCESS, payrollService.markAsUnpaid(1, updates));
  }

  @Test
  void testAdjustSalaryByEmployeeIdEmployeeNotFound() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(false);
    assertEquals(PayrollStatus.EMPLOYEE_NOT_FOUND,
            payrollService.adjustSalaryByEmployeeId(1, updates));
  }

  @Test
  void testAdjustSalaryByEmployeeIdInvalidData() {
    Map<String, Object> updates = new HashMap<>();
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    assertEquals(PayrollStatus.INVALID_DATA, payrollService.adjustSalaryByEmployeeId(1, updates));
  }

  @Test
  void testAdjustSalaryByEmployeeIdInvalidFormat() {
    Map<String, Object> updates = Map.of("month", 13, "year", 2024, "salary", 10000);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    assertEquals(PayrollStatus.INVALID_FORMAT, payrollService.adjustSalaryByEmployeeId(1, updates));
  }

  @Test
  void testAdjustSalaryByEmployeeIdNotFound() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024, "salary", 10000);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2022))
        .thenReturn(new Payroll());
    assertEquals(PayrollStatus.NOT_FOUND, payrollService.adjustSalaryByEmployeeId(1, updates));
  }

  @Test
  void testAdjustSalaryByEmployeeIdSuccess() {
    Map<String, Object> updates = Map.of("month", 10, "year", 2024, "salary", 10000);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2024))
        .thenReturn(payroll);
    assertEquals(PayrollStatus.SUCCESS, payrollService.adjustSalaryByEmployeeId(1, updates));
  }

  @Test
  void testAdjustPaymentDayByEmployeeIdEmployeeNotFound() {
    Map<String, Object> updates = Map.of("day", 10, "month", 10, "year", 2024);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(false);
    assertEquals(PayrollStatus.EMPLOYEE_NOT_FOUND,
            payrollService.adjustPaymentDayByEmployeeId(1, updates));
  }

  @Test
  void testAdjustPaymentDayByEmployeeIdInvalidData() {
    Map<String, Object> updates = new HashMap<>();
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    assertEquals(PayrollStatus.INVALID_DATA,
            payrollService.adjustPaymentDayByEmployeeId(1, updates));
  }

  @Test
  void testAdjustPaymentDayByEmployeeIdInvalidFormat() {
    Map<String, Object> updates = Map.of("day", 32, "month", 10, "year", 2024);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    assertEquals(PayrollStatus.INVALID_FORMAT,
            payrollService.adjustPaymentDayByEmployeeId(1, updates));
  }

  @Test
  void testAdjustPaymentDayByEmployeeIdNotFound() {
    Map<String, Object> updates = Map.of("day", 10, "month", 10, "year", 2024);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2022))
        .thenReturn(new Payroll());
    assertEquals(PayrollStatus.NOT_FOUND, payrollService.adjustPaymentDayByEmployeeId(1, updates));
  }

  @Test
  void testAdjustPaymentDayByEmployeeIdSuccess() {
    Map<String, Object> updates = Map.of("day", 10, "month", 10, "year", 2024);
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2024))
        .thenReturn(payroll);
    assertEquals(PayrollStatus.SUCCESS, payrollService.adjustPaymentDayByEmployeeId(1, updates));
  }

  @Test
  void testAddPayrollByEmployeeIdInvalidData() {
    Map<String, Object> updates = new HashMap<>();
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    assertEquals(PayrollStatus.INVALID_DATA, payrollService.addPayrollByEmployeeId(1, updates));
  }

  @Test
  void testAddPayrollByEmployeeIdEmployeeNotFound() {
    Map<String, Object> updates = new HashMap<>();
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(false);
    assertEquals(PayrollStatus.EMPLOYEE_NOT_FOUND,
            payrollService.addPayrollByEmployeeId(1, updates));
  }

  @Test
  void testDeletePayrollByEmployeeIdEmployeeNotFound() {
    Map<String, Object> updates = new HashMap<>();
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(false);
    assertEquals(PayrollStatus.EMPLOYEE_NOT_FOUND,
            payrollService.deletePayrollByEmployeeId(1, updates));
  }

  @Test
  void testDeletePayrollByEmployeeIdSuccess() {
    Map<String, Object> updates = new HashMap<>(Map.of("day", 10, "month", 10, "year", 2024));
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2024))
            .thenReturn(new Payroll());
    assertEquals(PayrollStatus.SUCCESS, payrollService.deletePayrollByEmployeeId(1, updates));
  }

  @Test
  void testGeneratePayrollInvalidData() {
    final Map<String, Object> updates = new HashMap<>();
    EmployeeProfile employeeProfile1 = new EmployeeProfile();
    employeeProfile1.setId(100);
    EmployeeProfile employeeProfile2 = new EmployeeProfile();
    employeeProfile2.setId(200);
    when(companyEmployeesService.getAllEmployeesInCompany()).thenReturn(Arrays.asList(100, 200));
    when(employeeProfileService.getEmployeeProfile(100)).thenReturn(Optional.of(employeeProfile1));
    when(companyEmployeesService.verifyIfEmployeeInCompany(100)).thenReturn(true);
    when(employeeProfileService.getEmployeeProfile(200)).thenReturn(Optional.of(employeeProfile2));
    when(companyEmployeesService.verifyIfEmployeeInCompany(200)).thenReturn(true);
    assertEquals(Map.of("response", "Invalid day or month or year"),
            payrollService.generatePayroll(updates));
  }

  @Test
  void testGeneratePayrollInvalidFormat() {
    final Map<String, Object> updates = new HashMap<>(Map.of("day", 32, "month", 10, "year", 2024));
    EmployeeProfile employeeProfile1 = new EmployeeProfile();
    employeeProfile1.setId(100);
    employeeProfile1.setBaseSalary(10000);
    EmployeeProfile employeeProfile2 = new EmployeeProfile();
    employeeProfile2.setId(200);
    employeeProfile1.setBaseSalary(10000);
    when(companyEmployeesService.getAllEmployeesInCompany()).thenReturn(Arrays.asList(100, 200));
    when(employeeProfileService.getEmployeeProfile(100)).thenReturn(Optional.of(employeeProfile1));
    when(companyEmployeesService.verifyIfEmployeeInCompany(100)).thenReturn(true);
    when(employeeProfileService.getEmployeeProfile(200)).thenReturn(Optional.of(employeeProfile2));
    when(companyEmployeesService.verifyIfEmployeeInCompany(200)).thenReturn(true);
    assertEquals(Map.of("response", "Invalid format for day or month or year"),
            payrollService.generatePayroll(updates));
  }

  @Test
  void testGeneratePayrollAlreadyExists() {
    final Map<String, Object> updates = new HashMap<>(Map.of("day", 10, "month", 10, "year", 2024));
    Map<String, Object> result = new HashMap<>();
    result.put("response", "Payroll for the employees in list have already "
            + "been generated and were not added");
    result.put("employeeList", Arrays.asList(100, 200));
    EmployeeProfile employeeProfile1 = new EmployeeProfile();
    employeeProfile1.setId(100);
    employeeProfile1.setBaseSalary(10000);
    EmployeeProfile employeeProfile2 = new EmployeeProfile();
    employeeProfile2.setId(200);
    employeeProfile2.setBaseSalary(10000);
    when(companyEmployeesService.getAllEmployeesInCompany()).thenReturn(Arrays.asList(100, 200));
    when(employeeProfileService.getEmployeeProfile(100)).thenReturn(Optional.of(employeeProfile1));
    when(companyEmployeesService.verifyIfEmployeeInCompany(100)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(100, 10, 2024))
        .thenReturn(new Payroll());
    when(employeeProfileService.getEmployeeProfile(200)).thenReturn(Optional.of(employeeProfile2));
    when(companyEmployeesService.verifyIfEmployeeInCompany(200)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(200, 10, 2024))
        .thenReturn(new Payroll());
    assertEquals(result, payrollService.generatePayroll(updates));
  }

  @Test
  void testGeneratePayrollSuccess() {
    final Map<String, Object> updates = new HashMap<>(Map.of("day", 17, "month", 10, "year", 2024));
    EmployeeProfile employeeProfile = new EmployeeProfile();
    employeeProfile.setId(1);
    employeeProfile.setBaseSalary(10000);
    when(companyEmployeesService.getAllEmployeesInCompany()).thenReturn(List.of(1));
    when(employeeProfileService.getEmployeeProfile(1)).thenReturn(Optional.of(employeeProfile));
    when(companyEmployeesService.verifyIfEmployeeInCompany(1)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(1, 10, 2024))
        .thenReturn(null);
    when(pdfGenerator.getPdfName(any(Payroll.class))).thenReturn("pdfName");
    when(pdfUploader.uploadPdf(any(String.class))).thenReturn("url");
    assertEquals(Map.of("response", "Payroll for this month and year has been generated"),
            payrollService.generatePayroll(updates));
    verify(pdfGenerator).generatePdfReport(any(Payroll.class));
    verify(payrollRepository).save(any(Payroll.class));
  }

  @Test
  void testDeletePayrollInvalidData() {
    final Map<String, Object> updates = new HashMap<>();
    EmployeeProfile employeeProfile1 = new EmployeeProfile();
    employeeProfile1.setId(100);
    EmployeeProfile employeeProfile2 = new EmployeeProfile();
    employeeProfile2.setId(200);
    when(companyEmployeesService.getAllEmployeesInCompany()).thenReturn(Arrays.asList(100, 200));
    when(employeeProfileService.getEmployeeProfile(100)).thenReturn(Optional.of(employeeProfile1));
    when(companyEmployeesService.verifyIfEmployeeInCompany(100)).thenReturn(true);
    when(employeeProfileService.getEmployeeProfile(200)).thenReturn(Optional.of(employeeProfile2));
    when(companyEmployeesService.verifyIfEmployeeInCompany(200)).thenReturn(true);
    assertEquals(Map.of("response", "Invalid month or year"),
            payrollService.deletePayroll(updates));
  }

  @Test
  void testDeletePayrollInvalidFormat() {
    final Map<String, Object> updates = new HashMap<>(Map.of("day", 32, "month", 13, "year", 2024));
    EmployeeProfile employeeProfile1 = new EmployeeProfile();
    employeeProfile1.setId(100);
    EmployeeProfile employeeProfile2 = new EmployeeProfile();
    employeeProfile2.setId(200);
    when(companyEmployeesService.getAllEmployeesInCompany()).thenReturn(Arrays.asList(100, 200));
    when(employeeProfileService.getEmployeeProfile(100)).thenReturn(Optional.of(employeeProfile1));
    when(companyEmployeesService.verifyIfEmployeeInCompany(100)).thenReturn(true);
    when(employeeProfileService.getEmployeeProfile(200)).thenReturn(Optional.of(employeeProfile2));
    when(companyEmployeesService.verifyIfEmployeeInCompany(200)).thenReturn(true);
    assertEquals(Map.of("response", "Invalid format for month or year"),
            payrollService.deletePayroll(updates));
  }

  @Test
  void testDeletePayrollSuccess() {
    final Map<String, Object> updates = new HashMap<>(Map.of("day", 10, "month", 10, "year", 2024));
    EmployeeProfile employeeProfile1 = new EmployeeProfile();
    employeeProfile1.setId(100);
    EmployeeProfile employeeProfile2 = new EmployeeProfile();
    employeeProfile2.setId(200);
    when(companyEmployeesService.getAllEmployeesInCompany()).thenReturn(Arrays.asList(100, 200));
    when(employeeProfileService.getEmployeeProfile(100)).thenReturn(Optional.of(employeeProfile1));
    when(companyEmployeesService.verifyIfEmployeeInCompany(100)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(100, 10, 2024))
        .thenReturn(null);
    when(employeeProfileService.getEmployeeProfile(200)).thenReturn(Optional.of(employeeProfile2));
    when(companyEmployeesService.verifyIfEmployeeInCompany(200)).thenReturn(true);
    when(payrollRepository.findByEmployeeIdPaymentMonthAndYear(200, 10, 2024))
        .thenReturn(null);
    assertEquals(Map.of("response", "Payroll for this month and year has been deleted"),
            payrollService.deletePayroll(updates));
  }
}
