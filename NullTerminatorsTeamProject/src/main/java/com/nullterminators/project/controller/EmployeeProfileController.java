package com.nullterminators.project.controller;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.service.EmployeeProfileService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API endpoints for employee profile management.
 */
@RestController
@RequestMapping("/employeeProfile")
public class EmployeeProfileController {
    
  @Autowired
  private EmployeeProfileService employeeProfileService;

  /**
   * GET /employeeProfile/ - gets a list of all existing employee profiles.
   *
   * @return ResponseEntity with appropriate status and message
   */
  @GetMapping({"/", "/getAllEmployees"})
  public ResponseEntity<?> getAllEmployees() {
    try {
      List<EmployeeProfile> allEmployees = employeeProfileService.getAllEmployees();
      return new ResponseEntity<>(
          allEmployees, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * GET /employeeProfile/{id} - gets an employee profile by id.
   *
   * @param id (int) : employee id
   * @return ResponseEntity with appropriate status and message
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> getEmployee(@PathVariable int id) {
    try {
      Optional<EmployeeProfile> employee = employeeProfileService.getEmployeeProfile(id);
      if (employee.isPresent()) {
        return new ResponseEntity<>(employee.get(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Employee with id " + id + " does not exist.",
          HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  
  /**
   * POST /employeeProfile/createNewEmployee - creates new employee profile.
   *
   * @return ResponseEntity with appropriate status and message
   */
  @PostMapping("/createNewEmployee")
  public ResponseEntity<?> createNewEmployee(@RequestParam String name, 
      @RequestParam String phoneNumber, @RequestParam String gender, 
      @RequestParam int age, @RequestParam LocalDate startDate,
      @RequestParam String designation, @RequestParam String email,
      @RequestParam String emergencyContact, @RequestParam int baseSalary) {
    try {
      EmployeeProfile employeeProfile = new EmployeeProfile();
      employeeProfile.setName(name);
      employeeProfile.setPhoneNumber(phoneNumber);
      employeeProfile.setGender(gender);
      employeeProfile.setAge(age);
      employeeProfile.setBaseSalary(baseSalary);
      employeeProfile.setStartDate(startDate);
      employeeProfile.setDesignation(designation);
      employeeProfile.setEmail(email);
      employeeProfile.setEmergencyContactNumber(emergencyContact);

      if (employeeProfileService.employeeProfileExists(email, phoneNumber)) {
        return new ResponseEntity<>(
          "Employee profile with given email-id and phone number exists.",
          HttpStatus.BAD_REQUEST);
      }
      int id = employeeProfileService.createNewEmployee(employeeProfile);
      return new ResponseEntity<>(
          id, HttpStatus.CREATED);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Updates name of an existing employee.
   *
   * @param id (int) : employee id
   * @param name (string) : new name
   * @return OK if successful, BAD_REQUEST if not, with appropriate messages
   */
  @PatchMapping("/{id}/updateName")
  public ResponseEntity<?> updateEmployeeName(@PathVariable int id, @RequestParam String name) {
    try {
      boolean completed = employeeProfileService.updateEmployeeName(id, name);
      System.out.println("L115");
      if (completed) {
        System.out.println("L117");
        return new ResponseEntity<>(
          "Employee name updated successfully.", HttpStatus.OK);
      } else {
        System.out.println("L121");
        return new ResponseEntity<>(
          "Employee not found.", HttpStatus.BAD_REQUEST);
      } 
    } catch (Exception e) {
      System.out.println("L27");
      return handleException(e);
    }
  }

  /**
   * Updates email id of an existing employee.
   *
   * @param id (int) : employee id
   * @param emailId (string) : new email id
   * @return OK if successful, BAD_REQUEST if not, with appropriate messages
   */
  @PatchMapping("/{id}/updateEmailId")
  public ResponseEntity<?> updateEmployeeEmailId(@PathVariable int id,
      @RequestParam String emailId) {
    try {
      boolean completed = employeeProfileService.updateEmployeeEmailId(id, emailId);
      if (completed) {
        return new ResponseEntity<>(
          "Employee email-id updated successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>(
          "Employee not found.", HttpStatus.BAD_REQUEST);
      } 
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Updates designation of an existing employee.
   *
   * @param id (int) : employee id
   * @param designation (string) : new designation
   * @return OK if successful, BAD_REQUEST if not, with appropriate messages
   */
  @PatchMapping("/{id}/updateDesignation")
  public ResponseEntity<?> updateEmployeeDesignation(@PathVariable int id,
      @RequestParam String designation) {
    try {
      boolean completed = employeeProfileService.updateEmployeeDesignation(id, designation);
      if (completed) {
        return new ResponseEntity<>(
          "Employee designation updated successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>(
          "Employee not found.", HttpStatus.BAD_REQUEST);
      } 
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Updates phone number of an existing employee.
   *
   * @param id (int) : employee id
   * @param phoneNumber (string) : new phone number
   * @return OK if successful, BAD_REQUEST if not, with appropriate messages
   */
  @PatchMapping("/{id}/updatePhoneNumber")
  public ResponseEntity<?> updateEmployeePhoneNumber(@PathVariable int id,
      @RequestParam String phoneNumber) {
    try {
      boolean completed = employeeProfileService.updateEmployeePhoneNumber(id, phoneNumber);
      if (completed) {
        return new ResponseEntity<>(
          "Employee phone number updated successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>(
          "Employee not found.", HttpStatus.BAD_REQUEST);
      } 
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Updates Base Salary of an existing employee.
   *
   * @param id (int) : employee id
   * @param baseSalary (int) : new base salary
   * @return OK if successful, BAD_REQUEST if not, with appropriate messages
   */
  @PatchMapping("/{id}/updateBaseSalary")
  public ResponseEntity<?> updateBaseSalary(@PathVariable int id,
      @RequestParam int baseSalary) {
    try {
      boolean completed = employeeProfileService.updateBaseSalary(id, baseSalary);
      if (completed) {
        return new ResponseEntity<>(
          "Employee base salary updated successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>(
          "Employee not found.", HttpStatus.BAD_REQUEST);
      } 
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Updates emergency contact of an existing employee.
   *
   * @param id (int) : employee id
   * @param emergencyContact (string) : new phone number
   * @return OK if successful, BAD_REQUEST if not, with appropriate messages
   */
  @PatchMapping("/{id}/updateEmergencyContact")
  public ResponseEntity<?> updateEmergencyContact(@PathVariable int id,
      @RequestParam String emergencyContact) {
    try {
      boolean completed = employeeProfileService.updateEmergencyContact(id, emergencyContact);
      if (completed) {
        return new ResponseEntity<>(
          "Employee emergency contact updated successfully.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>(
          "Employee not found.", HttpStatus.BAD_REQUEST);
      } 
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * DELETE /employeeProfile/{id} - deletes employee profile if exists.
   *
   * @param id (int) : id of the employee profile to be deleted
   * @return ResponseEntity with appropriate status and message
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
    try {
      boolean isDeleted = employeeProfileService.deleteEmployeeProfile(id);
      if (isDeleted) {
        return new ResponseEntity<>("Employee profile successfully deleted.",
            HttpStatus.OK);
      }
      return new ResponseEntity<>("Employee profile not found.",
            HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }
    
  private ResponseEntity<?> handleException(Exception e) {
    return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
  }
}
