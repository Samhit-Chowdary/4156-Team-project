package com.nullterminators.project.model;

import jakarta.persistence.*;
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
    private String phoneNumber;

    @NonNull
    private String gender;

    @NonNull
    private String address;

    @NonNull
    private Integer age;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private String email;

    @NonNull
    private String emergencyContactName;

    @NonNull
    private String emergencyContactRelation;

    @NonNull
    private String emergencyContactNumber;
    
}
