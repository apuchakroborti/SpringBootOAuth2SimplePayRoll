package com.example.payroll.controllers;

import com.example.payroll.dto.AuthorityModel;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.services.payroll.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class AccessController {
    @Autowired
    AuthorityService authorityService;

    /*
     * This API is for adding new authority
     * name must ne given
     */
    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping("/addNewRoll")
    public ServiceResponse<AuthorityModel> addNewAuthority(@Valid @RequestBody AuthorityModel authorityModel) throws GenericException {
        return authorityService.addNewAuthority(authorityModel);
    }
}
