package com.nullterminators.project.repository;

import com.nullterminators.project.model.EmployeeHierarchy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for EmployeeHierarchy model. It provides methods to query the
 * EmployeeHierarchy table.
 */
@Repository
public interface EmployeeHierarchyRepository extends JpaRepository<EmployeeHierarchy, Long> {

  List<EmployeeHierarchy> findByFromEmployeeId(Long fromEmployeeId);

  /**
   * Finds the supervisor of the employee with the given id.
   *
   * @param toEmployeeId the id of the employee
   * @return the id of the supervisor
   */
  @Query("SELECT e.fromEmployeeId FROM EmployeeHierarchy e WHERE e.toEmployeeId = :toEmployeeId")
  Long findSupervisorByEmployeeId(@Param("toEmployeeId") Long toEmployeeId);

  @Query(
      value =
          "WITH RECURSIVE EmployeeSubtree AS ("
              + "   SELECT from_employee_id, to_employee_id "
              + "   FROM employee_hierarchy "
              + "   WHERE from_employee_id = :employeeId "
              + "   UNION ALL "
              + "   SELECT eh.from_employee_id, eh.to_employee_id "
              + "   FROM employee_hierarchy eh "
              + "   INNER JOIN EmployeeSubtree es ON eh.from_employee_id = es.to_employee_id "
              + ") "
              + "SELECT ROW_NUMBER() OVER() AS id, from_employee_id, to_employee_id "
              + "FROM EmployeeSubtree",
      nativeQuery = true)
  List<EmployeeHierarchy> findSubtreeByEmployeeId(@Param("employeeId") Long employeeId);

  /**
   * Checks if there is a record in the EmployeeHierarchy table with the given employee id in either
   * the from_employee_id or to_employee_id column.
   *
   * @param employeeId the id of the employee
   * @return true if a record exists, false otherwise
   */
  @Query(
      "SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM EmployeeHierarchy"
          + " e WHERE e.fromEmployeeId = :employeeId OR e.toEmployeeId = :employeeId")
  boolean existsByEmployeeId(@Param("employeeId") Long employeeId);
}
