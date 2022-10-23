package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.services.payroll.EmployeeTaxDepositService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/employee/tax")
public class EmployeeTaxController {
    @Autowired
    EmployeeTaxDepositService employeeTaxDepositService;

    @PostMapping("/insert")
    public ServiceResponse insertTaxInfo(@RequestBody EmployeeTaxDepositDto employeeTaxDepositModel){
        return new ServiceResponse(null, employeeTaxDepositService.insertTaxInfo(employeeTaxDepositModel));
    }
    @GetMapping("/getAllTaxInfoByEmployeeId")
    public ServiceResponse getAllTaxInfoByEmployeeId(Long employeeId, @PageableDefault(value = 12) Pageable pageable){
        return Utils.pageToServiceResponse(employeeTaxDepositService.getAllTaxInfoByEmployeeId(employeeId, pageable), EmployeeTaxDepositDto.class);
    }
    @GetMapping("/getAllTxInfoByFromDateToDateAndEmployeeId/{employeeId}/{fromDate}/{toDate}")
    public ServiceResponse getAllTxInfoByFromDateToDateAndEmployeeId(@PathVariable("employeeId") Long employeeId,
                                                                     @PathVariable("fromDate") LocalDate fromDate,
                                                                     @PathVariable("toDate") LocalDate toDate,
                                                                     @PageableDefault(value = 12) Pageable pageable
                                                                     ){
        return Utils.pageToServiceResponse(employeeTaxDepositService.getTaxInfoWithInDateRangeAndEmployeeId(fromDate, toDate, employeeId, pageable), EmployeeTaxDepositDto.class);
    }
}
