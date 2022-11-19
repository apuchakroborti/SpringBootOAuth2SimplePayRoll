package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeSalaryDto;
import com.example.payroll.dto.request.SalarySearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.entity.payroll.EmployeeSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SalaryService {
    ServiceResponse<EmployeeSalaryDto> updateSalaryData(EmployeeSalaryDto employeeSalaryDto) throws GenericException;
    Page<EmployeeSalary> getSalaryDataWithInDateRangeAndEmployeeId(SalarySearchCriteria searchCriteria, Pageable pageable)throws GenericException;
    ServiceResponse<EmployeeSalaryDto> getCurrentSalaryByEmployeeId(Long employeeId)throws GenericException;
}
