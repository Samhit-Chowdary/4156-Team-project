package com.nullterminators.project.repository;

import com.nullterminators.project.model.EmployeeProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for storing employee profile DB.
 */
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Integer> {

    /*
     * Check if any employee has both the same email and phone number.
     */
    Optional<EmployeeProfile> findByEmailAndPhoneNumber(String phoneNumber, String email);

}
