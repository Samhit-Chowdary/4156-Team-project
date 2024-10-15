package com.nullterminators.project.repository;

import com.nullterminators.project.model.EmployeeProfile;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeProfileRepository extends JpaRepository <EmployeeProfile, Integer> {

    
}
