package com.nullterminators.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.nullterminators.project.model.EmployeeHierarchy;
import com.nullterminators.project.model.EmployeeNode;
import com.nullterminators.project.service.EmployeeHierarchyService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** JUnit tests for {@link EmployeeHierarchyController}. */
class EmployeeHierarchyControllerTests {

  @Mock private EmployeeHierarchyService employeeHierarchyService;

  @InjectMocks private EmployeeHierarchyController employeeHierarchyController;

  /**
   * Sets up the test environment before each test.
   *
   * <p>This method uses MockitoAnnotations to initialize the mocks. It is called before each test
   * via the {@link BeforeEach} annotation.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); // Initialize mocks
  }

  /**
   * Test for {@link EmployeeHierarchyController#getSubordinates(Long)}.
   *
   * <p>This test calls {@link EmployeeHierarchyController#getSubordinates(Long)} with a valid
   * employee id and asserts that the response has a status code of 200 OK and a body containing a
   * list of two subordinates of the given employee.
   */
  @Test
  void testGetSubordinatesSuccess() {
    Long fromEmployeeId = 1L;
    List<EmployeeHierarchy> mockSubordinates =
        Arrays.asList(new EmployeeHierarchy(1L, 2L), new EmployeeHierarchy(1L, 3L));

    when(employeeHierarchyService.employeeExists(fromEmployeeId)).thenReturn(true);
    when(employeeHierarchyService.getSubordinates(fromEmployeeId)).thenReturn(mockSubordinates);

    ResponseEntity<?> response = employeeHierarchyController.getSubordinates(fromEmployeeId);

    List<EmployeeHierarchy> responseBody = (List<EmployeeHierarchy>) response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(responseBody);
    assertEquals(2, responseBody.size());
    assertEquals(2L, responseBody.get(0).getToEmployeeId());
    assertEquals(3L, responseBody.get(1).getToEmployeeId());
  }

