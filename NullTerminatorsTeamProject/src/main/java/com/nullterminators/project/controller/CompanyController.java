package com.nullterminators.project.controller;

import com.nullterminators.project.model.Company;
import com.nullterminators.project.service.CompanyService;
import com.nullterminators.project.util.StringUtil;
import java.util.Map;
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
      if (StringUtil.isNullOrBlank(company.getPassword())
              || StringUtil.isNullOrBlank(company.getUsername())) {
        return new ResponseEntity<>(Map.of("response", "username and password are required"),
                HttpStatus.BAD_REQUEST);
      }
      String error = companyService.registerCompany(company);
      if (error == null) {
        return new ResponseEntity<>(
                Map.of("response", "Company is created successfully."),
                HttpStatus.CREATED
        );
      }
      return new ResponseEntity<>(Map.of("response", error), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * POST /changePassword - changes the password of the company.
   *
   * @param body (Map) : contains the new password
   * @return ResponseEntity with appropriate status and message
   */
  @PostMapping("/company/changePassword")
  public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
    try {
      String password = body.get("password");
      if (StringUtil.isNullOrBlank(password)) {
        return new ResponseEntity<>(Map.of("response", "proper password is required"),
                HttpStatus.BAD_REQUEST);
      }
      boolean passChanged = companyService.changePassword(password);
      if (passChanged) {
        return new ResponseEntity<>(
                Map.of("response", "Company password changed successfully."),
                HttpStatus.OK
        );
      }
      return new ResponseEntity<>(Map.of("response", "Company password change failed."),
              HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return handleException(e);
    }
  }



  private ResponseEntity<?> handleException(Exception e) {
    System.out.println(e.toString());
    return new ResponseEntity<>("An Error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
