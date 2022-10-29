package com.example.payroll.controllers;

import com.example.payroll.dto.AuthorityModel;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.services.payroll.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class AccessController {
    @Autowired
    AuthorityService authorityService;

    //TODO need to add description about this endpoint
    @PostMapping("/addNewRoll")
    public ServiceResponse addNewAuthority(@RequestBody AuthorityModel authorityModel) throws Exception{
        return new ServiceResponse(null, authorityService.addNewAuthority(authorityModel), null);
    }
}