  /**
   * Test for {@link EmployeeHierarchyController#getSubordinates(Long)} when the requested employee
   * does not exist.
   *
   * <p>This test calls {@link EmployeeHierarchyController#getSubordinates(Long)} with a
   * non-existent employee id and asserts that the response has a status code of 404 NOT FOUND and a
   * body containing an error message.
   */
  @Test
  void testGetSubordinatesEmployeeNotFound() {
    Long fromEmployeeId = 999L;

    when(employeeHierarchyService.employeeExists(fromEmployeeId)).thenReturn(false);

    ResponseEntity<?> response = employeeHierarchyController.getSubordinates(fromEmployeeId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Employee with ID " + fromEmployeeId + " not found", response.getBody());
  }

  /**
   * Test for {@link EmployeeHierarchyController#getSupervisorByEmployeeId(Long)} when the requested
   * employee exists and has a supervisor.
   *
   * <p>This test calls {@link EmployeeHierarchyController#getSupervisorByEmployeeId(Long)} with an
   * existing employee id and asserts that the response has a status code of 200 OK and a body
   * containing the supervisor id.
   */
  @Test
  void testGetSupervisorByEmployeeIdSuccess() {
    Long toEmployeeId = 2L;
    Long expectedSupervisorId = 1L;

    when(employeeHierarchyService.employeeExists(toEmployeeId)).thenReturn(true);
    when(employeeHierarchyService.getSupervisor(toEmployeeId)).thenReturn(expectedSupervisorId);

    ResponseEntity<?> response =
        employeeHierarchyController.getSupervisorByEmployeeId(toEmployeeId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedSupervisorId, response.getBody());
  }

  /**
   * Test for {@link EmployeeHierarchyController#getSupervisorByEmployeeId(Long)} when the requested
   * employee does not exist.
   *
   * <p>This test calls {@link EmployeeHierarchyController#getSupervisorByEmployeeId(Long)} with a
   * non-existent employee id and asserts that the response has a status code of 404 NOT FOUND and a
   * body containing an error message.
   */
  @Test
  void testGetSupervisorByEmployeeIdEmployeeNotFound() {
    Long toEmployeeId = 999L;

    when(employeeHierarchyService.employeeExists(toEmployeeId)).thenReturn(false);

    ResponseEntity<?> response =
        employeeHierarchyController.getSupervisorByEmployeeId(toEmployeeId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Employee with ID " + toEmployeeId + " not found", response.getBody());
  }

  /**
   * Test for {@link EmployeeHierarchyController#getSupervisorByEmployeeId(Long)} when the requested
   * employee exists but has no supervisor.
   *
   * <p>This test calls {@link EmployeeHierarchyController#getSupervisorByEmployeeId(Long)} with an
   * existing employee id and asserts that the response has a status code of 400 BAD REQUEST and a
   * body containing an error message.
   */
  @Test
  void testGetSupervisorByEmployeeIdSupervisorNotFound() {
    Long toEmployeeId = 2L;

    when(employeeHierarchyService.employeeExists(toEmployeeId)).thenReturn(true);
    when(employeeHierarchyService.getSupervisor(toEmployeeId)).thenReturn(null);

    ResponseEntity<?> response =
        employeeHierarchyController.getSupervisorByEmployeeId(toEmployeeId);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(
        "Supervisor for Employee with ID " + toEmployeeId + " doesn't exist", response.getBody());
  }

  /**
   * Test for {@link EmployeeHierarchyController#getSubtree(Long)} when the requested employee
   * exists and has subordinates.
   *
   * <p>This test calls {@link EmployeeHierarchyController#getSubtree(Long)} with an existing
   * employee id, mocks the {@link EmployeeHierarchyService#buildEmployeeTree(Long)} to return a
   * mock {@link EmployeeNode} with two children, and asserts that the response has a status code of
   * 200 OK and a body containing the subtree of the given employee.
   */
  @Test
  void testGetSubtreeSuccess() {
    Long employeeId = 1L;
    EmployeeNode mockEmployeeNode = new EmployeeNode(1L);
    mockEmployeeNode.getChildren().add(new EmployeeNode(2L));
    mockEmployeeNode.getChildren().add(new EmployeeNode(3L));

    when(employeeHierarchyService.employeeExists(employeeId)).thenReturn(true);
    when(employeeHierarchyService.buildEmployeeTree(employeeId)).thenReturn(mockEmployeeNode);

    ResponseEntity<?> response = employeeHierarchyController.getSubtree(employeeId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    EmployeeNode responseBody = (EmployeeNode) response.getBody();
    assertNotNull(responseBody);
    assertEquals(1L, responseBody.getId());
    assertEquals(2, responseBody.getChildren().size());
  }

  /**
   * Test for {@link EmployeeHierarchyController#getSubtree(Long)} when the requested employee does
   * not exist.
   *
   * <p>This test calls {@link EmployeeHierarchyController#getSubtree(Long)} with a non-existent
   * employee id and asserts that the response has a status code of 404 NOT FOUND and a body
   * containing an error message.
   */
  @Test
  void testGetSubtreeEmployeeNotFound() {
    Long employeeId = 999L;

    when(employeeHierarchyService.employeeExists(employeeId)).thenReturn(false);

    ResponseEntity<?> response = employeeHierarchyController.getSubtree(employeeId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Employee with ID " + employeeId + " not found", response.getBody());
  }

  @Test
  void testAddEdgeEmployeeOrSupervisorNotFound() {
    Long supervisorId = 1L;
    Long employeeId = 2L;

    when(employeeHierarchyService.employeeExists(supervisorId)).thenReturn(false);
    when(employeeHierarchyService.employeeExists(employeeId)).thenReturn(true);

    ResponseEntity<?> response =
        employeeHierarchyController.addEmployeeSupervisorEdge(supervisorId, employeeId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Either supervisor or employee doesn't exist.", response.getBody());
  }

  @Test
  void testAddEdgeSuccess() {
    Long supervisorId = 1L;
    Long employeeId = 2L;

    when(employeeHierarchyService.employeeExists(supervisorId)).thenReturn(true);
    when(employeeHierarchyService.employeeExists(employeeId)).thenReturn(true);
    when(employeeHierarchyService.addEmployeeSupervisorEdge(supervisorId, employeeId))
        .thenReturn(true);

    ResponseEntity<?> response =
        employeeHierarchyController.addEmployeeSupervisorEdge(supervisorId, employeeId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Edge added successfully.", response.getBody());
  }

  @Test
  void testAddEdgeCreatesCycle() {
    Long supervisorId = 1L;
    Long employeeId = 2L;

    when(employeeHierarchyService.employeeExists(supervisorId)).thenReturn(true);
    when(employeeHierarchyService.employeeExists(employeeId)).thenReturn(true);
    when(employeeHierarchyService.addEmployeeSupervisorEdge(supervisorId, employeeId))
        .thenReturn(false); // Cycle detected

    ResponseEntity<?> response =
        employeeHierarchyController.addEmployeeSupervisorEdge(supervisorId, employeeId);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(
        "Operation failed: Employee already has a supervisor, "
            + "or adding this edge would create a cycle.",
        response.getBody());
  }

  @Test
  void testRemoveEdgeEmployeeNotFound() {
    Long employeeId = 2L;

    when(employeeHierarchyService.employeeExists(employeeId)).thenReturn(false);

    ResponseEntity<?> response =
        employeeHierarchyController.removeEmployeeSupervisorEdge(employeeId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Employee with ID " + employeeId + " not found.", response.getBody());
  }

  @Test
  void testRemoveEdgeSuccess() {
    Long employeeId = 2L;

    when(employeeHierarchyService.employeeExists(employeeId)).thenReturn(true);
    when(employeeHierarchyService.removeEmployeeSupervisorEdge(employeeId)).thenReturn(true);

    ResponseEntity<?> response =
        employeeHierarchyController.removeEmployeeSupervisorEdge(employeeId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Edge removed successfully.", response.getBody());
  }

  @Test
  void testRemoveEdgeNoSupervisorFound() {
    Long employeeId = 2L;

    when(employeeHierarchyService.employeeExists(employeeId)).thenReturn(true);
    when(employeeHierarchyService.removeEmployeeSupervisorEdge(employeeId))
        .thenReturn(false); // No supervisor found

    ResponseEntity<?> response =
        employeeHierarchyController.removeEmployeeSupervisorEdge(employeeId);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Operation failed: Employee has no supervisor.", response.getBody());
  }
}
