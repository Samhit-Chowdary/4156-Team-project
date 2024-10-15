package com.nullterminators.project.service;

import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

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

    public Integer markAsPaid(Integer employeeId, String paymentMonth, String paymentYear) {
        Integer month = Integer.parseInt(paymentMonth);
        Integer year = Integer.parseInt(paymentYear);
        Payroll payroll = payrollRepository.findByEmployeeIdOrderByPaymentMonthAndYear(employeeId, month, year);
        if (payroll == null) { return 0; }
        else {
            Month latestPaymentMonth = payroll.getPaymentDate().getMonth();
            Month currentMonth = LocalDate.now().getMonth();
            if (!latestPaymentMonth.equals(currentMonth)) { return 1; }
            else {
                if (payroll.getPaid() == 1) { return 2; }
                else {
                    payroll.setPaid(1);
                    payrollRepository.save(payroll);
                    return 3;
                }
            }
        }
    }

    public Integer markAsUnpaid(Integer employeeId, String paymentMonth, String paymentYear) {
        Integer month = Integer.parseInt(paymentMonth);
        Integer year = Integer.parseInt(paymentYear);
        Payroll payroll = payrollRepository.findByEmployeeIdOrderByPaymentMonthAndYear(employeeId, month, year);
        if (payroll == null) { return 0; }
        else {
            Month latestPaymentMonth = payroll.getPaymentDate().getMonth();
            Month currentMonth = LocalDate.now().getMonth();
            if (!latestPaymentMonth.equals(currentMonth)) { return 1; }
            else {
                if (payroll.getPaid() == 0) { return 2; }
                else {
                    payroll.setPaid(0);
                    payrollRepository.save(payroll);
                    return 3;
                }
            }
        }
    }

}
