package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeProvidentFundModel;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.services.payroll.EmployeeProvidentFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/provident-fund")
public class ProvidentFundController {
    @Autowired
    EmployeeProvidentFundService employeeProvidentFundService;

    @PostMapping("/insert")
    public ServiceResponse insertProvidentFundData(@RequestBody EmployeeProvidentFundModel employeeProvidentFundModel){
        return new ServiceResponse(null, employeeProvidentFundService.insertPfData(employeeProvidentFundModel), null);
    }
    @GetMapping("/getPaySlipByFromDateToDateAndEmployeeId/{employeeId}/{fromDate}/{toDate}")
    public ServiceResponse getPaySlipByDateRangeAndEmployeeId(@PathVariable("employeeId") Long employeeId, @PathVariable("fromDate") LocalDate fromDate, @PathVariable("toDate") LocalDate toDate){
        return new ServiceResponse(null, employeeProvidentFundService.getPFInfoWithInDateRangeAndEmployeeId(fromDate, toDate, employeeId), null);
    }
}
