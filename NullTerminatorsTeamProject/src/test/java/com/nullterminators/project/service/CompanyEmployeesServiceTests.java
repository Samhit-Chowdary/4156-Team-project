package com.nullterminators.project.service;

import com.nullterminators.project.model.CompanyEmployees;
import com.nullterminators.project.repository.CompanyEmployeesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration
public class CompanyEmployeesServiceTests {

    private CompanyEmployeesService companyEmployeesService;

    @Mock
    private CompanyService companyService;

    @Mock
    private CompanyEmployeesRepository companyEmployeesRepository;

    @BeforeEach
    void setUp() {
        companyEmployeesService = new CompanyEmployeesService(companyEmployeesRepository, companyService);
    }

    @Test
    void testVerifyIfEmployeeInCompanySuccess() {
        when(companyService.getCompanyUsername()).thenReturn("company");
        when(companyEmployeesRepository.findAllByCompanyUsernameAndEmployeeId("company", 1)).thenReturn(List.of(new CompanyEmployees()));
        boolean result = companyEmployeesService.verifyIfEmployeeInCompany(1);
        assertTrue(result);
    }

    @Test
    void testVerifyIfEmployeeInCompanyFailure() {
        when(companyService.getCompanyUsername()).thenReturn("company");
        when(companyEmployeesRepository.findAllByCompanyUsernameAndEmployeeId("company", 1)).thenReturn(List.of());
        boolean result = companyEmployeesService.verifyIfEmployeeInCompany(1);
        assertFalse(result);
    }

    @Test
    void testAddEmployeeToCompanySuccess() {
        when(companyService.getCompanyUsername()).thenReturn("company");
        when(companyEmployeesRepository.findAllByEmployeeId(1)).thenReturn(List.of());
        boolean result = companyEmployeesService.addEmployeeToCompany(1);
        assertTrue(result);
    }

    @Test
    void testAddEmployeeToCompanyEmployeeExists() {
        when(companyService.getCompanyUsername()).thenReturn("company");
        when(companyEmployeesRepository.findAllByEmployeeId(1)).thenReturn(List.of(new CompanyEmployees()));
        boolean result = companyEmployeesService.addEmployeeToCompany(1);
        assertFalse(result);
    }

    @Test
    void testGetAllEmployeesInCompany() {
        when(companyService.getCompanyUsername()).thenReturn("company");
        CompanyEmployees companyEmployees = new CompanyEmployees();
        companyEmployees.setEmployeeId(1);
        when(companyEmployeesRepository.findAllByCompanyUsername("company")).thenReturn(List.of(companyEmployees));
        List<Integer> result = companyEmployeesService.getAllEmployeesInCompany();
        assertEquals(List.of(1), result);
        assertTrue(result.contains(1));
    }
}
