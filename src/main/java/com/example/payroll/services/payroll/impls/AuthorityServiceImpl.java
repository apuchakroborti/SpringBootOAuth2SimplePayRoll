package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.AuthorityModel;
import com.example.payroll.security_oauth2.models.security.Authority;
import com.example.payroll.security_oauth2.repository.AuthorityRepository;
import com.example.payroll.services.payroll.AuthorityService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    AuthorityRepository authorityRepository;

    public AuthorityModel addNewAuthority(AuthorityModel authorityModel) throws Exception{
        Authority authority = new Authority();

        Util.copyProperty(authorityModel, authority);

        authority = authorityRepository.save(authority);

        Util.copyProperty(authority, authorityModel);
        return authorityModel;
    }
}
