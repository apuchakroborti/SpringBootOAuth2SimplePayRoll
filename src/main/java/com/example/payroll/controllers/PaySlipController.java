package com.example.payroll.controllers;

import com.example.payroll.dto.MonthlyPaySlipDto;
import com.example.payroll.dto.request.MonthlyPaySlipRequestDto;
import com.example.payroll.dto.request.PayslipSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.services.payroll.MonthlyPaySlipService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/payslip")
public class PaySlipController {

    @Autowired
    MonthlyPaySlipService monthlyPaySlipService;
    //TODO need to add description about all of these endpoints
    @PostMapping
    public ServiceResponse generatePaySlip(@Valid @RequestBody MonthlyPaySlipRequestDto monthlyPaySlipRequestDto) throws GenericException {
        return new ServiceResponse(null, monthlyPaySlipService.geneartePaySlip(monthlyPaySlipRequestDto), null);
    }
    @GetMapping
    public ServiceResponse getPaySlipBySearchCriteria(PayslipSearchCriteria criteria, @PageableDefault(value = 12) Pageable pageable) throws GenericException{
        return Utils.pageToServiceResponse(monthlyPaySlipService.getPaySlipWithInDateRangeAndEmployeeId(criteria, pageable), MonthlyPaySlipDto.class);
    }
}
