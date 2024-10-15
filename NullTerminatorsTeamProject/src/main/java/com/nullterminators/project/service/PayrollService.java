package com.nullterminators.project.service;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.EmployeeProfileRepository;
import com.nullterminators.project.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    public PayrollService(PayrollRepository payrollRepository, EmployeeProfileRepository employeeProfileRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public String getPayrollByEmployeeId(Integer employeeId) {
        List<Payroll> payrollInformation = payrollRepository.findAllByEmployeeIdOrderByPaymentDateDesc(employeeId);
        StringBuilder result = new StringBuilder();

        for (Payroll payroll : payrollInformation) {
            result.append("Payment Date: ").append(payroll.getPaymentDate()).append("\n");
            result.append("Payslip: ").append(payroll.getPayslip()).append("\n");
        }

        return result.toString();
    }

    public Integer markAsPaid(Integer employeeId, Map<String, Object> updates) {
        Integer paymentMonth = Integer.parseInt((String) updates.get("month"));
        Integer paymentYear = Integer.parseInt((String) updates.get("year"));
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll == null) { return 0; }
        else {
            if (payroll.getPaid() == 1) { return 1; }
            else {
                payroll.setPaid(1);
                payrollRepository.save(payroll);
                return 2;
            }
        }
    }

    public Integer markAsUnpaid(Integer employeeId, Map<String, Object> updates) {
        Integer paymentMonth = Integer.parseInt((String) updates.get("month"));
        Integer paymentYear = Integer.parseInt((String) updates.get("year"));
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll == null) { return 0; }
        else {
            if (payroll.getPaid() == 0) { return 1; }
            else {
                payroll.setPaid(0);
                payrollRepository.save(payroll);
                return 2;
            }
        }
    }

    public Integer deletePayrollByEmployeeId(Integer employeeId, Map<String, Object> updates) {
        Integer paymentMonth = Integer.parseInt((String) updates.get("month"));
        Integer paymentYear = Integer.parseInt((String) updates.get("year"));
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll == null) { return 0; }
        else {
            payrollRepository.delete(payroll);
            return 1;
        }
    }

    public Integer addPayrollByEmployeeId(Integer employeeId, Map<String, Object> updates) {
        int paymentMonth = Integer.parseInt((String) updates.get("month"));
        int paymentYear = Integer.parseInt((String) updates.get("year"));
        int paymentDay = Integer.parseInt((String) updates.get("day"));
        Integer salary = Integer.parseInt((String) updates.get("salary"));
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll != null) { return 0; }
        else {
            Payroll newPayrollEntry = new Payroll();
            newPayrollEntry.setEmployeeId(employeeId);
            newPayrollEntry.setSalary(salary);
            newPayrollEntry.setPaymentDate(LocalDate.of(paymentYear, paymentMonth, paymentDay));
            newPayrollEntry.setPaid(1);
            payrollRepository.save(newPayrollEntry);
            return 1;
        }
    }

    public Integer adjustSalaryByEmployeeId(Integer employeeId, Map<String, Object> updates) {
        Integer paymentMonth = Integer.parseInt((String) updates.get("month"));
        Integer paymentYear = Integer.parseInt((String) updates.get("year"));
        Integer salary = Integer.parseInt((String) updates.get("salary"));
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll == null) { return 0; }
        else {
            payroll.setSalary(salary);
            payrollRepository.save(payroll);
            return 1;
        }
    }

    public Integer adjustPaymentDayByEmployeeId(Integer employeeId, Map<String, Object> updates) {
        int paymentMonth = Integer.parseInt((String) updates.get("month"));
        int paymentYear = Integer.parseInt((String) updates.get("year"));
        int paymentDay = Integer.parseInt((String) updates.get("day"));
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll == null) { return 0; }
        else {
            payroll.setPaymentDate(LocalDate.of(paymentYear, paymentMonth, paymentDay));
            payrollRepository.save(payroll);
            return 1;
        }
    }

    public String generatePayroll(Map<String, Object> updates) {
        List<EmployeeProfile> employeeInformation = employeeProfileRepository.findAll();
        StringBuilder result = new StringBuilder();
        for (EmployeeProfile employee : employeeInformation) {
            updates.put("salary", String.valueOf(employee.getBaseSalary()));
            Integer status = addPayrollByEmployeeId(employee.getId(), updates);
            if (status == 0) {
                result.append(employee.getId()).append("\n");
            }
        }
        if (!result.isEmpty()) {
            result.insert(0, "Payroll for the following employees have already been generated and were not added:\n");
        }
        return result.toString();
    }

    public String deletePayroll(Map<String, Object> updates) {
        List<EmployeeProfile> employeeInformation = employeeProfileRepository.findAll();
        StringBuilder result = new StringBuilder();
        for (EmployeeProfile employee : employeeInformation) {
            Integer status = deletePayrollByEmployeeId(employee.getId(), updates);
            if (status == 0) {
                result.append(employee.getId()).append("\n");
            }
        }
        if (!result.isEmpty()) {
            result.insert(0, "Payroll for the following employees have already been deleted and were not deleted:\n");
        }
        return result.toString();
    }

}
