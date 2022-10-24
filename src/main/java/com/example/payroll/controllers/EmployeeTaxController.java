package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.dto.request.TaxSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.services.payroll.EmployeeTaxDepositService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/employee/tax")
public class EmployeeTaxController {
    @Autowired
    EmployeeTaxDepositService employeeTaxDepositService;

    @PostMapping()
    public ServiceResponse insertTaxInfo(@Valid @RequestBody EmployeeTaxDepositDto employeeTaxDepositModel) throws GenericException {
        return new ServiceResponse(null, employeeTaxDepositService.insertTaxInfo(employeeTaxDepositModel));
    }
    @GetMapping("/{employeeId}")
    public ServiceResponse getAllTaxInfoByEmployeeId(@PathVariable("employeeId") Long employeeId, @PageableDefault(value = 12) Pageable pageable){
        return Utils.pageToServiceResponse(employeeTaxDepositService.getAllTaxInfoByEmployeeId(employeeId, pageable), EmployeeTaxDepositDto.class);
    }
    @GetMapping()
    public ServiceResponse getAllTaxInfo(TaxSearchCriteria criteria, @PageableDefault(value = 12) Pageable pageable){
        return Utils.pageToServiceResponse(employeeTaxDepositService.getTaxInfoWithInDateRangeAndEmployeeId(criteria, pageable), EmployeeTaxDepositDto.class);
    }
}
