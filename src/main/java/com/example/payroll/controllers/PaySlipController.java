package com.example.payroll.controllers;

import com.example.payroll.dto.MonthlyPaySlipDto;
import com.example.payroll.dto.request.MonthlyPaySlipRequestDto;
import com.example.payroll.dto.request.PayslipSearchCriteria;
import com.example.payroll.dto.response.Pagination;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.entity.payroll.MonthlyPaySlip;
import com.example.payroll.services.payroll.MonthlyPaySlipService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/payslip")
public class PaySlipController {

    @Autowired
    MonthlyPaySlipService monthlyPaySlipService;

    /*
    * Using this API you can generate monthly final payslip for any month from current or previous financial year
    * Monthly draft payslip will be generate for the current financial year but final for the starting of the financial year to current month
    * Tax and Provident fund will be deducted from salary and will be inserted into TaxDeposit and Provident fund table respectively
    * */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ServiceResponse<MonthlyPaySlipDto> generatePaySlip(@Valid @RequestBody MonthlyPaySlipRequestDto monthlyPaySlipRequestDto) throws GenericException {
        return monthlyPaySlipService.generatePaySlip(monthlyPaySlipRequestDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ServiceResponse<Page<MonthlyPaySlipDto>> getPaySlipBySearchCriteria(PayslipSearchCriteria criteria, @PageableDefault(value = 12) Pageable pageable) throws GenericException{

        Page<MonthlyPaySlip> payslipPage = monthlyPaySlipService.getPaySlipWithInDateRangeAndEmployeeId(criteria, pageable);

        return new ServiceResponse(Utils.getSuccessResponse(),
                Utils.toDtoList(payslipPage, MonthlyPaySlipDto.class),
                new Pagination(payslipPage.getTotalElements(), payslipPage.getNumberOfElements(), payslipPage.getNumber(), payslipPage.getSize()));
    }
}
