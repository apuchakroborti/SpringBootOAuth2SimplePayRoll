package com.example.payroll.services.payroll;

import com.example.payroll.dto.AuthorityModel;


public interface AuthorityService {
    AuthorityModel addNewAuthority(AuthorityModel authorityModel) throws Exception;
}
