package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.dto.request.TaxSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.EmployeeTaxDeposit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface EmployeeTaxDepositService {
    EmployeeTaxDepositDto insertTaxInfo(EmployeeTaxDepositDto employeeTaxDepositDto)throws GenericException;
    Page<EmployeeTaxDeposit> getAllTaxInfoByEmployeeId(Long employeeId, Pageable pageable);
    Page<EmployeeTaxDeposit> getTaxInfoWithInDateRangeAndEmployeeId(TaxSearchCriteria criteria, Pageable pageable);
}
