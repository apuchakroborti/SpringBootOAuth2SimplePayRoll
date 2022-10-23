package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeModel;
import com.example.payroll.dto.request.EmployeeSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.model.payroll.Employee;
import com.example.payroll.services.payroll.EmployeeService;
import com.example.payroll.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/enroll")
    public ServiceResponse enrollEmployee(@RequestBody EmployeeModel employeeModel) throws Exception{
        return new ServiceResponse(null, employeeService.addEmployee(employeeModel), null);
    }
    @GetMapping("/search")
    public ServiceResponse searchEmployee(EmployeeSearchCriteria employeeSearchCriteria, @PageableDefault(value = 2000) Pageable pageable){
        Page<Employee> employeePage = employeeService.searchEmployee(employeeSearchCriteria, pageable);

        return Converter.pageToServiceResponse(employeePage);
    }
}
