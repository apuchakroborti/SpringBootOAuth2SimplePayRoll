package com.example.payroll.services.payroll;

import com.example.payroll.dto.EmployeeProvidentFundModel;

import java.time.LocalDate;
import java.util.List;


public interface EmployeeProvidentFundService {
    EmployeeProvidentFundModel insertPfData(EmployeeProvidentFundModel employeeProvidentFundModel);
    List<EmployeeProvidentFundModel> getPFInfoWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId);
}
