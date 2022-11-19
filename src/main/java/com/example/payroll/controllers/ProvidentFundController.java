package com.example.payroll.controllers;

import com.example.payroll.dto.ProvidentFundDto;
import com.example.payroll.dto.request.ProvidentFundSearchCriteria;
import com.example.payroll.dto.response.Pagination;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.entity.payroll.ProvidentFund;
import com.example.payroll.services.payroll.ProvidentFundService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/provident-fund")
public class ProvidentFundController {
    @Autowired
    ProvidentFundService employeeProvidentFundService;
    @GetMapping
    public ServiceResponse<Page<ProvidentFundDto>> getPFData(ProvidentFundSearchCriteria criteria, @PageableDefault(value = 12) Pageable pageable) throws GenericException {

        Page<ProvidentFund> providentFundPage = employeeProvidentFundService.getPFInfoWithSearchCriteria(criteria, pageable);

        return new ServiceResponse(Utils.getSuccessResponse(),
                Utils.toDtoList(providentFundPage, ProvidentFundDto.class),
                new Pagination(providentFundPage.getTotalElements(),
                        providentFundPage.getNumberOfElements(),
                        providentFundPage.getNumber(),
                        providentFundPage.getSize()));
    }
}
