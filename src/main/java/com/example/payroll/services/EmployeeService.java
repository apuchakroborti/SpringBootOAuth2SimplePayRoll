package com.example.payroll.services;

import com.example.payroll.dto.request.EmployeeSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.entity.payroll.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeDto enrollEmployee(EmployeeDto employeeDto) throws GenericException;

    EmployeeDto findByUsername(String username) throws GenericException;
    EmployeeDto findEmployeeById(Long id) throws GenericException;
    EmployeeDto updateEmployeeById(Long id, EmployeeDto employeeBean) throws GenericException;
    Page<Employee> getEmployeeList(EmployeeSearchCriteria criteria, Pageable pageable) throws GenericException;
    Boolean  deleteEmployeeById(Long id) throws GenericException;
}
