package com.example.payroll.controllers;

import com.example.payroll.dto.request.LoginRequestDto;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.services.payroll.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping
    public ServiceResponse checkLoginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) throws GenericException {
        return new ServiceResponse(null, loginService.checkLoginUser(loginRequestDto));
    }
}
