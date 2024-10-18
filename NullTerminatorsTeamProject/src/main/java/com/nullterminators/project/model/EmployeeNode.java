package com.nullterminators.project.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A node in the employee hierarchy. Each node has an id and a list of its children in the
 * hierarchy.
 */
public class EmployeeNode {
  private Long id;
  private List<EmployeeNode> children;

  public EmployeeNode(Long id) {
    this.id = id;
    this.children = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<EmployeeNode> getChildren() {
    return children;
  }

  public void setChildren(List<EmployeeNode> children) {
    this.children = children;
  }
}
