package com.nullterminators.project.service;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.PayrollRepository;
import com.nullterminators.project.util.pdf.PdfGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {

  private final PayrollRepository payrollRepository;
  private final EmployeeProfileService employeeProfileService;
  private final PdfGenerator pdfGenerator;

  @Autowired
  public PayrollService(PayrollRepository payrollRepository,
                        EmployeeProfileService employeeProfileService,
                        PdfGenerator pdfGenerator) {
    this.payrollRepository = payrollRepository;
    this.employeeProfileService = employeeProfileService;
    this.pdfGenerator = pdfGenerator;
  }

  public List<Map<String, Object>> getPayrollByEmployeeId(Integer employeeId) {
    List<Payroll> payrollInformation =
            payrollRepository.findAllByEmployeeIdOrderByPaymentDateDesc(employeeId);
    List<Map<String, Object>> returnValue = new ArrayList<>();

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

    return returnValue;
  }

  public Integer markAsPaid(Integer employeeId, Map<String, Object> updates) {
    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.month, UpdateField.year));
    Map<String, Integer> data = checkError(updates, flags);

    if (data.get("result") != 0) {
      return data.get("result");
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.get("month"), data.get("year"));

    if (payroll == null) {
      return 3;
    } else {
      if (payroll.getPaid() == 1) {
        return 4;
      } else {
        payroll.setPaid(1);
        payrollRepository.save(payroll);
        return 5;
      }
    }
  }

  public Integer markAsUnpaid(Integer employeeId, Map<String, Object> updates) {
    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.month, UpdateField.year));
    Map<String, Integer> data = checkError(updates, flags);

    if (data.get("result") != 0) {
      return data.get("result");
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.get("month"), data.get("year"));

    if (payroll == null) {
      return 3;
    } else {
      if (payroll.getPaid() == 0) {
        return 4;
      } else {
        payroll.setPaid(0);
        payrollRepository.save(payroll);
        return 5;
      }
    }
  }

  public Integer deletePayrollByEmployeeId(Integer employeeId, Map<String, Object> updates) {
    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.month, UpdateField.year));
    Map<String, Integer> data = checkError(updates, flags);

    if (data.get("result") != 0) {
      return data.get("result");
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.get("month"), data.get("year"));

    if (payroll == null) {
      return 3;
    } else {
      payrollRepository.delete(payroll);
      return 4;
    }
  }

  public Integer addPayrollByEmployeeId(Integer employeeId, Map<String, Object> updates) {
    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.day,
            UpdateField.month, UpdateField.year, UpdateField.salary));
    Map<String, Integer> data = checkError(updates, flags);

    if (data.get("result") != 0) {
      return data.get("result");
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.get("month"), data.get("year"));

    if (payroll != null) {
      return 3;
    } else {
      Payroll newPayrollEntry = new Payroll();
      newPayrollEntry.setEmployeeId(employeeId);
      newPayrollEntry.setSalary(data.get("salary"));
      newPayrollEntry.setTax(calculateTax(data.get("salary")));
      newPayrollEntry.setPayslip("N/A");
      newPayrollEntry.setPaymentDate(LocalDate.of(data.get("year"),
              data.get("month"), data.get("day")));
      pdfGenerator.generatePdfReport(newPayrollEntry);
      newPayrollEntry.setPaid(1);
      payrollRepository.save(newPayrollEntry);
      return 4;
    }
  }

  public Integer adjustSalaryByEmployeeId(Integer employeeId, Map<String, Object> updates) {
    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.month, UpdateField.year,
            UpdateField.salary));
    Map<String, Integer> data = checkError(updates, flags);

    if (data.get("result") != 0) {
      return data.get("result");
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.get("month"), data.get("year"));

    if (payroll == null) {
      return 3;
    } else {
      payroll.setSalary(data.get("salary"));
      payroll.setTax(calculateTax(data.get("salary")));
      payrollRepository.save(payroll);
      return 4;
    }
  }

  public Integer adjustPaymentDayByEmployeeId(Integer employeeId, Map<String, Object> updates) {
    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.day, UpdateField.month,
            UpdateField.year));
    Map<String, Integer> data = checkError(updates, flags);

    if (data.get("result") != 0) {
      return data.get("result");
    }

    Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId,
            data.get("month"), data.get("year"));

    if (payroll == null) {
      return 3;
    } else {
      payroll.setPaymentDate(LocalDate.of(data.get("year"), data.get("month"), data.get("day")));
      payrollRepository.save(payroll);
      return 4;
    }
  }

  public Map<String, Object> generatePayroll(Map<String, Object> updates) {
    List<EmployeeProfile> employeeInformation = employeeProfileService.getAllEmployees();
    List<Integer> result = new ArrayList<>();
    Map<String, Object> returnValue = new HashMap<>();

    for (EmployeeProfile employee : employeeInformation) {
      updates.put("salary", employee.getBaseSalary());
      Integer status = addPayrollByEmployeeId(employee.getId(), updates);

      switch (status) {
        case 1:
          returnValue.put("response", "Invalid day or month or year");
          return returnValue;
        case 2:
          returnValue.put("response", "Invalid format for day or month or year");
          return returnValue;
        case 3:
          result.add(employee.getId());
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

  public Map<String, Object> deletePayroll(Map<String, Object> updates) {
    List<EmployeeProfile> employeeInformation = employeeProfileService.getAllEmployees();
    List<Integer> result = new ArrayList<>();
    Map<String, Object> returnValue = new HashMap<>();

    for (EmployeeProfile employee : employeeInformation) {
      Integer status = deletePayrollByEmployeeId(employee.getId(), updates);

      switch (status) {
        case 1:
          returnValue.put("response", "Invalid month or year");
          return returnValue;
        case 2:
          returnValue.put("response", "Invalid format for month or year");
          return returnValue;
        case 3:
          result.add(employee.getId());
          break;
        default:
          break;
      }
    }

    if (!result.isEmpty()) {
      returnValue.put("response", "Payroll for the employees in list have already been deleted");
      returnValue.put("employeeList", result);
    } else {
      returnValue.put("response", "Payroll for this month and year has been deleted");
    }

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

  private Map<String, Integer> checkError(Map<String, Object> updates, List<UpdateField> flags) {
    Map<String, Integer> data = new HashMap<>();
    try {
      for (UpdateField field : flags) {
        Integer value = (Integer) updates.get(field.name());
        if (value == null) {
          data.put("result", 1);
          return data;
        }
        data.put(field.name(), value);
      }
      try {
        LocalDate date;
        if (data.get("year") != null && data.get("month") != null && data.get("day") != null) {
          date = LocalDate.of(data.get("year"), data.get("month"), data.get("day"));
        } else if (data.get("month") != null && data.get("year") != null) {
          date = LocalDate.of(data.get("year"), data.get("month"), 1);
        }
      } catch (Exception e) {
        data.put("result", 2);
        return data;
      }
    } catch (Exception e) {
      data.put("result", 2);
      return data;
    }

    data.put("result", 0);
    return data;
  }
}
