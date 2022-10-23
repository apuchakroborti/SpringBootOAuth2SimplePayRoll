package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeModel;
import com.example.payroll.dto.request.LoginRequestDto;
import com.example.payroll.model.payroll.Employee;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.services.payroll.LoginService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    @Qualifier("userPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @Override
    public EmployeeModel checkLoginUser(LoginRequestDto loginRequestDto) throws Exception {
        EmployeeModel response = new EmployeeModel();

        if(loginRequestDto.getUsername() ==null || loginRequestDto.getUsername().trim().length() ==0){
            response = null;
        }

        if(loginRequestDto.getPassword()==null || loginRequestDto.getPassword().trim().length()==0){
            response = null;
        }

        if(loginRequestDto.getUsername() !=null && loginRequestDto.getPassword() !=null){
            Employee loginUser = employeeRepository.findByUsername(loginRequestDto.getUsername());
            if(loginUser !=null){
                if(passwordEncoder.matches(loginRequestDto.getPassword(), loginUser.getPassword())) {
                    Util.copyProperty(loginUser, response);
                } else {
                    System.out.println("Password mismatch!!");
                }
            } else{
                System.out.println("User not found!!");
            }
        }

        return response;
    }
}
