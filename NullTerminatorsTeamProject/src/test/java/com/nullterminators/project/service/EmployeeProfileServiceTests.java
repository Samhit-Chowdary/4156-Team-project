package com.nullterminators.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.repository.EmployeeProfileRepository;
import com.nullterminators.project.service.CompanyEmployeesService;
import com.nullterminators.project.service.EmployeeProfileService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Tests for employee profile service functions.
 */
@SpringBootTest
@ContextConfiguration
public class EmployeeProfileServiceTests {

  @Mock
  private EmployeeProfileRepository employeeProfileRepository;

  @Mock
  private CompanyEmployeesService companyEmployeesService;

  @InjectMocks
  private EmployeeProfileService employeeProfileService;

  private EmployeeProfile employeeProfile1;
  private EmployeeProfile employeeProfile2;
  
  /**
   * Setting up employee profiles.
   */
  @BeforeEach
  public void setUp() {
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

    when(companyEmployeesService.verifyIfEmployeeInCompany(anyInt())).thenReturn(true);
  }

  @Test
  public void createNewEmployeeProfileTest() {
    // create employee profile for employee 1 & 2
    employeeProfileService.createNewEmployee(employeeProfile1);
    Mockito.verify(employeeProfileRepository).save(employeeProfile1);
    Mockito.verify(companyEmployeesService).addEmployeeToCompany(employeeProfile1.getId());
    employeeProfileService.createNewEmployee(employeeProfile2);
    Mockito.verify(employeeProfileRepository).save(employeeProfile2);
    Mockito.verify(companyEmployeesService).addEmployeeToCompany(employeeProfile2.getId());
  }

  @Test
  public void getEmployeeProfileTest() {
    when(employeeProfileRepository.findById(1)).thenReturn(Optional.of(employeeProfile1));
    when(employeeProfileRepository.findById(2)).thenReturn(Optional.of(employeeProfile2));

    Optional<EmployeeProfile> result1 = employeeProfileService.getEmployeeProfile(1);
    
    verify(employeeProfileRepository).findById(1);
    assertEquals(Optional.of(employeeProfile1), result1);

    Optional<EmployeeProfile> result2 = employeeProfileService.getEmployeeProfile(2);
    
    verify(employeeProfileRepository).findById(2);
    assertEquals(Optional.of(employeeProfile2), result2);
  }

  @Test
  public void getAllEmployeesTest() {
    List<EmployeeProfile> allEmployees = new ArrayList<>();
    allEmployees.add(employeeProfile1);
    allEmployees.add(employeeProfile2);

    when(employeeProfileRepository.findAll()).thenReturn(allEmployees);
    when(companyEmployeesService.getAllEmployeesInCompany()).thenReturn(List.of(1, 2));

    List<EmployeeProfile> result = employeeProfileService.getAllEmployees();
    assertEquals(allEmployees, result);
  }

  @Test
  public void updateEmployeeNameTestSuccess() {
    String newName = "New Name";
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.of(employeeProfile1));

    boolean result = employeeProfileService.updateEmployeeName(id, newName);
    assertTrue(result);
    assertEquals(newName, employeeProfile1.getName());
  }

  @Test
  public void updateEmployeeNameTestFailure() {
    String newName = "New Name";
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.empty());

    boolean result = employeeProfileService.updateEmployeeName(id, newName);
    assertFalse(result);
  }

  @Test
  public void updateEmployeeEmailIdTestSuccess() {
    String newEmail = "newemail@example.com";
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.of(employeeProfile1));

    boolean result = employeeProfileService.updateEmployeeEmailId(id, newEmail);
    assertTrue(result);
    assertEquals(newEmail, employeeProfile1.getEmail());
  }

  @Test
  public void updateEmployeeEmailIdTestFailure() {
    String newEmail = "newemail@example.com";
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.empty());

    boolean result = employeeProfileService.updateEmployeeEmailId(id, newEmail);
    assertFalse(result);
  }

  @Test
  public void updateEmployeeDesignationTestSuccess() {
    String newDesignation = "Senior Manager";
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.of(employeeProfile1));

    boolean result = employeeProfileService.updateEmployeeDesignation(id, newDesignation);
    assertTrue(result);
    assertEquals(newDesignation, employeeProfile1.getDesignation());
  }

  @Test
  public void updateEmployeeDesignationTestFailure() {
    String newDesignation = "Senior Manager";
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.empty());

    boolean result = employeeProfileService.updateEmployeeDesignation(id, newDesignation);
    assertFalse(result);
  }

  @Test
  public void updateEmployeePhoneNumberTestSuccess() {
    String newPhoneNum = "0000000000";
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.of(employeeProfile1));

    boolean result = employeeProfileService.updateEmployeePhoneNumber(id, newPhoneNum);
    assertTrue(result);
    assertEquals(newPhoneNum, employeeProfile1.getPhoneNumber());
  }

  @Test
  public void updateEmployeePhoneNumberTestFailure() {
    String newPhoneNum = "0000000000";
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.empty());

    boolean result = employeeProfileService.updateEmployeePhoneNumber(id, newPhoneNum);
    assertFalse(result);
  }

  @Test
  public void updateBaseSalaryTestSuccess() {
    int newSalary = 500000;
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.of(employeeProfile1));

    boolean result = employeeProfileService.updateBaseSalary(id, newSalary);
    assertTrue(result);
    assertEquals(newSalary, employeeProfile1.getBaseSalary());
  }

  @Test
  public void updateBaseSalaryTestFailure() {
    int newSalary = 500000;
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.empty());

    boolean result = employeeProfileService.updateBaseSalary(id, newSalary);
    assertFalse(result);
  }

  @Test
  public void updateupdateEmergencyContactTestSuccess() {
    String newPhoneNum = "0000000000";
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.of(employeeProfile1));

    boolean result = employeeProfileService.updateEmergencyContact(id, newPhoneNum);
    assertTrue(result);
    assertEquals(newPhoneNum, employeeProfile1.getEmergencyContactNumber());
  }

  @Test
  public void updateEmergencyContactTestFailure() {
    String newPhoneNum = "0000000000";
    int id = 1;

    when(employeeProfileRepository.findById(id)).thenReturn(Optional.empty());

    boolean result = employeeProfileService.updateEmergencyContact(id, newPhoneNum);
    assertFalse(result);
  }

}
