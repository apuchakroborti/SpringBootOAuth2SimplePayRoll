package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.dto.request.TaxSearchCriteria;
import com.example.payroll.dto.response.Pagination;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.entity.payroll.EmployeeTaxDeposit;
import com.example.payroll.services.payroll.TaxDepositService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/employee/tax")
public class EmployeeTaxController {
    @Autowired
    TaxDepositService taxDepositService;

    @PostMapping
    public ServiceResponse<EmployeeTaxDepositDto> insertTaxInfo(@Valid @RequestBody EmployeeTaxDepositDto employeeTaxDepositModel) throws GenericException {
        return taxDepositService.insertIndividualTaxInfo(employeeTaxDepositModel);
    }
    @GetMapping("/{employeeId}")
    public ServiceResponse<Page<EmployeeTaxDepositDto>> getAllTaxInfoByEmployeeId(@PathVariable("employeeId") Long employeeId, @PageableDefault(value = 12) Pageable pageable){
        Page<EmployeeTaxDeposit> taxDepositPage = taxDepositService.getAllTaxInfoByEmployeeId(employeeId, pageable);

        return new ServiceResponse(Utils.getSuccessResponse(),
                Utils.toDtoList(taxDepositPage, EmployeeTaxDepositDto.class),
                new Pagination(taxDepositPage.getTotalElements(), taxDepositPage.getNumberOfElements(), taxDepositPage.getNumber(), taxDepositPage.getSize()));
    }
    @GetMapping
    public ServiceResponse<Page<EmployeeTaxDeposit>> getAllTaxInfo(TaxSearchCriteria criteria, @PageableDefault(value = 12) Pageable pageable){
        Page<EmployeeTaxDeposit> taxDepositPage = taxDepositService.getTaxInfoWithInDateRangeAndEmployeeId(criteria, pageable);

        return new ServiceResponse(Utils.getSuccessResponse(),
                Utils.toDtoList(taxDepositPage, EmployeeTaxDepositDto.class),
                new Pagination(taxDepositPage.getTotalElements(), taxDepositPage.getNumberOfElements(), taxDepositPage.getNumber(), taxDepositPage.getSize()));
    }
}
