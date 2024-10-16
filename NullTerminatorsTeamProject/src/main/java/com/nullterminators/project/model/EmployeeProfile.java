package com.nullterminators.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 * Database fields for an employee's profile.
 * Contains fields: id, name, phone number, gender, base salary,
 *     age, start date, email and emergency contact
 */
@Data
@Entity
@Table(name = "Employee")
public class EmployeeProfile implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @Size(min = 1, max = 255)
  private String name;

  @NotNull
  @Size(min = 10, max = 17)
  private String phoneNumber;

  @NotNull
  @Size(min = 1, max = 255)
  private String gender;

  @NotNull
  @Min(18)
  private Integer age;

  @NotNull
  private LocalDate startDate;

  @NotNull
  @Size(min = 1, max = 255)
  private String designation;

  @NotNull
  @Email
  @Size(min = 5, max = 255)
  @Column(name = "Email_Id")
  private String email;

  @NotNull
  @Size(min = 10, max = 17)
  private String emergencyContactNumber;

  @NotNull
  @Min(0)
  private Integer baseSalary;
}
