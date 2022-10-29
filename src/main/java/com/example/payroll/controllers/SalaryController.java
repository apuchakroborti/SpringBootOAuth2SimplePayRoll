package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeSalaryDto;
import com.example.payroll.dto.request.SalarySearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.services.payroll.SalaryService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    @Autowired
    SalaryService salaryService;
    //TODO need to add description about all of these endpoints
    @PostMapping
    public ServiceResponse insertSalaryInfo(@Valid @RequestBody EmployeeSalaryDto employeeSalaryDto) throws GenericException {
        return new ServiceResponse(null, salaryService.updateSalaryData(employeeSalaryDto), null);
    }
    @GetMapping
    public ServiceResponse getAllSalary(SalarySearchCriteria searchCriteria,
                                                     @PageableDefault(value = 12) Pageable pageable) throws GenericException {
        return Utils.pageToServiceResponse(salaryService.getSalaryDataWithInDateRangeAndEmployeeId(searchCriteria, pageable), EmployeeSalaryDto.class);
    }
    @GetMapping("/{employeeId}")
    public ServiceResponse getCurrentSalaryByEmployeeId(@PathVariable("employeeId") Long employeeId) throws GenericException {
        return new ServiceResponse(null, salaryService.getCurrentSalaryByEmployeeId(employeeId));
    }
}
