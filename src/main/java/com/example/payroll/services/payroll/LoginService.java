package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.dto.request.LoginRequestDto;
import com.example.payroll.exceptions.GenericException;

public interface LoginService {
    EmployeeDto checkLoginUser(LoginRequestDto loginRequestDto) throws GenericException;

}
