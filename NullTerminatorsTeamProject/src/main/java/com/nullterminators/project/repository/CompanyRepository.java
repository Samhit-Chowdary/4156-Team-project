package com.nullterminators.project.repository;

import com.nullterminators.project.model.Company;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  Repository for company table.
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {
  Optional<Company> findByUsername(String username);
}
