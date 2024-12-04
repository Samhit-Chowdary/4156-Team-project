package com.nullterminators.project.integration.external;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nullterminators.project.model.Company;
import com.nullterminators.project.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** External integration tests for company module. */
@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class CompanyExternalIntegrationTests {

  @Autowired private CompanyRepository companyRepository;


  /**
   * Setup a company for testing in the test database.
   *
   * <p>This method is annotated with {@link BeforeEach} and will be called before each test in
   * this class. It creates a new company with username "testing" and saves it to the test
   * database.
   */
  @BeforeEach
  public void setUp() {
    Company company = new Company();
    company.setUsername("testing");
    company.setPassword("testing");
    company.setAddress("test");
    company.setState("NY");
    company.setName("IT");
    companyRepository.save(company);
  }

  @Test
  public void testFindByUserName() {
    Company company = companyRepository.findByUsername("testing").orElse(null);
    assertEquals("testing", company.getUsername());
  }
}
