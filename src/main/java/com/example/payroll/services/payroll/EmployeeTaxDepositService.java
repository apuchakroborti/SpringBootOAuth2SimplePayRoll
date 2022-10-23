package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.models.payroll.EmployeeTaxDeposit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface EmployeeTaxDepositService {
    EmployeeTaxDepositDto insertTaxInfo(EmployeeTaxDepositDto employeeTaxDepositDto);
    Page<EmployeeTaxDeposit> getAllTaxInfoByEmployeeId(Long employeeId, Pageable pageable);
    Page<EmployeeTaxDeposit> getTaxInfoWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId, Pageable pageable);
}
