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
 * Contains fields: id, name, phone number, gender, base salary,
 *                  age, start date, email and emergency contact
 */
@Data
@Entity
@Table(name = "Employee", uniqueConstraints = {@UniqueConstraint(columnNames = {"Phone_Number", "Email_Id"})
        })
public class EmployeeProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NonNull
    @Size(min = 1, max = 255)
    private String name;

    @NonNull
    @Size(min = 10, max = 17)
    @Column(name = "Phone_number")
    private String phoneNumber;

    @NonNull
    @Size(min = 1, max = 255)
    private String gender;

    @NonNull
    @Min(18)
    private Integer age;

    @NonNull
    @Column(name = "Start_Date")
    private LocalDate startDate;

    @NonNull
    @Size(min = 1, max = 255)
    private String designation;

    @NonNull
    @Email
    @Size(min = 5, max = 255)
    @Column(name = "Email_Id")
    private String email;

    @NonNull
    @Size(min = 10, max = 17)
    @Column(name = "Emergency_Contact_Number")
    private String emergencyContactNumber;

    @NonNull
    @Min(0)
    @Column(name = "Base_Salary")
    private Integer baseSalary;
    
}
