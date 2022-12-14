package com.example.payroll.security_oauth2.security;


import com.example.payroll.exceptions.GenericException;
import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.security_oauth2.models.security.Authority;
import com.example.payroll.security_oauth2.models.security.User;
import com.example.payroll.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class CustomTokenEnhancer implements TokenEnhancer {

    Logger logger = LoggerFactory.getLogger(CustomTokenEnhancer.class);

    @Autowired
    private EmployeeService employeeService;


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        logger.info("CustomTokenEnhancer enhance called");
        User user = (User) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();

        EmployeeDto loginInfoDto = null;
        try {
            loginInfoDto = employeeService.findByUsername(user.getUsername());
        } catch (GenericException e) {
            e.printStackTrace();
        }
        if(loginInfoDto != null) {
            additionalInfo.put("id", loginInfoDto.getId());
            additionalInfo.put("userId", loginInfoDto.getUserId());
        }

        additionalInfo.put("authorities", user.getAuthorities()
                .stream().map(Authority::getAuthority)
                .collect(Collectors.toList()));

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }

}