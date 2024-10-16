package com.nullterminators.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    private Integer employeeId;

    @NotNull
    private Integer salary;

    @NotNull
    private Integer tax;

    @Size(min = 1, max = 255)
    private String payslip;

    @NotNull
    private LocalDate paymentDate;

    @NotNull
    private Integer paid;
}
