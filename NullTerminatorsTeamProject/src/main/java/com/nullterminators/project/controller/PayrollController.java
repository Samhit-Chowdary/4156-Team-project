package com.nullterminators.project.controller;

import com.nullterminators.project.model.Payroll;
import com.nullterminators.project.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @GetMapping("/{id}")
    public ResponseEntity<Payroll> getPayroll(@PathVariable("id") Integer id) {
        try{
            Payroll payroll = payrollService.getPayroll(id);
            if (payroll == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(payroll);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
