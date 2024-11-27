package com.nullterminators.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nullterminators.project.model.Company;
import com.nullterminators.project.service.CompanyService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

/**
 * Controller Tests for Company.
 */
@SpringBootTest
@ContextConfiguration
public class CompanyControllerTests {

  @Mock private CompanyService companyService;

  private CompanyController companyController;

  @BeforeEach
  void setUp() {
    companyController = new CompanyController(companyService);
  }

  @Test
  void testRegisterCompanySuccess() {
    when(companyService.registerCompany(any(Company.class))).thenReturn(null);
    Company company = new Company();
    company.setPassword("password");
    company.setUsername("username");
    ResponseEntity<?> response = companyController.registerCompany(company);
    assertEquals(Map.of("response", "Company is created successfully."), response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  void testRegisterCompanyUsernameAbsent() {
    when(companyService.registerCompany(any(Company.class))).thenReturn(null);
    Company company = new Company();
    company.setPassword("password");
    ResponseEntity<?> response = companyController.registerCompany(company);
    assertEquals(Map.of("response", "username and password are required"), response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testRegisterCompanyPasswordAbsent() {
    when(companyService.registerCompany(any(Company.class))).thenReturn(null);
    Company company = new Company();
    company.setUsername("username");
    ResponseEntity<?> response = companyController.registerCompany(company);
    assertEquals(Map.of("response", "username and password are required"), response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testRegisterCompanyUsernameExists() {
    when(companyService.registerCompany(any(Company.class)))
        .thenReturn("company with same username already exists");
    Company company = new Company();
    company.setPassword("password");
    company.setUsername("username");
    ResponseEntity<?> response = companyController.registerCompany(company);
    assertEquals(
        Map.of("response", "company with same username already exists"), response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testChangePasswordSuccess() {
    when(companyService.changePassword(anyString())).thenReturn(true);
    ResponseEntity<?> response = companyController.changePassword(Map.of("password", "testing"));
    assertEquals(Map.of("response", "Company password changed successfully."), response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testChangePasswordFailure() {
    when(companyService.changePassword(anyString())).thenReturn(false);
    ResponseEntity<?> response = companyController.changePassword(Map.of("password", "testing"));
    assertEquals(Map.of("response", "Company password change failed."), response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testChangePasswordFailurePasswordAbsent() {
    ResponseEntity<?> response = companyController.changePassword(Map.of("password", ""));
    assertEquals(Map.of("response", "proper password is required"), response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
