package com.example.payroll.services.payroll;

import com.example.payroll.dto.AuthorityModel;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;


public interface AuthorityService {
    ServiceResponse<AuthorityModel> addNewAuthority(AuthorityModel authorityModel) throws GenericException;
}
