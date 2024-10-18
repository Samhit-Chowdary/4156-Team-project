package com.nullterminators.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nullterminators.project.model.EmployeeHierarchy;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/** JUnit tests for {@link EmployeeHierarchyRepository}. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EmployeeHierarchyRepositoryTests {

  @Autowired private EmployeeHierarchyRepository employeeHierarchyRepository;

  /**
   * Test for {@link EmployeeHierarchyRepository#findByFromEmployeeId(Long)}.
   *
   * <p>This test creates two {@link EmployeeHierarchy} objects, saves them to the repository, and
   * then calls {@link EmployeeHierarchyRepository#findByFromEmployeeId(Long)} to retrieve the
   * subordinates of the first employee. The test asserts that the result is not null and has a size
   * of 2.
   */
  @Test
  void testFindByFromEmployeeId() {
    EmployeeHierarchy employee1 = new EmployeeHierarchy(1L, 2L);
    EmployeeHierarchy employee2 = new EmployeeHierarchy(1L, 3L);
    employeeHierarchyRepository.save(employee1);
    employeeHierarchyRepository.save(employee2);

    List<EmployeeHierarchy> subordinates = employeeHierarchyRepository.findByFromEmployeeId(1L);

    assertNotNull(subordinates);
    assertEquals(2, subordinates.size());
  }

  /**
   * Test for {@link EmployeeHierarchyRepository#findSupervisorByEmployeeId(Long)}.
   *
   * <p>This test creates an {@link EmployeeHierarchy} object, saves it to the repository, and then
   * calls {@link EmployeeHierarchyRepository#findSupervisorByEmployeeId(Long)} to retrieve the
   * supervisor of the second employee. The test asserts that the result is not null and matches the
   * supervisor id.
   */
  @Test
  void testFindSupervisorByEmployeeId() {
    EmployeeHierarchy employee = new EmployeeHierarchy(1L, 2L);
    employeeHierarchyRepository.save(employee);

    Long supervisorId = employeeHierarchyRepository.findSupervisorByEmployeeId(2L);

    assertNotNull(supervisorId);
    assertEquals(1L, supervisorId);
  }
}
