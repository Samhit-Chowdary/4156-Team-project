package com.nullterminators.project.integration.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.nullterminators.project.controller.EmployeeHierarchyController;
import com.nullterminators.project.model.CompanyEmployees;
import com.nullterminators.project.model.EmployeeHierarchy;
import com.nullterminators.project.repository.CompanyEmployeesRepository;
import com.nullterminators.project.repository.EmployeeHierarchyRepository;
import com.nullterminators.project.service.CompanyService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Integration tests for {@link EmployeeHierarchyController} to verify that it correctly integrates
 * with its dependencies.
 */
@SpringBootTest
class EmployeeHierarchyInternalIntegrationTests {

  @Autowired private EmployeeHierarchyController employeeHierarchyController;

  @MockBean private EmployeeHierarchyRepository employeeHierarchyRepository;
  @MockBean private CompanyEmployeesRepository companyEmployeesRepository;
  @MockBean private CompanyService companyService;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("testUser", null));

    when(companyService.getCompanyUsername()).thenReturn("testCompany");

    CompanyEmployees mockEmployee = new CompanyEmployees();
    mockEmployee.setId(1);
    mockEmployee.setCompanyUsername("testCompany");
    mockEmployee.setEmployeeId(1);

    CompanyEmployees mockEmployee2 = new CompanyEmployees();
    mockEmployee2.setId(2);
    mockEmployee2.setCompanyUsername("testCompany");
    mockEmployee2.setEmployeeId(2);

    when(companyEmployeesRepository.findAllByCompanyUsernameAndEmployeeId("testCompany", 1))
        .thenReturn(List.of(mockEmployee));
    when(companyEmployeesRepository.findAllByCompanyUsernameAndEmployeeId("testCompany", 2))
        .thenReturn(List.of(mockEmployee2));
    when(companyEmployeesRepository.findAllByCompanyUsernameAndEmployeeId("testCompany", 999))
        .thenReturn(Collections.emptyList());

    when(employeeHierarchyRepository.existsByEmployeeId(1L)).thenReturn(true);
    when(employeeHierarchyRepository.existsByEmployeeId(2L)).thenReturn(true);
    when(employeeHierarchyRepository.existsByEmployeeId(999L)).thenReturn(false);
  }

  @Test
  void testGetSubordinatesSuccess() {
    Long supervisorId = 1L;
    List<EmployeeHierarchy> mockSubordinates =
        List.of(new EmployeeHierarchy(1L, 2L), new EmployeeHierarchy(1L, 3L));
    when(employeeHierarchyRepository.findByFromEmployeeId(supervisorId))
        .thenReturn(mockSubordinates);

    ResponseEntity<?> response = employeeHierarchyController.getSubordinates(supervisorId);

    assertEquals(200, response.getStatusCodeValue());
    @SuppressWarnings("unchecked")
    List<EmployeeHierarchy> subordinates = (List<EmployeeHierarchy>) response.getBody();
    assertNotNull(subordinates);
    assertEquals(2, subordinates.size());
  }

  @Test
  void testAddEmployeeSupervisorEdgeSuccess() {
    Long supervisorId = 1L;
    Long employeeId = 2L;

    when(employeeHierarchyRepository.findSupervisorByEmployeeId(employeeId)).thenReturn(null);
    when(employeeHierarchyRepository.findSubtreeByEmployeeId(supervisorId))
        .thenReturn(Collections.emptyList());

    ResponseEntity<?> response =
        employeeHierarchyController.addEmployeeSupervisorEdge(supervisorId, employeeId);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals("Edge added successfully.", response.getBody());
  }

  @Test
  void testAddEmployeeSupervisorEdgeFailureCycle() {
    Long supervisorId = 1L;
    Long employeeId = 2L;

    when(employeeHierarchyRepository.findSupervisorByEmployeeId(supervisorId)).thenReturn(null);
    when(employeeHierarchyRepository.findSubtreeByEmployeeId(employeeId))
        .thenReturn(List.of(new EmployeeHierarchy(2L, 1L))); // Simulate a cycle

    ResponseEntity<?> response =
        employeeHierarchyController.addEmployeeSupervisorEdge(supervisorId, employeeId);

    assertEquals(400, response.getStatusCodeValue());
    assertEquals(
        "Operation failed: Employee already has a supervisor, or adding this edge would"
            + " create a cycle.",
        response.getBody());
  }


  @Test
  void testGetSubordinatesFailure() {
    ResponseEntity<?> response = employeeHierarchyController.getSubordinates(999L);

    assertEquals(404, response.getStatusCodeValue());
    assertEquals("Employee with ID 999 not found", response.getBody());
  }
}
