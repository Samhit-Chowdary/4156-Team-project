package com.nullterminators.project.repository;

import com.nullterminators.project.model.CompanyEmployees;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for company employees table.
 */
public interface CompanyEmployeesRepository extends JpaRepository<CompanyEmployees, Integer> {
  List<CompanyEmployees> findAllByCompanyUsernameAndEmployeeId(
      String companyUsername, Integer companyId);

  List<CompanyEmployees> findAllByEmployeeId(Integer employeeId);

  List<CompanyEmployees> findAllByCompanyUsername(String companyUsername);
}
