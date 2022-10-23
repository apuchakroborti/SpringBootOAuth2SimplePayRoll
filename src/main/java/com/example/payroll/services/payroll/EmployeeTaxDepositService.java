package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeTaxDepositModel;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeTaxDepositService {
    EmployeeTaxDepositModel insertTaxInfo(EmployeeTaxDepositModel employeeTaxDepositModel);
    List<EmployeeTaxDepositModel> getAllTaxInfoByEmployeeId(Long employeeId);
    List<EmployeeTaxDepositModel> getPFInfoWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId);
}
