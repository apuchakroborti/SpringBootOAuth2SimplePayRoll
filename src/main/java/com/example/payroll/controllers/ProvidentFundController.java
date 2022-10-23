package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeProvidentFundDto;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.services.payroll.EmployeeProvidentFundService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/provident-fund")
public class ProvidentFundController {
    @Autowired
    EmployeeProvidentFundService employeeProvidentFundService;

    @PostMapping("/insert")
    public ServiceResponse insertProvidentFundData(@RequestBody EmployeeProvidentFundDto employeeProvidentFundDto){
        return new ServiceResponse(null, employeeProvidentFundService.insertPfData(employeeProvidentFundDto), null);
    }
    @GetMapping("/getPaySlipByFromDateToDateAndEmployeeId/{employeeId}/{fromDate}/{toDate}")
    public ServiceResponse getPaySlipByDateRangeAndEmployeeId(@PathVariable("employeeId") Long employeeId,
                                                              @PathVariable("fromDate") LocalDate fromDate,
                                                              @PathVariable("toDate") LocalDate toDate,
                                                              @PageableDefault(value = 12) Pageable pageable){
        return Utils.pageToServiceResponse(employeeProvidentFundService.getPFInfoWithInDateRangeAndEmployeeId(fromDate, toDate, employeeId, pageable), EmployeeProvidentFundDto.class);
    }
}
