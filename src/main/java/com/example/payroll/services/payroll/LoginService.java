package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeModel;
import com.example.payroll.dto.request.LoginRequestDto;

public interface LoginService {
    EmployeeModel checkLoginUser(LoginRequestDto loginRequestDto) throws Exception;

}
