package com.example.payroll.controllers;

import com.example.payroll.dto.RoleModel;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.services.payroll.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class AccessController {
    @Autowired
    RoleService roleService;

    @PostMapping("/addNewRoll")
    public ServiceResponse addNewRoll(@RequestBody RoleModel roleModel) throws Exception{
        return new ServiceResponse(null, roleService.addNewRoll(roleModel), null);
    }
}
