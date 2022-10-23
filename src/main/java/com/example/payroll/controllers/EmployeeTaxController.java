package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeTaxDepositModel;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.services.payroll.EmployeeTaxDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/employee/tax")
public class EmployeeTaxController {
    @Autowired
    EmployeeTaxDepositService employeeTaxDepositService;

    @PostMapping("/insert")
    public ServiceResponse insertTaxInfo(@RequestBody EmployeeTaxDepositModel employeeTaxDepositModel){
        return new ServiceResponse(null, employeeTaxDepositService.insertTaxInfo(employeeTaxDepositModel));
    }
    @GetMapping("/getAllTaxInfoByEmployeeId")
    public ServiceResponse getAllTaxInfoByEmployeeId(Long employeeId){
        return new ServiceResponse(null, employeeTaxDepositService.getAllTaxInfoByEmployeeId(employeeId));
    }
    @GetMapping("/getAllTxInfoByFromDateToDateAndEmployeeId/{employeeId}/{fromDate}/{toDate}")
    public ServiceResponse getAllTxInfoByFromDateToDateAndEmployeeId(@PathVariable("employeeId") Long employeeId, @PathVariable("fromDate") LocalDate fromDate, @PathVariable("toDate") LocalDate toDate){
        return new ServiceResponse(null, employeeTaxDepositService.getPFInfoWithInDateRangeAndEmployeeId(fromDate, toDate, employeeId), null);
    }
}
