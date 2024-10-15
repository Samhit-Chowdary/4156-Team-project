package com.nullterminators.project.repository;

import com.nullterminators.project.model.Payroll;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PayrollRepository extends JpaRepository<Payroll, Integer> {
    @Query(value = "SELECT * FROM Payroll p WHERE p.employee_id = ?1 AND p.paid = 1 ORDER BY p.payment_date DESC", nativeQuery = true)
    List<Payroll> findAllByEmployeeIdOrderByPaymentDateDesc(Integer employeeId);

    @Query(value = "SELECT * FROM Payroll p WHERE p.employee_id = ?1 AND EXTRACT(MONTH FROM p.payment_date) = ?2 AND EXTRACT(YEAR FROM p.payment_date) = ?3", nativeQuery = true)
    Payroll findByEmployeeIdOrderByPaymentMonthAndYear(Integer employeeId, Integer paymentMonth , Integer paymentYear);
}
