package com.nullterminators.project.service;

import com.nullterminators.project.enums.PayrollStatus;
import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.PayrollRepository;
import com.nullterminators.project.util.pdf.PdfGenerator;
import com.nullterminators.project.util.pdf.PdfUploader;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

/**
 * Service class for Payroll.
 */
@Service
public class PayrollService {

  private final PayrollRepository payrollRepository;
  private final EmployeeProfileService employeeProfileService;
  private final CompanyEmployeesService companyEmployeesService;
  private final TimeOffService timeOffService;
  private final PdfGenerator pdfGenerator;
  private final PdfUploader pdfUploader;

  /**
   * Constructor for PayrollService.
   *
   * @param payrollRepository : {@link PayrollRepository}
   * @param employeeProfileService : {@link EmployeeProfileService}
   * @param pdfGenerator : {@link PdfGenerator}
   */
  @Autowired
  public PayrollService(PayrollRepository payrollRepository,
                        EmployeeProfileService employeeProfileService,
                        CompanyEmployeesService companyEmployeesService,
                        TimeOffService timeOffService,
                        PdfGenerator pdfGenerator,
                        PdfUploader pdfUploader) {
    this.payrollRepository = payrollRepository;
    this.employeeProfileService = employeeProfileService;
    this.companyEmployeesService = companyEmployeesService;
    this.pdfGenerator = pdfGenerator;
    this.pdfUploader = pdfUploader;
    this.timeOffService = timeOffService;
  }

  /**
   * Get all Payroll entries by employee id.
   *
   * @param employeeId (Integer) : Employee ID
   * @return (List) : List of Payroll entries for the employee
   */
  public Pair<PayrollStatus, List<Map<String, Object>>> getPayrollByEmployeeId(Integer employeeId) {
    List<Map<String, Object>> returnValue = new ArrayList<>();
    Boolean status = companyEmployeesService.verifyIfEmployeeInCompany(employeeId);
    if (!status) {
      return Pair.of(PayrollStatus.EMPLOYEE_NOT_FOUND, returnValue);
    }

    List<Payroll> payrollInformation =
            payrollRepository.findAllByEmployeeIdOrderByPaymentDateDesc(employeeId);

    for (Payroll payroll : payrollInformation) {
      Map<String, Object> data = new HashMap<>();
      data.put("employeeId", payroll.getEmployeeId());
      data.put("date", payroll.getPaymentDate());
      data.put("salary", payroll.getSalary());
      data.put("tax", payroll.getTax());
      data.put("netSalary", payroll.getSalary() - payroll.getTax());
      data.put("payslip", payroll.getPayslip());
      returnValue.add(data);
    }

    return Pair.of(PayrollStatus.OK, returnValue);
  }

