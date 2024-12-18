package com.nullterminators.project.service;

import static java.util.Objects.isNull;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.repository.EmployeeProfileRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for employe profiles.
 */
@Service
public class EmployeeProfileService {

  @Autowired
  private EmployeeProfileRepository employeeProfileRepository;

  @Autowired
  private CompanyEmployeesService companyEmployeesService;

  /**
   * Creates a new employee profile and saves it in the DB.
   *
   * @param employee (EmployeeProfile)
   * @return employee id of the record created
   */
  public int createNewEmployee(EmployeeProfile employee) {
    employeeProfileRepository.save(employee);
    companyEmployeesService.addEmployeeToCompany(employee.getId());
    return employee.getId();
  }

  /**
   * Checks if a profile with the same email and phone number combination exists.
   *
   * @param email (String) : email Id
   * @param phoneNumber (String) : phone number
   * @return true if exists, false otherwise
   */
  public boolean employeeProfileExists(String email, String phoneNumber) {
    Optional<EmployeeProfile> employeeProfileOptional =
        employeeProfileRepository.findByEmailAndPhoneNumber(phoneNumber, email);
    return employeeProfileOptional.isPresent()
        && companyEmployeesService.verifyIfEmployeeInCompany(
            employeeProfileOptional.get().getId());
  }

  /**
   * Gets the employee profile given the ID.
   *
   * @param id (int)
   * @return employee profile if available, else returns null
   */
  public Optional<EmployeeProfile> getEmployeeProfile(int id) {
    Optional<EmployeeProfile> employeeProfile = employeeProfileRepository.findById(id);
    if (employeeProfile.isPresent() && companyEmployeesService.verifyIfEmployeeInCompany(id)) {
      return employeeProfile;
    }
    return Optional.empty();
  }

  /**
   * Lists all the existing employee profiles.
   *
   * @return list of employee profiles
   */
  public List<EmployeeProfile> getAllEmployees() {
    List<Integer> empIds = companyEmployeesService.getAllEmployeesInCompany();

    return employeeProfileRepository.findAll().stream()
        .filter(employeeProfile -> empIds.contains(employeeProfile.getId()))
        .collect(Collectors.toList());
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

    if (isEmployeePresent && companyEmployeesService.verifyIfEmployeeInCompany(id)) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setName(name);
      employeeProfileRepository.save(existingEmployeeProfile);
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

    if (isEmployeePresent && companyEmployeesService.verifyIfEmployeeInCompany(id)) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setEmail(emailId);
      employeeProfileRepository.save(existingEmployeeProfile);
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

    if (isEmployeePresent && companyEmployeesService.verifyIfEmployeeInCompany(id)) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setDesignation(designation);
      employeeProfileRepository.save(existingEmployeeProfile);
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

    if (isEmployeePresent && companyEmployeesService.verifyIfEmployeeInCompany(id)) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setPhoneNumber(phoneNumber);
      employeeProfileRepository.save(existingEmployeeProfile);
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

    if (isEmployeePresent && companyEmployeesService.verifyIfEmployeeInCompany(id)) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setBaseSalary(baseSalary);
      employeeProfileRepository.save(existingEmployeeProfile);
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

    if (isEmployeePresent && companyEmployeesService.verifyIfEmployeeInCompany(id)) {
      EmployeeProfile existingEmployeeProfile = employeeProfileRepository.findById(id).get();
      existingEmployeeProfile.setEmergencyContactNumber(emergencyContact);
      employeeProfileRepository.save(existingEmployeeProfile);
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
    Optional<EmployeeProfile> employeeOptional = employeeProfileRepository.findById(id);
    if (employeeOptional.isPresent() && companyEmployeesService.verifyIfEmployeeInCompany(id)) {
      employeeProfileRepository.deleteById(id);
      return true;
    }
    return false;
  }

  /**
   * Return true/false depending on the employee existence.
   *
   * @param id (int) : employee Id
   * @return true if present, false otherwise
   */
  public boolean doesEmployeeExist(Integer id) {
    if (isNull(id) || id < 0) {
      return false;
    }
    return employeeProfileRepository.findById(id).isPresent()
        && companyEmployeesService.verifyIfEmployeeInCompany(id);
  }
}
