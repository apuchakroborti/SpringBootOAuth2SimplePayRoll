package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeMonthlyPaySlipDto;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.models.payroll.EmployeeMonthlyPaySlip;
import com.example.payroll.services.payroll.EmployeeMonthlyPaySlipService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/payslip")
public class PaySlipController {

    @Autowired
    EmployeeMonthlyPaySlipService employeeMonthlyPaySlipService;

    @PostMapping("/create")
    public ServiceResponse createPaySlip(@RequestBody EmployeeMonthlyPaySlipDto employeeMonthlyPaySlipDto){
        return new ServiceResponse(null, employeeMonthlyPaySlipService.createPaySlip(employeeMonthlyPaySlipDto), null);
    }
    @GetMapping("/getPaySlipByFromDateToDateAndEmployeeId/{employeeId}/{fromDate}/{toDate}")
    public ServiceResponse getPaySlipByEmployeeId(@PathVariable("employeeId") Long employeeId,
                                                  @PathVariable("fromDate") LocalDate fromDate,
                                                  @PathVariable("toDate") LocalDate toDate,
                                                  @PageableDefault(value = 12) Pageable pageable){
        return Utils.pageToServiceResponse(employeeMonthlyPaySlipService.getPaySlipWithInDateRangeAndEmployeeId(fromDate, toDate, employeeId, pageable), EmployeeMonthlyPaySlipDto.class);
    }
}
