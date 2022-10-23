package com.example.payroll.services;

import com.example.payroll.dto.request.UserSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.models.payroll.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeDto enrollEmployee(EmployeeDto user) throws GenericException;

    EmployeeDto findByUsername(String username) throws GenericException;
    EmployeeDto findEmployeeById(Long id) throws GenericException;
    EmployeeDto updateEmployeeById(Long id, EmployeeDto employeeBean) throws GenericException;
    Page<Employee> getEmployeeList(UserSearchCriteria criteria, Pageable pageable) throws GenericException;
    Boolean deleteEmployeeById(Long id) throws GenericException;
}
