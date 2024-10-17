package com.nullterminators.project.controller;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.service.EmployeeProfileService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
                    allEmployees.toString(), HttpStatus.OK);
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
                return new ResponseEntity<>(employee.get().toString(), HttpStatus.OK);
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
     * @param employeeProfile (EmployeeProfile)
     * @return ResponseEntity with appropriate status and message
     */
    @PostMapping("/createNewEmployee")
    public ResponseEntity<?> createNewEmployee(@RequestBody EmployeeProfile employeeProfile) {
        try {
            String email = employeeProfile.getEmail();
            String phoneNumber = employeeProfile.getPhoneNumber();
            if (employeeProfileService.employeeProfileExists(email, phoneNumber)) {
                return new ResponseEntity<>(
                        "Employee profile with given email-id and phone number exists.",
                        HttpStatus.BAD_REQUEST);
            }
            employeeProfileService.createNewEmployee(employeeProfile);
            return new ResponseEntity<>(
                    "Employee profile created successfully.", HttpStatus.CREATED);
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
        System.out.println(e.toString());
        return new ResponseEntity<>("An Error has occurred", HttpStatus.OK);
    }

  }
    
  /**
  * Return true/false depending on the employee existence
  *
  * @param id (int) : employee Id
  * @return true if present, false otherwise
  */
  private ResponseEntity<?> handleException(Exception e) {
    return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
  }

}
