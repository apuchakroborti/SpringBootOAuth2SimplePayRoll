package com.example.payroll.services;

import com.example.payroll.dto.request.EmployeeSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.models.payroll.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    ServiceResponse<EmployeeDto> enrollEmployee(EmployeeDto user) throws GenericException;

    EmployeeDto findByUsername(String username) throws GenericException;
    ServiceResponse<EmployeeDto> findEmployeeById(Long id) throws GenericException;
    ServiceResponse<EmployeeDto> updateEmployeeById(Long id, EmployeeDto employeeBean) throws GenericException;
    Page<Employee> getEmployeeList(EmployeeSearchCriteria criteria, Pageable pageable) throws GenericException;
    ServiceResponse<Boolean>  deleteEmployeeById(Long id) throws GenericException;
}
