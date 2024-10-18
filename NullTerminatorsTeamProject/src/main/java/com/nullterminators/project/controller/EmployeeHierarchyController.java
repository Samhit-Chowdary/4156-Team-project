package com.nullterminators.project.controller;

import com.nullterminators.project.model.EmployeeHierarchy;
import com.nullterminators.project.model.EmployeeNode;
import com.nullterminators.project.service.EmployeeHierarchyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** API endpoints for employee hierarchy management. */
@RestController
@RequestMapping("/api/employee-hierarchy")
public class EmployeeHierarchyController {

  @Autowired private EmployeeHierarchyService employeeHierarchyService;

  /**
   * Finds all subordinates of the employee with the given id.
   *
   * @param fromEmployeeId the id of the employee whose subordinates are to be found
   * @return a list of subordinates of the given employee
   */
  @GetMapping("/subordinates/{fromEmployeeId}")
  public ResponseEntity<?> getSubordinates(@PathVariable Long fromEmployeeId) {
    boolean employeeExists = employeeHierarchyService.employeeExists(fromEmployeeId);

    if (!employeeExists) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Employee with ID " + fromEmployeeId + " not found");
    }

    List<EmployeeHierarchy> hierarchyList =
        employeeHierarchyService.getSubordinates(fromEmployeeId);
    return ResponseEntity.ok(hierarchyList);
  }

  /**
   * Finds the supervisor of the employee with the given id.
   *
   * @param toEmployeeId the id of the employee whose supervisor is to be found
   * @return the id of the supervisor of the given employee
   */
  @GetMapping("/supervisor/{toEmployeeId}")
  public ResponseEntity<?> getSupervisorByEmployeeId(@PathVariable Long toEmployeeId) {
    boolean employeeExists = employeeHierarchyService.employeeExists(toEmployeeId);

    if (!employeeExists) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Employee with ID " + toEmployeeId.toString() + " not found");
    }

    Long supervisorId = employeeHierarchyService.getSupervisor(toEmployeeId);
    if (supervisorId != null) {
      return ResponseEntity.ok(supervisorId);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Supervisor for Employee with ID " + toEmployeeId.toString() + " doesn't exist");
    }
  }

  /**
   * Gets the subtree of an employee with the given id.
   *
   * @param employeeId the id of the employee
   * @return the subtree of the given employee
   */
  @GetMapping("/tree/{employeeId}")
  public ResponseEntity<?> getSubtree(@PathVariable Long employeeId) {
    boolean employeeExists = employeeHierarchyService.employeeExists(employeeId);

    if (!employeeExists) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Employee with ID " + employeeId + " not found");
    }

    EmployeeNode employeeTree = employeeHierarchyService.buildEmployeeTree(employeeId);
    return ResponseEntity.ok(employeeTree);
  }

  /**
   * Adds a supervisor-employee edge.
   *
   * @param supervisorId the ID of the supervisor
   * @param employeeId the ID of the employee
   * @return a response entity with status
   */
  @GetMapping("/addEdge/{supervisorId}/{employeeId}")
  public ResponseEntity<?> addEmployeeSupervisorEdge(
      @PathVariable Long supervisorId, @PathVariable Long employeeId) {
    boolean supervisorExists = employeeHierarchyService.employeeExists(supervisorId);
    boolean employeeExists = employeeHierarchyService.employeeExists(employeeId);

    if (!supervisorExists || !employeeExists) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Either supervisor or employee doesn't exist.");
    }

    boolean added = employeeHierarchyService.addEmployeeSupervisorEdge(supervisorId, employeeId);
    if (added) {
      return ResponseEntity.ok("Edge added successfully.");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              "Operation failed: Employee already has a supervisor, "
                  + "or adding this edge would create a cycle.");
    }
  }

  /**
   * Removes a supervisor-employee edge.
   *
   * @param employeeId the ID of the employee whose supervisor edge is to be removed
   * @return a response entity with status
   */
  @GetMapping("/removeEdge/{employeeId}")
  public ResponseEntity<?> removeEmployeeSupervisorEdge(@PathVariable Long employeeId) {
    boolean employeeExists = employeeHierarchyService.employeeExists(employeeId);

    if (!employeeExists) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Employee with ID " + employeeId + " not found.");
    }

    boolean removed = employeeHierarchyService.removeEmployeeSupervisorEdge(employeeId);
    if (removed) {
      return ResponseEntity.ok("Edge removed successfully.");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Operation failed: Employee has no supervisor.");
    }
  }
}
