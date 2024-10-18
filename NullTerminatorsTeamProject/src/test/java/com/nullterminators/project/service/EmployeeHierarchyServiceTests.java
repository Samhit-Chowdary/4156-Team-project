package com.nullterminators.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nullterminators.project.model.EmployeeHierarchy;
import com.nullterminators.project.model.EmployeeNode;
import com.nullterminators.project.repository.EmployeeHierarchyRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** JUnit tests for {@link EmployeeHierarchyService}. */
class EmployeeHierarchyServiceTests {

  @Mock private EmployeeHierarchyRepository employeeHierarchyRepository;

  @InjectMocks private EmployeeHierarchyService employeeHierarchyService;

  /**
   * Initializes the mock objects before each test.
   *
   * <p>This method is annotated with {@link BeforeEach} and is called before each test. It uses
   * MockitoAnnotations to initialize the mocks.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); // Initialize mocks
  }

  /**
   * Tests that the service can find all subordinates of an employee given their id.
   *
   * <p>This test sets up a mock hierarchy of employees and verifies that the service can find the
   * subordinates of a given employee.
   *
   * <p>The test calls the service method with the id of a supervisor and asserts that the result
   * contains exactly two subordinates, which are the ones that are directly subordinate to the
   * given supervisor.
   */
  @Test
  void testGetSubordinates() {
    Long supervisorId = 1L;

    List<EmployeeHierarchy> mockSubordinates =
        Arrays.asList(
            new EmployeeHierarchy(supervisorId, 2L), new EmployeeHierarchy(supervisorId, 3L));
    when(employeeHierarchyRepository.findByFromEmployeeId(supervisorId))
        .thenReturn(mockSubordinates);

    List<EmployeeHierarchy> result = employeeHierarchyService.getSubordinates(supervisorId);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(2L, result.get(0).getToEmployeeId());
    assertEquals(3L, result.get(1).getToEmployeeId());

    verify(employeeHierarchyRepository, times(1)).findByFromEmployeeId(supervisorId);
  }

  /**
   * Tests that the service can build a tree structure given a root employee id.
   *
   * <p>This test sets up a mock hierarchy of employees and verifies that the service can build the
   * correct tree structure.
   *
   * <p>The hierarchy is as follows:
   *
   * <pre>
   * Employee 1
   *   - Employee 2
   *     - Employee 4
   *     - Employee 5
   *   - Employee 3
   *     - Employee 6
   * </pre>
   */
  @Test
  void testBuildEmployeeTreeStructure() {
    List<EmployeeHierarchy> mockSubordinates =
        Arrays.asList(
            new EmployeeHierarchy(1L, 2L),
            new EmployeeHierarchy(1L, 3L),
            new EmployeeHierarchy(2L, 4L),
            new EmployeeHierarchy(2L, 5L),
            new EmployeeHierarchy(3L, 6L));

    when(employeeHierarchyRepository.findSubtreeByEmployeeId(1L)).thenReturn(mockSubordinates);

    EmployeeNode rootNode = employeeHierarchyService.buildEmployeeTree(1L);

    assertNotNull(rootNode);
    assertEquals(1L, rootNode.getId());

    assertEquals(2, rootNode.getChildren().size());

    EmployeeNode employee2 = rootNode.getChildren().get(0);
    assertEquals(2L, employee2.getId());
    assertEquals(2, employee2.getChildren().size());

    EmployeeNode employee4 = employee2.getChildren().get(0);
    EmployeeNode employee5 = employee2.getChildren().get(1);
    assertEquals(4L, employee4.getId());
    assertEquals(5L, employee5.getId());
    assertEquals(0, employee4.getChildren().size());
    assertEquals(0, employee5.getChildren().size());

    EmployeeNode employee3 = rootNode.getChildren().get(1);
    assertEquals(3L, employee3.getId());
    assertEquals(1, employee3.getChildren().size());

    EmployeeNode employee6 = employee3.getChildren().get(0);
    assertEquals(6L, employee6.getId());
    assertEquals(0, employee6.getChildren().size());
  }

  /**
   * Tests that {@link EmployeeHierarchyService#getSupervisor(Long)} returns the supervisor of the
   * given employee id.
   *
   * <p>This test mocks the call to {@link EmployeeHierarchyRepository#findSupervisorByEmployeeId}
   * to return the expected supervisor id. It then calls {@link
   * EmployeeHierarchyService#getSupervisor(Long)} and asserts that the result is not null and
   * matches the expected supervisor id. Finally, it verifies that the repository method was called
   * with the given employee id.
   */
  @Test
  void testGetSupervisor() {
    Long toEmployeeId = 2L;
    Long expectedSupervisorId = 1L;

    when(employeeHierarchyRepository.findSupervisorByEmployeeId(toEmployeeId))
        .thenReturn(expectedSupervisorId);

    Long result = employeeHierarchyService.getSupervisor(toEmployeeId);

    assertNotNull(result);
    assertEquals(expectedSupervisorId, result);

    verify(employeeHierarchyRepository, times(1)).findSupervisorByEmployeeId(toEmployeeId);
  }

