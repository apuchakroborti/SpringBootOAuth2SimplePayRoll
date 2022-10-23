package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeSalaryDto;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.EmployeeSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeSalaryService {
    EmployeeSalaryDto insertSalaryData(EmployeeSalaryDto employeeSalaryDto) throws GenericException;
    Page<EmployeeSalary> getSalaryDataWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId, Pageable pageable)throws GenericException;
    EmployeeSalaryDto getCurrentSalaryByEmployeeId(Long employeeId)throws GenericException;
}
