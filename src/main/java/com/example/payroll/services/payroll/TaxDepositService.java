package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.dto.request.TaxSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.models.payroll.EmployeeTaxDeposit;
import com.example.payroll.models.payroll.MonthlyPaySlip;
import com.example.payroll.utils.TaxType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface TaxDepositService {
    ServiceResponse<EmployeeTaxDepositDto> insertIndividualTaxInfo(EmployeeTaxDepositDto employeeTaxDepositDto)throws GenericException;
    ServiceResponse<EmployeeTaxDeposit> insertPayslipTaxInfo(MonthlyPaySlip monthlyPaySlip,
                                            Employee employee,
                                            Double taxToDepositForTheRequestMonth,
                                            TaxType taxType,
                                            LocalDate fromDate,
                                            LocalDate toDate)throws GenericException;
    Page<EmployeeTaxDeposit> getAllTaxInfoByEmployeeId(Long employeeId, Pageable pageable);
    Page<EmployeeTaxDeposit> getTaxInfoWithInDateRangeAndEmployeeId(TaxSearchCriteria criteria, Pageable pageable);
}
