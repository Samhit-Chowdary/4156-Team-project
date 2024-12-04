package com.nullterminators.project.integration.external;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nullterminators.project.model.Company;
import com.nullterminators.project.model.CompanyEmployees;
import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.repository.CompanyEmployeesRepository;
import com.nullterminators.project.repository.CompanyRepository;
import com.nullterminators.project.repository.EmployeeProfileRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** External integration tests for company employees module. */
@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class CompanyEmployeesExternalIntegrationTests {
  @Autowired private CompanyRepository companyRepository;

  @Autowired private EmployeeProfileRepository employeeProfileRepository;

  @Autowired private CompanyEmployeesRepository companyEmployeesRepository;

  @BeforeEach
  public void setUp() {}

  private Integer createCompany(String username) {
    Company company = new Company();
    company.setUsername(username);
    company.setPassword("testing");
    company.setAddress("test");
    company.setState("NY");
    company.setName("IT");
    companyRepository.save(company);

    return company.getId();
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

  private Integer createCompanyEmployees(String username, Integer employeeId) {
    CompanyEmployees companyEmployees = new CompanyEmployees();
    companyEmployees.setCompanyUsername(username);
    companyEmployees.setEmployeeId(employeeId);
    companyEmployeesRepository.save(companyEmployees);

    return companyEmployees.getId();
  }

  @Test
  public void testFindAllByCompanyUsernameAndEmployeeId() {
    Integer comId = createCompany("testCompany");
    Integer empId = createEmployeeProfile();
    Integer temp = createCompanyEmployees("testCompany", empId);
    List<CompanyEmployees> result =
        companyEmployeesRepository.findAllByCompanyUsernameAndEmployeeId("testCompany", empId);
    assertEquals(1, result.size());
  }

  @Test
  public void testFindAllByEmployeeId() {
    Integer comId = createCompany("testCompany");
    Integer empId = createEmployeeProfile();
    Integer temp = createCompanyEmployees("testCompany", empId);
    List<CompanyEmployees> result = companyEmployeesRepository.findAllByEmployeeId(empId);
    assertEquals(1, result.size());
  }

  @Test
  public void testFindAllByCompanyUsername() {
    Integer comId = createCompany("testCompany");
    Integer empId = createEmployeeProfile();
    Integer temp = createCompanyEmployees("testCompany", empId);
    List<CompanyEmployees> result =
        companyEmployeesRepository.findAllByCompanyUsername("testCompany");
    assertEquals(1, result.size());
  }
}
