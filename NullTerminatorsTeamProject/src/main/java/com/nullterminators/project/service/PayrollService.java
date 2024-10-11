package com.nullterminators.project.service;

import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    public Payroll getPayroll(Integer id) {
        return payrollRepository.findById(id).orElse(null);
    }

}
