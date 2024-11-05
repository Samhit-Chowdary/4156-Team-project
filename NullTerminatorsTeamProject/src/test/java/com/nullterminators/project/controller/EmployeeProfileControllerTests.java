package com.nullterminators.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.service.EmployeeProfileService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

/**
 * Controller Tests for Employee Profile.
 */
@SpringBootTest
@ContextConfiguration
public class EmployeeProfileControllerTests {
  private EmployeeProfile employeeProfile1;
  private EmployeeProfile employeeProfile2;

  @Mock
  private EmployeeProfileService employeeProfileService;

  @InjectMocks
  private EmployeeProfileController employeeProfileController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    employeeProfile1 = new EmployeeProfile();
    employeeProfile1.setId(1);
    employeeProfile1.setName("Employee One");
    employeeProfile1.setAge(50);
    employeeProfile1.setEmail("employeeOne@email.com");
    employeeProfile1.setDesignation("Manager");
    employeeProfile1.setPhoneNumber("+1-123-456-7904");
    employeeProfile1.setGender("Female");
    employeeProfile1.setStartDate(LocalDate.now());
    employeeProfile1.setEmergencyContactNumber("9876543210");
    employeeProfile1.setBaseSalary(233445);

    employeeProfile2 = new EmployeeProfile();
    employeeProfile2.setId(2);
    employeeProfile2.setName("Employee Two");
    employeeProfile2.setAge(50);
    employeeProfile2.setEmail("employeeTwo@email.com");
    employeeProfile2.setDesignation("Manager");
    employeeProfile2.setPhoneNumber("+1-123-456-7905");
    employeeProfile2.setGender("Male");
    employeeProfile2.setStartDate(LocalDate.now());
    employeeProfile2.setEmergencyContactNumber("9876543210");
    employeeProfile2.setBaseSalary(233445);
  }

  @Test
  public void getAllEmployeesTestSuccess() {
    List<EmployeeProfile> mockReply = new ArrayList<>();
    mockReply.add(employeeProfile1);
    mockReply.add(employeeProfile2);
    when(employeeProfileService.getAllEmployees()).thenReturn(mockReply);
    ResponseEntity<?> response = employeeProfileController.getAllEmployees();

    assertEquals(mockReply, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void getAllEmployeeException() {
    when(employeeProfileService.getAllEmployees()).thenThrow(
      new RuntimeException()
    );

    ResponseEntity<?> response = employeeProfileController.getAllEmployees();

    assertEquals("java.lang.RuntimeException", response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void getEmployeeIdSuccess() {
    when(employeeProfileService.getEmployeeProfile(1)).thenReturn(
        Optional.of(employeeProfile1)
    );

    ResponseEntity<?> response = employeeProfileController.getEmployee(1);

    assertEquals(employeeProfile1, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
  
  @Test
  public void getEmployeeIdNotFound() {
    when(employeeProfileService.getEmployeeProfile(1)).thenReturn(
        Optional.empty()
    );

    ResponseEntity<?> response = employeeProfileController.getEmployee(1);

    assertEquals("Employee with id 1 does not exist.", response.getBody());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void createNewEmployeeTestSuccess() {
    when(employeeProfileService.employeeProfileExists("employeeOne@email.com",
        "+1-123-456-7904")).thenReturn(false);

    ResponseEntity<?> response = employeeProfileController.createNewEmployee(employeeProfile1);

    assertEquals("Employee profile created successfully.", response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    verify(employeeProfileService).createNewEmployee(employeeProfile1);
  }

  @Test
  public void createNewEmployeeTestEmployeeExists() {
    when(employeeProfileService.employeeProfileExists("employeeOne@email.com",
        "+1-123-456-7904")).thenReturn(true);

    ResponseEntity<?> response = employeeProfileController.createNewEmployee(employeeProfile1);

    assertEquals("Employee profile with given email-id and phone number exists.",
        response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    verify(employeeProfileService, never()).createNewEmployee(employeeProfile1);
  }

  @Test
  public void deleteEmployeeTestSuccess() {
    when(employeeProfileService.deleteEmployeeProfile(1))
        .thenReturn(true);
    
    ResponseEntity<?> response = employeeProfileController.deleteEmployee(1);

    assertEquals("Employee profile successfully deleted.", response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void deleteEmployeeTestEmployeeNotFound() {
    when(employeeProfileService.deleteEmployeeProfile(1))
        .thenReturn(false);
    
    ResponseEntity<?> response = employeeProfileController.deleteEmployee(1);

    assertEquals("Employee profile not found.", response.getBody());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void updateEmployeeNameTestSuccess() {
    int id = 1;
    String name = "New Name";
    when(employeeProfileService.updateEmployeeName(id, name)).thenReturn(true);

    ResponseEntity<?> response = employeeProfileController.updateEmployeeName(id, name);

    assertEquals("Employee name updated successfully.", response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void updateEmployeeNameTestFailure() {
    int id = 1;
    String name = "New Name";
    when(employeeProfileService.updateEmployeeName(id, name)).thenReturn(false);

    ResponseEntity<?> response = employeeProfileController.updateEmployeeName(id, name);

    assertEquals("Employee not found.", response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void updateEmployeeEmailIdTestSuccess() {
    int id = 1;
    String emailId = "New@gmail.com";
    when(employeeProfileService.updateEmployeeEmailId(id, emailId)).thenReturn(true);

    ResponseEntity<?> response = employeeProfileController.updateEmployeeEmailId(id, emailId);

    assertEquals("Employee email-id updated successfully.", response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void updateEmployeeEmailIdTestFailure() {
    int id = 1;
    String emailId = "New@email.com";
    when(employeeProfileService.updateEmployeeEmailId(id, emailId)).thenReturn(false);

    ResponseEntity<?> response = employeeProfileController.updateEmployeeEmailId(id, emailId);

    assertEquals("Employee not found.", response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void updateEmployeeDesignationTestSuccess() {
    int id = 1;
    String designation = "new designation";
    when(employeeProfileService.updateEmployeeDesignation(id, designation)).thenReturn(true);

    ResponseEntity<?> response = employeeProfileController.updateEmployeeDesignation(id,
        designation);

    assertEquals("Employee designation updated successfully.", response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void updateEmployeeDesignationTestFailure() {
    int id = 1;
    String designation = "New designation";
    when(employeeProfileService.updateEmployeeDesignation(id, designation)).thenReturn(false);

    ResponseEntity<?> response = employeeProfileController.updateEmployeeDesignation(id,
        designation);

    assertEquals("Employee not found.", response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void updateEmployeePhoneNumberTestSuccess() {
    int id = 1;
    String phoneNumber = "1234657890";
    when(employeeProfileService.updateEmployeePhoneNumber(id, phoneNumber)).thenReturn(true);

    ResponseEntity<?> response = employeeProfileController.updateEmployeePhoneNumber(id,
        phoneNumber);

    assertEquals("Employee phone number updated successfully.", response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void updateEmployeePhoneNumberTestFailure() {
    int id = 1;
    String phoneNumber = "1234657890";
    when(employeeProfileService.updateEmployeePhoneNumber(id, phoneNumber)).thenReturn(false);

    ResponseEntity<?> response = employeeProfileController.updateEmployeePhoneNumber(id,
        phoneNumber);

    assertEquals("Employee not found.", response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void updateEmployeeBaseSalaryTestSuccess() {
    int id = 1;
    int baseSalary = 1234484;
    when(employeeProfileService.updateBaseSalary(id, baseSalary)).thenReturn(true);

    ResponseEntity<?> response = employeeProfileController.updateBaseSalary(id, baseSalary);

    assertEquals("Employee base salary updated successfully.", response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void updateEmployeeBaseSalaryTestFailure() {
    int id = 1;
    int baseSalary = 1234484;
    when(employeeProfileService.updateBaseSalary(id, baseSalary)).thenReturn(false);

    ResponseEntity<?> response = employeeProfileController.updateBaseSalary(id, baseSalary);

    assertEquals("Employee not found.", response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void updateEmergencyContactTestSuccess() {
    int id = 1;
    String emergencyContact = "9876543210";
    when(employeeProfileService.updateEmergencyContact(id, emergencyContact)).thenReturn(true);

    ResponseEntity<?> response = employeeProfileController.updateEmergencyContact(id,
        emergencyContact);

    assertEquals("Employee emergency contact updated successfully.", response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void updateEmergencyContactTestFailure() {
    int id = 1;
    String emergencyContact = "9876543210";
    when(employeeProfileService.updateEmergencyContact(id, emergencyContact)).thenReturn(false);

    ResponseEntity<?> response = employeeProfileController.updateEmergencyContact(id,
        emergencyContact);

    assertEquals("Employee not found.", response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

}