  @Test
  void testAddEdgeEmployeeAlreadyHasSupervisor() {
    Long supervisorId = 1L;
    Long employeeId = 2L;

    when(employeeHierarchyRepository.findSupervisorByEmployeeId(employeeId)).thenReturn(3L);

    boolean added = employeeHierarchyService.addEmployeeSupervisorEdge(supervisorId, employeeId);

    assertFalse(added); // Should reject adding the edge
    verify(employeeHierarchyRepository, never()).save(any());
  }

  @Test
  void testAddEdgeCreatesCycle() {
    Long supervisorId = 1L;
    Long employeeId = 2L;

    when(employeeHierarchyRepository.existsByEmployeeId(supervisorId)).thenReturn(true);
    when(employeeHierarchyRepository.existsByEmployeeId(employeeId)).thenReturn(true);
    when(employeeHierarchyRepository.findSupervisorByEmployeeId(employeeId)).thenReturn(null);

    List<EmployeeHierarchy> mockSubtree = Arrays.asList(new EmployeeHierarchy(1L, employeeId));
    when(employeeHierarchyRepository.findSubtreeByEmployeeId(supervisorId)).thenReturn(mockSubtree);

    boolean added = employeeHierarchyService.addEmployeeSupervisorEdge(supervisorId, employeeId);

    assertFalse(added);
    verify(employeeHierarchyRepository, never()).save(any());
  }

  @Test
  void testAddEdgeSuccess() {
    Long supervisorId = 1L;
    Long employeeId = 2L;

    when(employeeHierarchyRepository.existsByEmployeeId(supervisorId)).thenReturn(true);
    when(employeeHierarchyRepository.existsByEmployeeId(employeeId)).thenReturn(true);
    when(employeeHierarchyRepository.findSupervisorByEmployeeId(employeeId)).thenReturn(null);

    List<EmployeeHierarchy> mockSubtree = Collections.emptyList();
    when(employeeHierarchyRepository.findSubtreeByEmployeeId(supervisorId)).thenReturn(mockSubtree);

    boolean added = employeeHierarchyService.addEmployeeSupervisorEdge(supervisorId, employeeId);

    assertTrue(added);
    verify(employeeHierarchyRepository, times(1)).save(any(EmployeeHierarchy.class));
  }

  @Test
  void testRemoveEdgeNoSupervisorFound() {
    Long employeeId = 2L;

    when(employeeHierarchyRepository.existsByEmployeeId(employeeId)).thenReturn(true);
    when(employeeHierarchyRepository.findSupervisorByEmployeeId(employeeId)).thenReturn(null);

    boolean removed = employeeHierarchyService.removeEmployeeSupervisorEdge(employeeId);

    assertFalse(removed);
    verify(employeeHierarchyRepository, never()).delete(any());
  }

  @Test
  void testRemoveEdgeEdgeNotFound() {
    Long employeeId = 2L;
    Long supervisorId = 1L;

    when(employeeHierarchyRepository.existsByEmployeeId(employeeId)).thenReturn(true);
    when(employeeHierarchyRepository.findSupervisorByEmployeeId(employeeId))
        .thenReturn(supervisorId);

    when(employeeHierarchyRepository.findByFromEmployeeId(supervisorId))
        .thenReturn(Collections.emptyList());

    boolean removed = employeeHierarchyService.removeEmployeeSupervisorEdge(employeeId);

    assertFalse(removed);
    verify(employeeHierarchyRepository, never()).delete(any());
  }

  @Test
  void testRemoveEdgeSuccess() {
    Long employeeId = 2L;
    Long supervisorId = 1L;

    when(employeeHierarchyRepository.existsByEmployeeId(employeeId)).thenReturn(true);
    when(employeeHierarchyRepository.findSupervisorByEmployeeId(employeeId))
        .thenReturn(supervisorId);

    List<EmployeeHierarchy> mockEdges =
        Arrays.asList(new EmployeeHierarchy(supervisorId, employeeId));
    when(employeeHierarchyRepository.findByFromEmployeeId(supervisorId)).thenReturn(mockEdges);

    boolean removed = employeeHierarchyService.removeEmployeeSupervisorEdge(employeeId);

    assertTrue(removed);
    verify(employeeHierarchyRepository, times(1)).delete(any(EmployeeHierarchy.class));
  }
}
