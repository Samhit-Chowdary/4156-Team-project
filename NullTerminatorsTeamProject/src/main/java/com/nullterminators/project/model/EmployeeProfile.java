package com.nullterminators.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Data;


import java.io.Serializable;
import java.time.LocalDate;

import io.micrometer.common.lang.NonNull;

/**
 * Database fields for an employee's profile
 * Contains fields: id, name, phone number, gender, address,
 *                  age, start date, email and emergency contact
 */
@Data
@Entity
@Table(name = "employeeProfile")
public class EmployeeProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String name;

    @NonNull
    @Size(max = 17, message = "A phone number can have at max of 17 characters")
    private String phoneNumber;

    @NonNull
    private String gender;

    @NonNull
    private String address;

    @NonNull
    @Min(18)
    private Integer age;

    @NonNull
    private LocalDate startDate;

    @NonNull
    @Email
    private String email;

    @NonNull
    private String emergencyContactName;

    @NonNull
    private String emergencyContactRelation;

    @NonNull
    @Size(max = 17, message = "A phone number can have at max of 17 characters")
    private String emergencyContactNumber;
    
}
