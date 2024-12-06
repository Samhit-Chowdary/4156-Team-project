package com.nullterminators.project.integration.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nullterminators.project.enums.LeaveStatus;
import com.nullterminators.project.enums.LeaveType;
import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.TimeOff;
import com.nullterminators.project.repository.EmployeeProfileRepository;
import com.nullterminators.project.repository.TimeOffRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** External integration tests for the TimeOff module. */
@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class TimeOffExternalIntegrationTests {

  @Autowired private TimeOffRepository timeOffRepository;
  @Autowired private EmployeeProfileRepository employeeProfileRepository;

  @BeforeEach
  public void setUp() {
    timeOffRepository.deleteAll();
    employeeProfileRepository.deleteAll();
  }

  private Integer createEmployeeProfile() {
    EmployeeProfile employeeProfile = new EmployeeProfile();
    employeeProfile.setName("testUser");
    employeeProfile.setPhoneNumber("9999999999");
    employeeProfile.setGender("male");
    employeeProfile.setAge(20);
    employeeProfile.setStartDate(LocalDate.now());
    employeeProfile.setDesignation("tester");
    employeeProfile.setEmail("testUser@nullterminators");
    employeeProfile.setBaseSalary(100000);
    employeeProfile.setEmergencyContactNumber("9999999999");
    employeeProfileRepository.save(employeeProfile);

    return employeeProfile.getId();
  }

  private Integer createTimeOff(
      Integer employeeId,
      LocalDate startDate,
      LocalDate endDate,
      String leaveType,
      String leaveStatus,
      String reason) {
    TimeOff timeOff = new TimeOff();
    timeOff.setEmployeeId(employeeId);
    timeOff.setStartDate(startDate);
    timeOff.setEndDate(endDate);
    timeOff.setLeaveType(LeaveType.valueOf(leaveType));
    timeOff.setStatus(LeaveStatus.valueOf(leaveStatus));
    timeOff.setReason(reason);
    timeOff.setRequestDate(LocalDate.now());
    timeOffRepository.save(timeOff);

    return timeOff.getId();
  }

  @Test
  public void testFindAllByEmployeeIdGivenDateRange() {
    Integer empId = createEmployeeProfile();
    createTimeOff(
        empId, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10), "SICK", "PENDING", "Flu");
    createTimeOff(
        empId,
        LocalDate.of(2024, 2, 1),
        LocalDate.of(2024, 2, 5),
        "MATERNITY",
        "APPROVED",
        "Vacation");
    createTimeOff(
        empId, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 3), "SICK", "REJECTED", "Cold");

    List<TimeOff> result =
        timeOffRepository.findAllByEmployeeIdGivenDateRange(
            empId, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 28));

    assertEquals(2, result.size());
    assertTrue(result.get(0).getStartDate().isBefore(result.get(1).getStartDate()));
  }

  @Test
  public void testFindAllByEmployeeIdOrderByStartDateDesc() {
    Integer empId = createEmployeeProfile();
    createTimeOff(
        empId, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10), "SICK", "PENDING", "Flu");
    createTimeOff(
        empId,
        LocalDate.of(2024, 2, 1),
        LocalDate.of(2024, 2, 5),
        "MATERNITY",
        "APPROVED",
        "Vacation");
    createTimeOff(
        empId, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 3), "SICK", "REJECTED", "Cold");

    List<TimeOff> result = timeOffRepository.findAllByEmployeeIdOrderByStartDateDesc(empId);

    assertEquals(3, result.size());
    assertTrue(result.get(0).getStartDate().isAfter(result.get(1).getStartDate()));
  }

  @Test
  public void testDeleteByEmployeeIdAndTimeOffId() {
    Integer empId = createEmployeeProfile();
    Integer timeOffId =
        createTimeOff(
            empId, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10), "SICK", "PENDING", "Flu");

    int rowsDeleted = timeOffRepository.deleteByEmployeeIdAndTimeOffId(empId, timeOffId);

    assertEquals(1, rowsDeleted);
    List<TimeOff> result = timeOffRepository.findAllByEmployeeIdOrderByStartDateDesc(empId);
    assertEquals(0, result.size());
  }
}
