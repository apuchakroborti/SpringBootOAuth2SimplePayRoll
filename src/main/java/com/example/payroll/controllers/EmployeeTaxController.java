package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.dto.request.TaxSearchCriteria;
import com.example.payroll.dto.response.APIResponse;
import com.example.payroll.dto.response.Pagination;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.entity.payroll.EmployeeTaxDeposit;
import com.example.payroll.services.payroll.TaxDepositService;
import com.example.payroll.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/employee/tax")
@AllArgsConstructor
@Slf4j
public class EmployeeTaxController {

    private final TaxDepositService taxDepositService;

    @PostMapping
    public ResponseEntity<APIResponse> insertTaxInfo(@Valid @RequestBody EmployeeTaxDepositDto employeeTaxDepositModel) throws GenericException {
        log.info("EmployeeTaxController::insertTaxInfo request body {}", Utils.jsonAsString(employeeTaxDepositModel));

        EmployeeTaxDepositDto  employeeResponseDTO = taxDepositService.insertIndividualTaxInfo(employeeTaxDepositModel);
        log.debug("EmployeeTaxController::insertTaxInfo response {}", Utils.jsonAsString(employeeResponseDTO));

        //Builder Design pattern
        APIResponse<EmployeeTaxDepositDto> responseDTO = APIResponse
                .<EmployeeTaxDepositDto>builder()
                .status("SUCCESS")
                .results(employeeResponseDTO)
                .build();

        log.info("EmployeeController::insertTaxInfo response {}", Utils.jsonAsString(responseDTO));
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    @GetMapping("/{employeeId}")
    public ResponseEntity<APIResponse> getAllTaxInfoByEmployeeId(@PathVariable("employeeId") Long employeeId, @PageableDefault(value = 12) Pageable pageable) throws GenericException{
        log.info("EmployeeTaxController::getAllTaxInfoByEmployeeId requested employee id: {}", employeeId);

        Page<EmployeeTaxDeposit> taxDepositPage = taxDepositService.getAllTaxInfoByEmployeeId(employeeId, pageable);
        List<EmployeeTaxDepositDto> employeeTaxDepositDtoList = Utils.toDtoList(taxDepositPage, EmployeeTaxDepositDto.class);

        APIResponse<List<EmployeeTaxDepositDto>> responseDTO = APIResponse
                .<List<EmployeeTaxDepositDto>>builder()
                .status("SUCCESS")
                .results(employeeTaxDepositDtoList)
                .pagination(new Pagination(taxDepositPage.getTotalElements(), taxDepositPage.getNumberOfElements(), taxDepositPage.getNumber(), taxDepositPage.getSize()))
                .build();

        log.info("EmployeeTaxController::getAllTaxInfoByEmployeeId response: {}", Utils.jsonAsString(responseDTO));
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<APIResponse> getAllTaxInfo(TaxSearchCriteria criteria, @PageableDefault(value = 12) Pageable pageable) throws GenericException{
        log.info("EmployeeTaxController::getAllTaxInfo request criteria: {}", Utils.jsonAsString(criteria));
        Page<EmployeeTaxDeposit> taxDepositPage = taxDepositService.getTaxInfoWithInDateRangeAndEmployeeId(criteria, pageable);

        List<EmployeeTaxDepositDto> employeeTaxDepositDtoList = Utils.toDtoList(taxDepositPage, EmployeeTaxDepositDto.class);

        APIResponse<List<EmployeeTaxDepositDto>> responseDTO = APIResponse
                .<List<EmployeeTaxDepositDto>>builder()
                .status("SUCCESS")
                .results(employeeTaxDepositDtoList)
                .pagination(new Pagination(taxDepositPage.getTotalElements(), taxDepositPage.getNumberOfElements(), taxDepositPage.getNumber(), taxDepositPage.getSize()))
                .build();

        log.info("EmployeeTaxController::getAllTaxInfo response: {}", Utils.jsonAsString(responseDTO));
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
