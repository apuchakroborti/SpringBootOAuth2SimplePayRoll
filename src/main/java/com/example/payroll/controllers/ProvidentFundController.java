package com.example.payroll.controllers;

import com.example.payroll.dto.ProvidentFundDto;
import com.example.payroll.dto.request.ProvidentFundSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.services.payroll.ProvidentFundService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/provident-fund")
public class ProvidentFundController {
    @Autowired
    ProvidentFundService employeeProvidentFundService;

    @GetMapping()
    public ServiceResponse getPFData(ProvidentFundSearchCriteria criteria, @PageableDefault(value = 12) Pageable pageable) throws GenericException {
        return Utils.pageToServiceResponse(employeeProvidentFundService.getPFInfoWithSearchCriteria(criteria, pageable), ProvidentFundDto.class);
    }
}
