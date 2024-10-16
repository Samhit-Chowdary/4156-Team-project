package com.nullterminators.project.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class CompanyEmployees implements Serializable {

    @Id
    @SequenceGenerator(name = "companyEmployeesIdSeq",
            sequenceName = "company_employees_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companyEmployeesIdSeq")
    private Integer id;

    private String companyUsername;

    private Integer employeeId;
}
