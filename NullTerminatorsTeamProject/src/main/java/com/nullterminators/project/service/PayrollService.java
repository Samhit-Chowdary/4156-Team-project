package com.nullterminators.project.service;

import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.EmployeeProfileRepository;
import com.nullterminators.project.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

    public List<Payroll> getPayrollByEmployeeId(Integer employeeId) {
        return payrollRepository.findAllByEmployeeIdOrderByPaymentDateDesc(employeeId);
    }

    public Integer markAsPaid(Integer employeeId, Map<String, Object> updates) {
        Integer paymentMonth = (Integer) updates.get("month");
        Integer paymentYear = (Integer) updates.get("year");
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
        Integer paymentMonth = (Integer) updates.get("month");
        Integer paymentYear = (Integer) updates.get("year");
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
        Integer paymentMonth = (Integer) updates.get("month");
        Integer paymentYear = (Integer) updates.get("year");
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll == null) { return 0; }
        else {
            payrollRepository.delete(payroll);
            return 1;
        }
    }

    public Integer addPayrollByEmployeeId(Integer employeeId, Map<String, Object> updates) {
        Integer paymentMonth = (Integer) updates.get("month");
        Integer paymentYear = (Integer) updates.get("year");
        Integer paymentDay = (Integer) updates.get("day");
        Integer salary = (Integer) updates.get("salary");
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll != null) { return 0; }
        else {
            Payroll newPayrollEntry = new Payroll();
            newPayrollEntry.setEmployeeId(employeeId);
            newPayrollEntry.setSalary(salary);
            newPayrollEntry.setTax(0);
            newPayrollEntry.setPayslip("N/A");
            newPayrollEntry.setPaymentDate(LocalDate.of(paymentYear, paymentMonth, paymentDay));
            newPayrollEntry.setPaid(1);
            payrollRepository.save(newPayrollEntry);
            return 1;
        }
    }

    public Integer adjustSalaryByEmployeeId(Integer employeeId, Map<String, Object> updates) {
        Integer paymentMonth = (Integer) updates.get("month");
        Integer paymentYear = (Integer) updates.get("year");
        Integer salary = (Integer) updates.get("salary");
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll == null) { return 0; }
        else {
            payroll.setSalary(salary);
            payrollRepository.save(payroll);
            return 1;
        }
    }

    public Integer adjustPaymentDayByEmployeeId(Integer employeeId, Map<String, Object> updates) {
        Integer paymentMonth = (Integer) updates.get("month");
        Integer paymentYear = (Integer) updates.get("year");
        Integer paymentDay = (Integer) updates.get("day");
        Payroll payroll = payrollRepository.findByEmployeeIdPaymentMonthAndYear(employeeId, paymentMonth, paymentYear);
        if (payroll == null) { return 0; }
        else {
            payroll.setPaymentDate(LocalDate.of(paymentYear, paymentMonth, paymentDay));
            payrollRepository.save(payroll);
            return 1;
        }
    }

    public Map<String, Object> generatePayroll(Map<String, Object> updates) {
        List<EmployeeProfile> employeeInformation = employeeProfileRepository.findAll();
        List<Integer> result = new ArrayList<>();
        Map<String, Object> returnValue = new HashMap<>();
        for (EmployeeProfile employee : employeeInformation) {
            updates.put("salary", employee.getBaseSalary());
            Integer status = addPayrollByEmployeeId(employee.getId(), updates);
            if (status == 0) {
                result.add(employee.getId());
            }
        }
        if (!result.isEmpty()) {
            returnValue.put("response", "Payroll for the employees in list have already been generated and were not added");
            returnValue.put("employeeList", result);
        } else {
            returnValue.put("response", "Payroll for this month and year has been generated");
        }
        return returnValue;
    }

    public Map<String, Object> deletePayroll(Map<String, Object> updates) {
        List<EmployeeProfile> employeeInformation = employeeProfileRepository.findAll();
        List<Integer> result = new ArrayList<>();
        Map<String, Object> returnValue = new HashMap<>();
        for (EmployeeProfile employee : employeeInformation) {
            Integer status = deletePayrollByEmployeeId(employee.getId(), updates);
            if (status == 0) {
                result.add(employee.getId());
            }
        }
        if (!result.isEmpty()) {
            returnValue.put("response", "Payroll for the employees in list have already been deleted and were not deleted");
            returnValue.put("employeeList", result);
        } else {
            returnValue.put("response", "Payroll for this month and year has been deleted");
        }
        return returnValue;
    }

}
