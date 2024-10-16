package com.nullterminators.project.service;

import com.nullterminators.project.model.CompanyEmployees;
import com.nullterminators.project.repository.CompanyEmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyEmployeesService {

    @Autowired
    private CompanyEmployeesRepository companyEmployeesRepository;

    @Autowired
    private CompanyService companyService;


    public Boolean verifyIfEmployeeInCompany(Integer employeeId) {
        String companyUsername = companyService.getCompanyUsername();
        List<CompanyEmployees> companyEmployees = companyEmployeesRepository.findAllByCompanyUsernameAndEmployeeId(
                companyUsername, employeeId
        );
        return !companyEmployees.isEmpty();
    }

    public Boolean addEmployeeToCompany(Integer employeeId) {
        List<CompanyEmployees> companyEmployeesList = companyEmployeesRepository.findAllByEmployeeId(employeeId);
        if (!companyEmployeesList.isEmpty()) {
            return false;
        }
        CompanyEmployees companyEmployee = new CompanyEmployees();
        companyEmployee.setEmployeeId(employeeId);
        companyEmployee.setCompanyUsername(companyService.getCompanyUsername());
        companyEmployeesRepository.save(companyEmployee);
        return true;
    }

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
