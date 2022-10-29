package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeSalaryDto;
import com.example.payroll.dto.request.SalarySearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.EmployeeSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SalaryService {
    EmployeeSalaryDto updateSalaryData(EmployeeSalaryDto employeeSalaryDto) throws GenericException;
    Page<EmployeeSalary> getSalaryDataWithInDateRangeAndEmployeeId(SalarySearchCriteria searchCriteria, Pageable pageable)throws GenericException;
    EmployeeSalaryDto getCurrentSalaryByEmployeeId(Long employeeId)throws GenericException;
}
