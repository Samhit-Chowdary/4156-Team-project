package com.nullterminators.project.repository;

import com.nullterminators.project.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeProfileRepository extends JpaRepository <EmployeeProfile, Integer> {
    
}
