package com.nullterminators.project.integration.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.nullterminators.project.controller.EmployeeProfileController;
import com.nullterminators.project.service.CompanyEmployeesService;
import java.time.LocalDate;
import java.util.List;
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
@AutoConfigureMockMvc
public class EmployeeProfileManagementInternalIntegrationTests {

  @Autowired
  private EmployeeProfileController employeeProfileController;
  
  @MockBean
  private CompanyEmployeesService companyEmployeesService;

  private int empOneId;

  @BeforeEach
  void setUp() { 
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("testCompany", null));

    ResponseEntity<?> resp = employeeProfileController.createNewEmployee("Employee One",
        "+1-123-456-7789", "Female", 50, LocalDate.now(),
        "Manager", "employeeOne@email.com", "9876543210",
        233445);
   
    Object body = resp.getBody();
    if (body instanceof Integer) {
      empOneId = (Integer) body;
    }
  }

  @Test
  public void getAllEmployeesSuccessTest() throws Exception {
    List<Integer> mockEmployees = List.of(empOneId);

    Mockito.when(companyEmployeesService.getAllEmployeesInCompany()).thenReturn(mockEmployees);
    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
        .thenReturn(true);
    ResponseEntity<?> response = employeeProfileController.getAllEmployees();
    assertEquals(HttpStatus.OK, response.getStatusCode());
    employeeProfileController.deleteEmployee(empOneId);
  }

  @Test
  public void getEmployeesSuccessTest() throws Exception {
    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
        .thenReturn(true);
    ResponseEntity<?> response = employeeProfileController.getEmployee(empOneId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    employeeProfileController.deleteEmployee(empOneId);
  }

  @Test
  public void updateEmployeeNameSuccessTest() throws Exception {
    String newName = "new name";
    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
        .thenReturn(true);
    ResponseEntity<?> response = employeeProfileController.updateEmployeeName(
        empOneId, newName);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    employeeProfileController.deleteEmployee(empOneId);
  }

  @Test
  public void updateEmployeeEmailIdSuccessTest() throws Exception {
    String newEmail = "newemail@gmail.com";
    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
        .thenReturn(true);
    ResponseEntity<?> response = employeeProfileController.updateEmployeeEmailId(
        empOneId, newEmail);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    employeeProfileController.deleteEmployee(empOneId);
  }

  @Test
  public void updateEmployeeDesignationSuccessTest() throws Exception {
    String newDesignation = "Manager";
    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
        .thenReturn(true);
    ResponseEntity<?> response = employeeProfileController.updateEmployeeDesignation(
        empOneId, newDesignation);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    employeeProfileController.deleteEmployee(empOneId);
  }

  @Test
  public void updateEmployeePhoneNumSuccessTest() throws Exception {
    String newPhoneNum = "00000000000";
    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
        .thenReturn(true);
    ResponseEntity<?> response = employeeProfileController.updateEmployeePhoneNumber(
        empOneId, newPhoneNum);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    employeeProfileController.deleteEmployee(empOneId);
  }

  @Test
  public void updateEmployeeBaseSalarySuccessTest() throws Exception {
    int newBase = 100000;
    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
        .thenReturn(true);
    ResponseEntity<?> response = employeeProfileController.updateBaseSalary(
        empOneId, newBase);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    employeeProfileController.deleteEmployee(empOneId);
  }

  @Test
  public void updateEmployeeEmergencySuccessTest() throws Exception {
    String newEmergency = "00000000000";
    when(companyEmployeesService.verifyIfEmployeeInCompany(empOneId))
        .thenReturn(true);
    ResponseEntity<?> response = employeeProfileController.updateEmergencyContact(
        empOneId, newEmergency);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    employeeProfileController.deleteEmployee(empOneId);
  }

}
