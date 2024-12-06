package com.nullterminators.project.integration.external;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.EmployeeProfileRepository;
import com.nullterminators.project.repository.PayrollRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** External integration tests for Payroll module. */
@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class PayrollExternalIntegrationTests {

  @Autowired
  private PayrollRepository payrollRepository;

  @Autowired
  private EmployeeProfileRepository employeeProfileRepository;

  @BeforeEach
  public void setUp() {}

  private void createPayroll(Integer employeeId) {
    Payroll payroll = new Payroll();
    payroll.setEmployeeId(employeeId);
    payroll.setPaymentDate(LocalDate.of(2024, 10, 17));
    payroll.setSalary(10000);
    payroll.setTax(3000);
    payroll.setPayslip("N/A");
    payroll.setPaid(1);
    payrollRepository.save(payroll);
  }

  private Integer createEmployeeProfile() {
    EmployeeProfile employeeProfile = new EmployeeProfile();
    employeeProfile.setName("testUser");
    employeeProfile.setPhoneNumber("9999999999");
    employeeProfile.setGender("male");
    employeeProfile.setAge(20);
    employeeProfile.setStartDate(LocalDate.now());
    employeeProfile.setDesignation("tester");
    employeeProfile.setEmail("testUser@nullterminators");
    employeeProfile.setBaseSalary(100000);
    employeeProfile.setEmergencyContactNumber("9999999999");
    employeeProfileRepository.save(employeeProfile);

    return employeeProfile.getId();
  }

  @Test
  public void testFindAllByEmployeeIdOrderByPaymentDateDesc() {
    Integer empId = createEmployeeProfile();
    createPayroll(empId);
    List<Payroll> payrollList = payrollRepository.findAllByEmployeeIdOrderByPaymentDateDesc(empId);

    for (Payroll payroll : payrollList) {
      assertEquals(empId, payroll.getEmployeeId());
      assertEquals(LocalDate.of(2024, 10, 17), payroll.getPaymentDate());
      assertEquals(10000, payroll.getSalary());
      assertEquals(3000, payroll.getTax());
      assertEquals("N/A", payroll.getPayslip());
      assertEquals(1, payroll.getPaid());
    }
  }

  @Test
  public void testFindByEmployeeIdPaymentMonthAndYear() {
    Integer empId = createEmployeeProfile();
    createPayroll(empId);
    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(empId, 10, 2024);
    assertEquals(empId, payroll.getEmployeeId());
    assertEquals(LocalDate.of(2024, 10, 17), payroll.getPaymentDate());
    assertEquals(10000, payroll.getSalary());
    assertEquals(3000, payroll.getTax());
    assertEquals("N/A", payroll.getPayslip());
    assertEquals(1, payroll.getPaid());
  }

  @Test
  public void testSave() {
    Integer empId = createEmployeeProfile();
    Payroll payroll = new Payroll();
    payroll.setEmployeeId(empId);
    payroll.setPaymentDate(LocalDate.of(2024, 10, 17));
    payroll.setSalary(10000);
    payroll.setTax(3000);
    payroll.setPayslip("N/A");
    payroll.setPaid(1);
    payrollRepository.save(payroll);
    Payroll savedPayroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(empId, 10, 2024);
    assertEquals(empId, savedPayroll.getEmployeeId());
    assertEquals(LocalDate.of(2024, 10, 17), savedPayroll.getPaymentDate());
    assertEquals(10000, savedPayroll.getSalary());
    assertEquals(3000, savedPayroll.getTax());
    assertEquals("N/A", savedPayroll.getPayslip());
    assertEquals(1, savedPayroll.getPaid());
  }

  @Test
  public void testDelete() {
    Integer empId = createEmployeeProfile();
    Payroll payroll = new Payroll();
    payroll.setEmployeeId(empId);
    payroll.setPaymentDate(LocalDate.of(2024, 10, 17));
    payroll.setSalary(10000);
    payroll.setTax(3000);
    payroll.setPayslip("N/A");
    payroll.setPaid(1);
    payrollRepository.save(payroll);
    payrollRepository.delete(payroll);
    Payroll deletedPayroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(empId, 10, 2024);
    assertEquals(null, deletedPayroll);
  }

}
