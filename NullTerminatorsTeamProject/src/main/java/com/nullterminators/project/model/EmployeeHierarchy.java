package com.nullterminators.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a record in the employee hierarchy table. This table is used to store the hierarchy
 * between employees. It is a many-to-many relationship between employees. The from employee is the
 * manager and the to employee is the subordinate.
 */
@Entity
@Table(name = "employee_hierarchy")
public class EmployeeHierarchy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "from_employee_id", nullable = false)
  private Long fromEmployeeId;

  @Column(name = "to_employee_id", nullable = false)
  private Long toEmployeeId;

  public EmployeeHierarchy(Long fromEmployeeId, Long toEmployeeId) {
    this.fromEmployeeId = fromEmployeeId;
    this.toEmployeeId = toEmployeeId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getFromEmployeeId() {
    return fromEmployeeId;
  }

  public void setFromEmployeeId(Long fromEmployeeId) {
    this.fromEmployeeId = fromEmployeeId;
  }

  public Long getToEmployeeId() {
    return toEmployeeId;
  }

  public void setToEmployeeId(Long toEmployeeId) {
    this.toEmployeeId = toEmployeeId;
  }
}
