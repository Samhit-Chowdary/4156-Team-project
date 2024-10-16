package com.nullterminators.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "payroll")
public class Payroll implements Serializable {

  @Id
  @SequenceGenerator(name = "payrollIdGenerationSeq",
          sequenceName = "payroll_id_generation_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payrollIdGenerationSeq")
  @Column(name = "id", updatable = false)
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
