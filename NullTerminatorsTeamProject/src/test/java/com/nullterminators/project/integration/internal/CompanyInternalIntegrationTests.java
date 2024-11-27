package com.nullterminators.project.integration.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nullterminators.project.controller.CompanyController;
import com.nullterminators.project.model.Company;
import com.nullterminators.project.repository.CompanyRepository;
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

/** Internal integration tests for company module. */
@SpringBootTest
public class CompanyInternalIntegrationTests {

  @Autowired private CompanyController companyController;

  @MockBean private CompanyRepository companyRepository;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("testing", null));
  }

  @Test
  void testCreateCompanySuccess() {
    Company company = new Company();
    company.setUsername("testing");
    company.setPassword("testing");
    when(companyRepository.save(any(Company.class))).thenReturn(null);
    ResponseEntity<?> response = companyController.registerCompany(company);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  void testCreateCompanyFailure() {
    Company company = new Company();
    company.setPassword("testing");
    when(companyRepository.save(any(Company.class))).thenReturn(null);
    ResponseEntity<?> response = companyController.registerCompany(company);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testChangePasswordSuccess() {
    Company company = new Company();
    company.setId(1);
    company.setUsername("testing");
    company.setPassword("testing");
    when(companyRepository.findByUsername(any(String.class))).thenReturn(Optional.of(company));
    ResponseEntity<?> response = companyController.changePassword(Map.of("password", "testing"));
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testChangePasswordFailure() {
    Company company = new Company();
    company.setId(1);
    company.setUsername("testing");
    company.setPassword("testing");
    when(companyRepository.findByUsername(any(String.class))).thenReturn(Optional.of(company));
    ResponseEntity<?> response = companyController.changePassword(Map.of("password", ""));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
