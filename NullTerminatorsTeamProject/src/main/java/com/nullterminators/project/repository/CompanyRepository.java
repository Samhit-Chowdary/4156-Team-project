package com.nullterminators.project.repository;

import com.nullterminators.project.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByUsername(String username);
}
