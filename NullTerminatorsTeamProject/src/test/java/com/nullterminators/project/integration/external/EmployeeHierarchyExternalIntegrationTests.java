package com.nullterminators.project.integration.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nullterminators.project.model.EmployeeHierarchy;
import com.nullterminators.project.repository.EmployeeHierarchyRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the EmployeeHierarchyRepository. These tests validate the database
 * persistence and retrieval functionalities for employee hierarchy data.
 *
 * <p>The tests ensure that edges between employees are correctly added to and retrieved from the
 * database, maintaining the integrity of the employee hierarchy.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class EmployeeHierarchyExternalIntegrationTests {

  @Autowired private EmployeeHierarchyRepository employeeHierarchyRepository;

  /** External Integration Test: Database Persistence for Add and Retrieve Edge. */
  @Test
  void testDatabaseIntegrationForAddAndRetrieveEdge() {
    EmployeeHierarchy edge = new EmployeeHierarchy(1L, 2L);

    employeeHierarchyRepository.save(edge);

    List<EmployeeHierarchy> result = employeeHierarchyRepository.findByFromEmployeeId(1L);
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(2L, result.get(0).getToEmployeeId());
  }

  /** External Integration Test: Fetch Supervisor of Employee. */
  @Test
  void testDatabaseIntegrationForSupervisorQuery() {
    employeeHierarchyRepository.save(new EmployeeHierarchy(1L, 2L));
    Long supervisorId = employeeHierarchyRepository.findSupervisorByEmployeeId(2L);

    assertNotNull(supervisorId);
    assertEquals(1L, supervisorId);
  }

  /** External Integration Test: Check Existence of Employee in Hierarchy. */
  @Test
  void testDatabaseIntegrationForEmployeeExistence() {
    employeeHierarchyRepository.save(new EmployeeHierarchy(1L, 2L));
    boolean exists = employeeHierarchyRepository.existsByEmployeeId(1L);

    assertEquals(true, exists);
  }
}
