package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeMonthlyPaySlipModel;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeMonthlyPaySlipService {
   EmployeeMonthlyPaySlipModel createPaySlip(EmployeeMonthlyPaySlipModel employeeMonthlyPaySlipModel);
   List<EmployeeMonthlyPaySlipModel> getPaySlipWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId);

}
