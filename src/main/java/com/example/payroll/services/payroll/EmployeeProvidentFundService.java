package com.example.payroll.services.payroll;

import com.example.payroll.dto.EmployeeProvidentFundDto;
import com.example.payroll.models.payroll.EmployeeProvidentFund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface EmployeeProvidentFundService {
    EmployeeProvidentFundDto insertPfData(EmployeeProvidentFundDto employeeProvidentFundDto);
    Page<EmployeeProvidentFund> getPFInfoWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId, Pageable pageable);
}
