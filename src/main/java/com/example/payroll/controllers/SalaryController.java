package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeSalaryDto;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.services.payroll.EmployeeSalaryService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    @Autowired
    EmployeeSalaryService employeeSalaryService;

    @PostMapping()
    public ServiceResponse insertSalaryInfo(@RequestBody EmployeeSalaryDto employeeSalaryDto) throws GenericException {
        return new ServiceResponse(null, employeeSalaryService.insertSalaryData(employeeSalaryDto), null);
    }
    @GetMapping("/{employeeId}/{fromDate}/{toDate}")
    public ServiceResponse getAllTaxInfoByEmployeeId(@PathVariable("employeeId") Long employeeId,
                                                     @PathVariable("fromDate") LocalDate fromDate,
                                                     @PathVariable("toDate") LocalDate toDate,
                                                     @PageableDefault(value = 12) Pageable pageable) throws GenericException {
        return Utils.pageToServiceResponse(employeeSalaryService.getSalaryDataWithInDateRangeAndEmployeeId(fromDate, toDate, employeeId, pageable), EmployeeSalaryDto.class);
    }
    @GetMapping("/{employeeId}")
    public ServiceResponse getCurrentSalaryByEmployeeId(@PathVariable("employeeId") Long employeeId) throws GenericException {
        return new ServiceResponse(null, employeeSalaryService.getCurrentSalaryByEmployeeId(employeeId));
    }
}
