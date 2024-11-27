package com.nullterminators.project.service;

import com.nullterminators.project.model.CompanyEmployees;
import com.nullterminators.project.repository.CompanyEmployeesRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for company employee management.
 */
@Service
public class CompanyEmployeesService {

  @Autowired private CompanyEmployeesRepository companyEmployeesRepository;

  @Autowired private CompanyService companyService;

  public CompanyEmployeesService(
          CompanyEmployeesRepository companyEmployeesRepository,
          CompanyService companyService
  ) {
    this.companyService = companyService;
    this.companyEmployeesRepository = companyEmployeesRepository;
  }

  /**
   * Checks if the given employee is in the company.
   *
   * @param employeeId employee id to be checked
   * @return true if the employee is in the company, false otherwise
   */
  public Boolean verifyIfEmployeeInCompany(Integer employeeId) {
    String companyUsername = companyService.getCompanyUsername();
    List<CompanyEmployees> companyEmployees =
        companyEmployeesRepository.findAllByCompanyUsernameAndEmployeeId(
            companyUsername, employeeId);
    return !companyEmployees.isEmpty();
  }

  /**
   * Adds the given employee to the company.
   *
   * @param employeeId the id of the employee to be added
   * @return true if the employee was successfully added, false otherwise
   */
  public Boolean addEmployeeToCompany(Integer employeeId) {
    List<CompanyEmployees> companyEmployeesList =
        companyEmployeesRepository.findAllByEmployeeId(employeeId);
    if (!companyEmployeesList.isEmpty()) {
      return false;
    }
    CompanyEmployees companyEmployee = new CompanyEmployees();
    companyEmployee.setEmployeeId(employeeId);
    companyEmployee.setCompanyUsername(companyService.getCompanyUsername());
    companyEmployeesRepository.save(companyEmployee);
    return true;
  }


  /**
   * Returns a list of employee ids of all employees in the company.
   *
   * @return a list of employee ids
   */
  public List<Integer> getAllEmployeesInCompany() {
    List<Integer> employeeIds = new ArrayList<Integer>();
    List<CompanyEmployees> companyEmployeesList =
        companyEmployeesRepository.findAllByCompanyUsername(companyService.getCompanyUsername());
    for (CompanyEmployees companyEmployees : companyEmployeesList) {
      employeeIds.add(companyEmployees.getEmployeeId());
    }
    return employeeIds;
  }
}
