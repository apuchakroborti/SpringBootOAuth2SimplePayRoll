package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeMonthlyPaySlipModel;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.services.payroll.EmployeeMonthlyPaySlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/payslip")
public class PaySlipController {

    @Autowired
    EmployeeMonthlyPaySlipService employeeMonthlyPaySlipService;

    @PostMapping("/create")
    public ServiceResponse createPaySlip(@RequestBody EmployeeMonthlyPaySlipModel employeeMonthlyPaySlipModel){
        return new ServiceResponse(null, employeeMonthlyPaySlipService.createPaySlip(employeeMonthlyPaySlipModel), null);
    }
    @GetMapping("/getPaySlipByFromDateToDateAndEmployeeId/{employeeId}/{fromDate}/{toDate}")
    public ServiceResponse getPaySlipByEmployeeId(@PathVariable("employeeId") Long employeeId, @PathVariable("fromDate") LocalDate fromDate, @PathVariable("toDate") LocalDate toDate){
        return new ServiceResponse(null, employeeMonthlyPaySlipService.getPaySlipWithInDateRangeAndEmployeeId(fromDate, toDate, employeeId), null);
    }
}
