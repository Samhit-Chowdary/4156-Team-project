package com.nullterminators.project.model;

import com.nullterminators.project.enums.LeaveStatus;
import com.nullterminators.project.enums.LeaveType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

/**
 * Represents a leave or time-off request made by an employee. This entity is mapped to the
 * "timeoff" table in the database.
 */
@Data
@Entity
@Table(name = "timeoff")
public class TimeOff implements Serializable {

  @Id
  @SequenceGenerator(
      name = "timeOffIdGenerationSeq",
      sequenceName = "timeoff_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timeOffIdGenerationSeq")
  @Column(name = "id", updatable = false)
  private Integer id;

  @NotNull(message = "Employee ID cannot be NULL")
  @Column(nullable = false)
  private Integer employeeId;

  @NotNull(message = "Leave Type cannot be NULL")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LeaveType leaveType;

  @NotNull(message = "Start Date cannot be NULL")
  @Column(nullable = false)
  private LocalDate startDate;

  @NotNull(message = "End Date cannot be NULL")
  @Column(nullable = false)
  private LocalDate endDate;

  @NotNull(message = "Reason cannot be NULL")
  @Size(min = 1, message = "Reason cannot be empty")
  @Column(nullable = false)
  private String reason;

  @NotNull(message = "Status cannot be NULL")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LeaveStatus status;

  private Integer approverId;

  private LocalDate requestDate;
}
