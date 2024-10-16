package com.nullterminators.project.repository;

import com.nullterminators.project.model.CompanyEmployees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyEmployeesRepository extends JpaRepository<CompanyEmployees, Integer> {
    public List<CompanyEmployees> findAllByCompanyUsernameAndEmployeeId(String companyUsername, Integer companyId);

    public List<CompanyEmployees> findAllByEmployeeId(Integer employeeId);

    public List<CompanyEmployees> findAllByCompanyUsername(String companyUsername);
}
