package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeSalaryModel;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.PayrollException;
import com.example.payroll.services.payroll.EmployeeSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    @Autowired
    EmployeeSalaryService employeeSalaryService;

    @PostMapping("/insert")
    public ServiceResponse insertSalaryInfo(@RequestBody EmployeeSalaryModel employeeSalaryModel) throws PayrollException {
        return new ServiceResponse(null, employeeSalaryService.insertSalaryData(employeeSalaryModel), null);
    }
    @GetMapping("/getAllTaxInfoByEmployeeId/{employeeId}/{fromDate}/{toDate}")
    public ServiceResponse getAllTaxInfoByEmployeeId(@PathVariable("employeeId") Long employeeId, @PathVariable("fromDate") LocalDate fromDate, @PathVariable("toDate") LocalDate toDate) throws PayrollException{
        return new ServiceResponse(null, employeeSalaryService.getSalaryDataWithInDateRangeAndEmployeeId(fromDate, toDate, employeeId), null);
    }
}