  /**
   * Update the paid status of Payroll entry by employee id.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates (Map) : Map consisting of month and year
   * @return (Integer) : Status of update
   */
  public PayrollStatus markAsPaid(Integer employeeId, Map<String, Object> updates) {
    Boolean status = companyEmployeesService.verifyIfEmployeeInCompany(employeeId);
    if (!status) {
      return PayrollStatus.EMPLOYEE_NOT_FOUND;
    }

    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.month, UpdateField.year));
    Pair<PayrollStatus, Map<String, Integer>> data = checkError(updates, flags);

    if (data.getFirst() != PayrollStatus.OK) {
      return data.getFirst();
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.getSecond().get("month"), data.getSecond().get("year"));

    if (payroll == null) {
      return PayrollStatus.NOT_FOUND;
    } else {
      if (payroll.getPaid() == 1) {
        return PayrollStatus.ALREADY_COMPLETED;
      } else {
        payroll.setPaid(1);
        payrollRepository.save(payroll);
        return PayrollStatus.SUCCESS;
      }
    }
  }

  /**
   * Update the paid status of Payroll entry by employee id.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates (Map) : Map consisting of month and year
   * @return (Integer) : Status of update
   */
  public PayrollStatus markAsUnpaid(Integer employeeId, Map<String, Object> updates) {
    Boolean status = companyEmployeesService.verifyIfEmployeeInCompany(employeeId);
    if (!status) {
      return PayrollStatus.EMPLOYEE_NOT_FOUND;
    }

    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.month, UpdateField.year));
    Pair<PayrollStatus, Map<String, Integer>> data = checkError(updates, flags);

    if (data.getFirst() != PayrollStatus.OK) {
      return data.getFirst();
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.getSecond().get("month"), data.getSecond().get("year"));

    if (payroll == null) {
      return PayrollStatus.NOT_FOUND;
    } else {
      if (payroll.getPaid() == 0) {
        return PayrollStatus.ALREADY_COMPLETED;
      } else {
        payroll.setPaid(0);
        payrollRepository.save(payroll);
        return PayrollStatus.SUCCESS;
      }
    }
  }

  /**
   * Delete Payroll entry by employee id.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates (Map) : Map consisting of month and year
   * @return (Integer) : Status of update
   */
  public PayrollStatus deletePayrollByEmployeeId(Integer employeeId, Map<String, Object> updates) {
    Boolean status = companyEmployeesService.verifyIfEmployeeInCompany(employeeId);
    if (!status) {
      return PayrollStatus.EMPLOYEE_NOT_FOUND;
    }

    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.month, UpdateField.year));
    Pair<PayrollStatus, Map<String, Integer>> data = checkError(updates, flags);

    if (data.getFirst() != PayrollStatus.OK) {
      return data.getFirst();
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.getSecond().get("month"), data.getSecond().get("year"));

    if (payroll == null) {
      return PayrollStatus.NOT_FOUND;
    } else {
      payrollRepository.delete(payroll);
      return PayrollStatus.SUCCESS;
    }
  }

  /**
   * Add Payroll entry by employee id.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates (Map) : Map consisting of month and year
   * @return (Integer) : Status of update
   */
  public PayrollStatus addPayrollByEmployeeId(Integer employeeId, Map<String, Object> updates) {
    Boolean status = companyEmployeesService.verifyIfEmployeeInCompany(employeeId);
    if (!status) {
      return PayrollStatus.EMPLOYEE_NOT_FOUND;
    }

    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.day,
            UpdateField.month, UpdateField.year, UpdateField.salary));
    Pair<PayrollStatus, Map<String, Integer>> data = checkError(updates, flags);

    if (data.getFirst() != PayrollStatus.OK) {
      return data.getFirst();
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.getSecond().get("month"), data.getSecond().get("year"));

    if (payroll != null) {
      return PayrollStatus.ALREADY_EXISTS;
    } else {
      Payroll newPayrollEntry = new Payroll();
      newPayrollEntry.setEmployeeId(employeeId);
      newPayrollEntry.setSalary(data.getSecond().get("salary"));
      newPayrollEntry.setTax(calculateTax(data.getSecond().get("salary")));
      newPayrollEntry.setPaymentDate(LocalDate.of(data.getSecond().get("year"),
              data.getSecond().get("month"), data.getSecond().get("day")));
      EmployeeProfile employee = employeeProfileService.getEmployeeProfile(employeeId).get();
      LocalDate startDate = LocalDate.of(data.getSecond().get("year"),
              data.getSecond().get("month"), 1);
      LocalDate endDate = startDate.with(TemporalAdjusters.firstDayOfMonth());
      Integer leaveCount = timeOffService.getTimeOffByEmployeeIdWithDateRange(employee.getId(),
              startDate, endDate).size();
      pdfGenerator.generatePdfReport(newPayrollEntry, employee, leaveCount);
      String url = pdfUploader.uploadPdf(pdfGenerator.getPdfName(newPayrollEntry));
      newPayrollEntry.setPaid(1);
      newPayrollEntry.setPayslip(url);
      payrollRepository.save(newPayrollEntry);
      return PayrollStatus.SUCCESS;
    }
  }

  /**
   * Adjust salary by employee id.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates (Map) : Map consisting of month, year and salary
   * @return (Integer) : Status of update
   */
  public PayrollStatus adjustSalaryByEmployeeId(Integer employeeId, Map<String, Object> updates) {
    Boolean status = companyEmployeesService.verifyIfEmployeeInCompany(employeeId);
    if (!status) {
      return PayrollStatus.EMPLOYEE_NOT_FOUND;
    }

    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.month, UpdateField.year,
            UpdateField.salary));
    Pair<PayrollStatus, Map<String, Integer>> data = checkError(updates, flags);

    if (data.getFirst() != PayrollStatus.OK) {
      return data.getFirst();
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.getSecond().get("month"), data.getSecond().get("year"));

    if (payroll == null) {
      return PayrollStatus.NOT_FOUND;
    } else {
      payroll.setSalary(data.getSecond().get("salary"));
      payroll.setTax(calculateTax(data.getSecond().get("salary")));
      payrollRepository.save(payroll);
      return PayrollStatus.SUCCESS;
    }
  }

  /**
   * Adjust payment day by employee id.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates (Map) : Map consisting of day, month and year
   * @return (Integer) : Status of update
   */
  public PayrollStatus adjustPaymentDayByEmployeeId(Integer employeeId,
                                                    Map<String, Object> updates) {
    Boolean status = companyEmployeesService.verifyIfEmployeeInCompany(employeeId);
    if (!status) {
      return PayrollStatus.EMPLOYEE_NOT_FOUND;
    }

    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.day, UpdateField.month,
            UpdateField.year));
    Pair<PayrollStatus, Map<String, Integer>> data = checkError(updates, flags);

    if (data.getFirst() != PayrollStatus.OK) {
      return data.getFirst();
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.getSecond().get("month"), data.getSecond().get("year"));

    if (payroll == null) {
      return PayrollStatus.NOT_FOUND;
    } else {
      payroll.setPaymentDate(LocalDate.of(data.getSecond().get("year"),
              data.getSecond().get("month"), data.getSecond().get("day")));
      payrollRepository.save(payroll);
      return PayrollStatus.SUCCESS;
    }
  }

  /**
   * Adds Payroll for all employees in a company.
   *
   * @param updates (Map) : Map consisting of month and year
   * @return (Map) : Map consisting of response and employeeList
   */
  public Map<String, Object> generatePayroll(Map<String, Object> updates) {
    List<Integer> employeeList = companyEmployeesService.getAllEmployeesInCompany();
    List<Integer> result = new ArrayList<>();
    Map<String, Object> returnValue = new HashMap<>();

    for (Integer employeeId : employeeList) {
      Optional<EmployeeProfile> employee = employeeProfileService.getEmployeeProfile(employeeId);
      if (employee.isEmpty()) {
        continue;
      }
      updates.put("salary", employee.get().getBaseSalary());
      PayrollStatus status = addPayrollByEmployeeId(employee.get().getId(), updates);

      switch (status) {
        case INVALID_DATA:
          returnValue.put("response", "Invalid day or month or year");
          return returnValue;
        case INVALID_FORMAT:
          returnValue.put("response", "Invalid format for day or month or year");
          return returnValue;
        case ALREADY_EXISTS:
          result.add(employee.get().getId());
          break;
        default:
          break;
      }
    }

    if (!result.isEmpty()) {
      returnValue.put("response",
              "Payroll for the employees in list have already been generated and were not added");
      returnValue.put("employeeList", result);
    } else {
      returnValue.put("response", "Payroll for this month and year has been generated");
    }

    return returnValue;
  }

  /**
   * Deletes Payroll for all employees in a company.
   *
   * @param updates (Map) : Map consisting of month and year
   * @return (Map) : Map consisting of response and employeeList
   */
  public Map<String, Object> deletePayroll(Map<String, Object> updates) {
    List<Integer> employeeList = companyEmployeesService.getAllEmployeesInCompany();
    Map<String, Object> returnValue = new HashMap<>();

    for (Integer employeeId : employeeList) {
      Optional<EmployeeProfile> employee = employeeProfileService.getEmployeeProfile(employeeId);
      if (employee.isEmpty()) {
        continue;
      }
      PayrollStatus status = deletePayrollByEmployeeId(employee.get().getId(), updates);

      switch (status) {
        case INVALID_DATA:
          returnValue.put("response", "Invalid month or year");
          return returnValue;
        case INVALID_FORMAT:
          returnValue.put("response", "Invalid format for month or year");
          return returnValue;
        default:
          break;
      }
    }

    returnValue.put("response", "Payroll for this month and year has been deleted");
    return returnValue;
  }

  private enum UpdateField {
    salary,
    day,
    month,
    year
  }

  private Integer calculateTax(Integer salary) {
    return (int) (salary * 0.3);
  }

  /**
   * Checks if data is valid or returns appropriate status.
   *
   * @param updates (Map) : Map may consist some fields such as day, month, year and salary
   * @param flags (List) : List of UpdateField which may consist some fields such
   *                       as day, month, year and salary
   * @return (Pair) : Pair consisting of status and data converted into Integer values
   */
  private Pair<PayrollStatus, Map<String, Integer>> checkError(Map<String, Object> updates,
                                                               List<UpdateField> flags) {
    Map<String, Integer> data = new HashMap<>();
    try {
      for (UpdateField field : flags) {
        Integer value = (Integer) updates.get(field.name());
        if (value == null) {
          return Pair.of(PayrollStatus.INVALID_DATA, data);
        }
        data.put(field.name(), value);
      }
      try {
        LocalDate unusedDate;
        if (data.get("year") != null && data.get("month") != null && data.get("day") != null) {
          unusedDate = LocalDate.of(data.get("year"), data.get("month"), data.get("day"));
        } else if (data.get("month") != null && data.get("year") != null) {
          unusedDate = LocalDate.of(data.get("year"), data.get("month"), 1);
        }
      } catch (Exception e) {
        return Pair.of(PayrollStatus.INVALID_FORMAT, data);
      }
    } catch (Exception e) {
      return Pair.of(PayrollStatus.INVALID_FORMAT, data);
    }

    return Pair.of(PayrollStatus.OK, data);
  }
}
