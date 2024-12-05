package com.nullterminators.project.integration.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.nullterminators.project.controller.EmployeeProfileController;
import com.nullterminators.project.model.CompanyEmployees;
import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.repository.CompanyEmployeesRepository;
import com.nullterminators.project.repository.EmployeeProfileRepository;
import com.nullterminators.project.service.CompanyEmployeesService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nullterminators.project.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * External Integration tests for employeee profile API endpoints.
 */
@SpringBootTest
public class EmployeeProfileManagementInternalIntegrationTests {

  @Autowired
  private EmployeeProfileController employeeProfileController;

  @MockBean
  private EmployeeProfileRepository employeeProfileRepository;

  @MockBean
  private CompanyEmployeesRepository companyEmployeesRepository;

  @Autowired private CompanyService companyService;

  private int empOneId;

  @BeforeEach
  void setUp() { 
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
    when(companyEmployeesRepository.findAllByCompanyUsername("testCompany")).thenReturn(List.of(mockEmployee));
    when(employeeProfileRepository.findAll()).thenReturn(List.of(mockProfile));
    when(employeeProfileRepository.findById(1)).thenReturn(Optional.of(mockProfile));
    when(companyEmployeesRepository.findAllByCompanyUsernameAndEmployeeId("testCompany", 1)).thenReturn(List.of(mockEmployee));
  }

  @Test
  public void getAllEmployeesSuccessTest() {
    ResponseEntity<?> response = employeeProfileController.getAllEmployees();
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void getEmployeesSuccessTest() throws Exception {
    ResponseEntity<?> response = employeeProfileController.getEmployee(1);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
//
//  @Test
//  public void updateEmployeeNameSuccessTest() throws Exception {
//    String newName = "new name";
//    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
//        .thenReturn(true);
//    ResponseEntity<?> response = employeeProfileController.updateEmployeeName(
//        empOneId, newName);
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//    employeeProfileController.deleteEmployee(empOneId);
//  }
//
//  @Test
//  public void updateEmployeeEmailIdSuccessTest() throws Exception {
//    String newEmail = "newemail@gmail.com";
//    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
//        .thenReturn(true);
//    ResponseEntity<?> response = employeeProfileController.updateEmployeeEmailId(
//        empOneId, newEmail);
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//    employeeProfileController.deleteEmployee(empOneId);
//  }
//
//  @Test
//  public void updateEmployeeDesignationSuccessTest() throws Exception {
//    String newDesignation = "Manager";
//    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
//        .thenReturn(true);
//    ResponseEntity<?> response = employeeProfileController.updateEmployeeDesignation(
//        empOneId, newDesignation);
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//    employeeProfileController.deleteEmployee(empOneId);
//  }
//
//  @Test
//  public void updateEmployeePhoneNumSuccessTest() throws Exception {
//    String newPhoneNum = "00000000000";
//    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
//        .thenReturn(true);
//    ResponseEntity<?> response = employeeProfileController.updateEmployeePhoneNumber(
//        empOneId, newPhoneNum);
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//    employeeProfileController.deleteEmployee(empOneId);
//  }
//
//  @Test
//  public void updateEmployeeBaseSalarySuccessTest() throws Exception {
//    int newBase = 100000;
//    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
//        .thenReturn(true);
//    ResponseEntity<?> response = employeeProfileController.updateBaseSalary(
//        empOneId, newBase);
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//    employeeProfileController.deleteEmployee(empOneId);
//  }
//
//  @Test
//  public void updateEmployeeEmergencySuccessTest() throws Exception {
//    String newEmergency = "00000000000";
//    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
//        .thenReturn(true);
//    ResponseEntity<?> response = employeeProfileController.updateEmergencyContact(
//        empOneId, newEmergency);
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//    employeeProfileController.deleteEmployee(empOneId);
//  }

}
