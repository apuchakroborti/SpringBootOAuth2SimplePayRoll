package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.AuthorityModel;
import com.example.payroll.security_oauth2.models.security.Authority;
import com.example.payroll.security_oauth2.repository.AuthorityRepository;
import com.example.payroll.services.payroll.AuthorityService;
import com.example.payroll.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    Logger logger = LoggerFactory.getLogger(AuthorityServiceImpl.class);


    @Autowired
    AuthorityRepository authorityRepository;

    public AuthorityModel addNewAuthority(AuthorityModel authorityModel) throws Exception{
        Authority authority = new Authority();

        Utils.copyProperty(authorityModel, authority);

        authority = authorityRepository.save(authority);

        Utils.copyProperty(authority, authorityModel);
        return authorityModel;
    }
}
