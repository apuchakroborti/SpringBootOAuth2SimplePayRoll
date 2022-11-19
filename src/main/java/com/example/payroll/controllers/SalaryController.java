package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeSalaryDto;
import com.example.payroll.dto.request.SalarySearchCriteria;
import com.example.payroll.dto.response.Pagination;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.entity.payroll.EmployeeSalary;
import com.example.payroll.services.payroll.SalaryService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    @Autowired
    SalaryService salaryService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ServiceResponse<EmployeeSalaryDto> updateSalary(@Valid @RequestBody EmployeeSalaryDto employeeSalaryDto) throws GenericException {
        return salaryService.updateSalaryData(employeeSalaryDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public ServiceResponse getAllSalary(SalarySearchCriteria searchCriteria,
                                                     @PageableDefault(value = 12) Pageable pageable) throws GenericException {
        Page<EmployeeSalary> employeeSalaryPage = salaryService.getSalaryDataWithInDateRangeAndEmployeeId(searchCriteria, pageable);

        return new ServiceResponse(Utils.getSuccessResponse(),
                Utils.toDtoList(employeeSalaryPage, EmployeeSalaryDto.class),
                new Pagination(employeeSalaryPage.getTotalElements(),
                        employeeSalaryPage.getNumberOfElements(),
                        employeeSalaryPage.getNumber(),
                        employeeSalaryPage.getSize()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/{employeeId}")
    public ServiceResponse<EmployeeSalaryDto> getCurrentSalaryByEmployeeId(@PathVariable("employeeId") Long employeeId) throws GenericException {
        return salaryService.getCurrentSalaryByEmployeeId(employeeId);
    }
}
