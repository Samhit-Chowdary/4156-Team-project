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
    @SequenceGenerator(name="payrollIdGenerationSeq", sequenceName="payroll_id_generation_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="payrollIdGenerationSeq")
    @Column(name = "id", updatable=false)
    private Integer id;

    private Integer employeeId;

    private Integer salary;

    private Integer tax;

    private String payslip;

    private LocalDate paymentDate;

    private Integer paid;
}
