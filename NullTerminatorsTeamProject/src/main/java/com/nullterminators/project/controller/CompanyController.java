package com.nullterminators.project.controller;

import com.nullterminators.project.model.Company;
import com.nullterminators.project.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for API endpoints for company management.
 */
@RestController
public class CompanyController {

  private final CompanyService companyService;

  @Autowired
  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }


  /**
   * POST /registerCompany - registers a new company.
   *
   * @param company (Company(for exact fields refer Company.java)) : company to be registered
   * @return ResponseEntity with appropriate status and message
   */
  @PostMapping("/registerCompany")
  public ResponseEntity<?> registerCompany(@RequestBody Company company) {
    try {
      companyService.registerCompany(company);
      return new ResponseEntity<>("Company is registered successfully", HttpStatus.CREATED);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<?> handleException(Exception e) {
    System.out.println(e.toString());
    return new ResponseEntity<>("An Error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
