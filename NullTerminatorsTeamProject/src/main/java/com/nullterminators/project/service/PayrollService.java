package com.nullterminators.project.service;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;

    @Autowired
    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
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
        Integer paymentMonth = Integer.parseInt((String) updates.get("month"));
        Integer paymentYear = Integer.parseInt((String) updates.get("year"));
        Integer paymentDay = Integer.parseInt((String) updates.get("day"));
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
        Integer paymentMonth = Integer.parseInt((String) updates.get("month"));
        Integer paymentYear = Integer.parseInt((String) updates.get("year"));
        Integer paymentDay = Integer.parseInt((String) updates.get("day"));
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll == null) { return 0; }
        else {
            payroll.setPaymentDate(LocalDate.of(paymentYear, paymentMonth, paymentDay));
            payrollRepository.save(payroll);
            return 1;
        }
    }

}
