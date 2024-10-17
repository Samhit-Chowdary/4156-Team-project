package com.nullterminators.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.io.Serializable;
import lombok.Data;

/**
 * CompanyEmployees Model to map company employees table in DB.
 */
@Data
@Entity
public class CompanyEmployees implements Serializable {

  @Id
  @SequenceGenerator(
      name = "companyEmployeesIdSeq",
      sequenceName = "company_employees_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companyEmployeesIdSeq")
  private Integer id;

  private String companyUsername;

  private Integer employeeId;
}
