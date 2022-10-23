package com.example.payroll.services.payroll;

import com.example.payroll.dto.EmployeeModel;
import com.example.payroll.dto.request.EmployeeSearchCriteria;
import com.example.payroll.model.payroll.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EmployeeService {
    EmployeeModel addEmployee(EmployeeModel employeeModel) throws Exception;
    Page<Employee> searchEmployee(EmployeeSearchCriteria criteria, Pageable pageable);

}
