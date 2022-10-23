package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeSalaryModel;
import com.example.payroll.exceptions.PayrollException;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeSalaryService {
    EmployeeSalaryModel insertSalaryData(EmployeeSalaryModel employeeSalaryModel) throws PayrollException;
    List<EmployeeSalaryModel> getSalaryDataWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId)throws PayrollException;
    EmployeeSalaryModel getCurrentSalaryByEmployeeId(Long employeeId)throws PayrollException;
}
