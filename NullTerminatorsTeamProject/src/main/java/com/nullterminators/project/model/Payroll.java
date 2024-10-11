package com.nullterminators.project.model;

import jakarta.persistence.*;
import lombok.Data;


import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payroll")
public class Payroll implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer employeeId;

    private Integer salary;

    private Integer tax;

    private String payslip;

    private LocalDate paymentDate;
}
