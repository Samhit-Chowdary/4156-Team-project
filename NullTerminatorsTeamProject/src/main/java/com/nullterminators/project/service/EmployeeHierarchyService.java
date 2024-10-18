package com.nullterminators.project.service;

import com.nullterminators.project.model.EmployeeHierarchy;
import com.nullterminators.project.model.EmployeeNode;
import com.nullterminators.project.repository.EmployeeHierarchyRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service class for managing employee hierarchy. */
@Service
public class EmployeeHierarchyService {

  @Autowired private EmployeeHierarchyRepository employeeHierarchyRepository;
  @Autowired private CompanyEmployeesService companyEmployeesService;

  /**
   * Finds all subordinates of the employee with the given id.
   *
   * @param fromEmployeeId the id of the employee whose subordinates are to be found
   * @return a list of subordinates of the given employee
   */
  public List<EmployeeHierarchy> getSubordinates(Long fromEmployeeId) {
    return employeeHierarchyRepository.findByFromEmployeeId(fromEmployeeId);
  }

  /**
   * Finds the supervisor of the employee with the given id.
   *
   * @param toEmployeeId the id of the employee whose supervisor is to be found
   * @return the id of the supervisor of the given employee, or null if no supervisor exists
   */
  public Long getSupervisor(Long toEmployeeId) {
    return employeeHierarchyRepository.findSupervisorByEmployeeId(toEmployeeId);
  }

  /**
   * Finds the subtree of an employee with the given id.
   *
   * @param employeeId the id of the employee
   * @return a list of all the subordinates of the given employee
   */
  public List<EmployeeHierarchy> getSubtree(Long employeeId) {
    return employeeHierarchyRepository.findSubtreeByEmployeeId(employeeId);
  }

  /**
   * Builds an {@link EmployeeNode} tree with the given root employee as the root of the tree.
   *
   * @param rootEmployeeId the id of the root employee
   * @return the root of the tree
   */
  public EmployeeNode buildEmployeeTree(Long rootEmployeeId) {
    List<EmployeeHierarchy> allSubordinates =
        employeeHierarchyRepository.findSubtreeByEmployeeId(rootEmployeeId);

    Map<Long, EmployeeNode> employeeNodeMap = new HashMap<>();

    for (EmployeeHierarchy hierarchy : allSubordinates) {
      employeeNodeMap.putIfAbsent(
          hierarchy.getFromEmployeeId(), new EmployeeNode(hierarchy.getFromEmployeeId()));
      employeeNodeMap.putIfAbsent(
          hierarchy.getToEmployeeId(), new EmployeeNode(hierarchy.getToEmployeeId()));

      EmployeeNode supervisorNode = employeeNodeMap.get(hierarchy.getFromEmployeeId());
      EmployeeNode subordinateNode = employeeNodeMap.get(hierarchy.getToEmployeeId());
      supervisorNode.getChildren().add(subordinateNode);
    }

    return employeeNodeMap.get(rootEmployeeId);
  }

  /**
   * Checks if an employee with the given id exists in the hierarchy.
   *
   * @param employeeId the id of the employee
   * @return true if the employee exists, false otherwise
   */
  public boolean employeeExists(Long employeeId) {
    return companyEmployeesService.verifyIfEmployeeInCompany(Math.toIntExact(employeeId));
  }

  /**
   * Adds a supervisor-employee edge.
   *
   * @param supervisorId the ID of the supervisor
   * @param employeeId the ID of the employee
   * @return true if the edge was added successfully, false otherwise
   */
  public boolean addEmployeeSupervisorEdge(Long supervisorId, Long employeeId) {
    Long existingSupervisor = employeeHierarchyRepository.findSupervisorByEmployeeId(employeeId);
    if (existingSupervisor != null) {
      System.out.println("Employee " + employeeId + " already has a supervisor.");
      return false;
    }

    if (createsCycleUsingSubtree(supervisorId, employeeId)
        || createsCycleUsingSubtree(employeeId, supervisorId)) {
      System.out.println("Adding this edge would create a cycle.");
      return false;
    }

    EmployeeHierarchy newEdge = new EmployeeHierarchy(supervisorId, employeeId);
    employeeHierarchyRepository.save(newEdge);
    System.out.println("Edge added: Supervisor " + supervisorId + " -> Employee " + employeeId);
    return true;
  }

  /**
   * Removes the supervisor-employee edge for the given employee.
   *
   * @param employeeId the ID of the employee whose supervisor edge is to be removed
   * @return true if the edge was removed successfully, false if the employee has no supervisor
   */
  public boolean removeEmployeeSupervisorEdge(Long employeeId) {
    Long supervisorId = employeeHierarchyRepository.findSupervisorByEmployeeId(employeeId);
    if (supervisorId == null) {
      System.out.println("Employee " + employeeId + " does not have a supervisor.");
      return false;
    }

    List<EmployeeHierarchy> edges = employeeHierarchyRepository.findByFromEmployeeId(supervisorId);
    for (EmployeeHierarchy edge : edges) {
      if (edge.getToEmployeeId().equals(employeeId)) {
        employeeHierarchyRepository.delete(edge);
        System.out.println(
            "Edge removed: Supervisor " + supervisorId + " -> Employee " + employeeId);
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if adding a supervisor-employee edge from the given supervisor to the given employee
   * would create a cycle in the hierarchy.
   *
   * @param supervisorId the ID of the supervisor
   * @param employeeId the ID of the employee
   * @return true if adding the edge would create a cycle, false otherwise
   */
  private boolean createsCycleUsingSubtree(Long supervisorId, Long employeeId) {
    List<EmployeeHierarchy> supervisorSubtree =
        employeeHierarchyRepository.findSubtreeByEmployeeId(supervisorId);
    for (EmployeeHierarchy hierarchy : supervisorSubtree) {
      if (hierarchy.getToEmployeeId().equals(employeeId)) {
        return true;
      }
    }
    return false;
  }
}
