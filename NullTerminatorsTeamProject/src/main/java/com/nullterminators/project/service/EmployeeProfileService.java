package com.nullterminators.project.service;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.repository.EmployeeProfileRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for employe profiles.
 */
@Service
public class EmployeeProfileService {

  @Autowired
  private EmployeeProfileRepository employeeProfileRepository;

  /**
   * Creates a new employee profile and saves it in the DB.
   *
   * @param employee (EmployeeProfile)
   */
  public void createNewEmployee(EmployeeProfile employee) {
    employeeProfileRepository.save(employee);
  }

  /**
   * Gets the employee profile given the ID.
   *
   * @param id (int)
   * @return employee profile if available, else returns null
   */
  public Optional<EmployeeProfile> getEmployeeProfile(int id) {
    return employeeProfileRepository.findById(id);
  }

  /**
   * Lists all the existing employee profiles.
   *
   * @return list of employee profiles
   */
  public List<EmployeeProfile> getAllEmployees() {
    return employeeProfileRepository.findAll();
  }

  /**
   * Updates name of an existing employee.
   *
   * @param id   (int): Employee Id
   * @param name (String): Updated name
   * @return true if successful, false otherwise
   */
  public boolean updateEmployeeName(int id, String name) {
    boolean isEmployeePresent = employeeProfileRepository.findById(id).isPresent();

    if (isEmployeePresent) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setName(name);
      return true;
    }
    return false;
  }

  /**
   * Updates email id of an existing employee.
   *
   * @param id      (int): Employee Id
   * @param emailId (String): Updated email Id of the employee
   * @return true if successful, false otherwise
   */
  public boolean updateEmployeeEmailId(int id, String emailId) {
    boolean isEmployeePresent = employeeProfileRepository.findById(id).isPresent();

    if (isEmployeePresent) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setEmail(emailId);
      return true;
    }
    return false;
  }

  /**
   * Updates designation of an existing employee.
   *
   * @param id          (int): Employee Id
   * @param designation (String): Updated designation of the employee
   * @return true if successful, false otherwise
   */
  public boolean updateEmployeeDesignation(int id, String designation) {
    boolean isEmployeePresent = employeeProfileRepository.findById(id).isPresent();

    if (isEmployeePresent) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setDesignation(designation);
      return true;
    }
    return false;
  }

  /**
   * Updates phone number of an existing employee.
   *
   * @param id          (int): Employee Id
   * @param phoneNumber (String): Updated Phone number of the employee
   * @return true if successful, false otherwise
   */
  public boolean updateEmployeePhoneNumber(int id, String phoneNumber) {
    boolean isEmployeePresent = employeeProfileRepository.findById(id).isPresent();

    if (isEmployeePresent) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setPhoneNumber(phoneNumber);
      return true;
    }
    return false;
  }

  /**
   * Updates the base salary of an existing employee.
   *
   * @param id         (int): Employee Id
   * @param baseSalary (int): Updated base salary
   * @return true if successful, false otherwise
   */
  public boolean updateBaseSalary(int id, int baseSalary) {
    boolean isEmployeePresent = employeeProfileRepository.findById(id).isPresent();

    if (isEmployeePresent) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setBaseSalary(baseSalary);
      return true;
    }
    return false;
  }

  /**
   * Updates the emergency contact of an existing employee.
   *
   * @param id               (int): Employee Id
   * @param emergencyContact (String): Updated emergency contact
   * @return true if successful, false otherwise
   */
  public boolean updateEmergencyContact(int id, String emergencyContact) {
    boolean isEmployeePresent = employeeProfileRepository.findById(id).isPresent();

    if (isEmployeePresent) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setEmergencyContactNumber(emergencyContact);
      return true;
    }
    return false;
  }

  /**
   * Deletes the corresonding employee profile if present.
   *
   * @param id (int) : employee Id
   * @return true if successful, false otherwise
   */
  public boolean deleteEmployeeProfile(int id) {
    boolean isEmployeePresent = employeeProfileRepository.findById(id) != null;
    if (isEmployeePresent) {
      employeeProfileRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
