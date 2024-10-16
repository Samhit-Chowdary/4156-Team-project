package com.nullterminators.project.repository;

import com.nullterminators.project.model.TimeOff;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for storing timeoff records DB.
 */
public interface TimeOffRepository extends JpaRepository<TimeOff, Integer> {

    @Query(value = "SELECT * FROM timeoff t WHERE t.employee_id = ?1 AND t.start_date >= ?2 AND t.end_date <= ?3 ORDER BY t.start_date ASC", nativeQuery = true)
    List<TimeOff> findAllByEmployeeIdGivenDateRange(Integer employeeId, LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT * FROM timeoff t WHERE t.employee_id = ?1 ORDER BY t.start_date DESC", nativeQuery = true)
    List<TimeOff> findAllByEmployeeIdOrderByStartDateDesc(Integer employeeId);
}
