package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.dto.request.LoginRequestDto;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.security_oauth2.models.security.User;
import com.example.payroll.security_oauth2.repository.UserRepository;
import com.example.payroll.services.payroll.LoginService;
import com.example.payroll.utils.Defs;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @Override
    public EmployeeDto checkLoginUser(LoginRequestDto loginRequestDto) throws GenericException {
        EmployeeDto employeeDto = new EmployeeDto();


        Optional<User> optionalUser = userRepository.findByUsername(loginRequestDto.getUsername());
        if(optionalUser.isPresent()){
            if(passwordEncoder.matches(loginRequestDto.getPassword(), optionalUser.get().getPassword())) {
                Optional<Employee> optionalEmployee = employeeRepository.findByEmail(loginRequestDto.getUsername());
                if(optionalEmployee.isPresent()){
                    Util.copyProperty(optionalEmployee.get(), employeeDto);
                }else{
                    employeeDto.setFirstName("Admin");
                    employeeDto.setLastName("Admin");
                }
            } else {
                throw new GenericException(Defs.PASSWORD_MISMATCHED);
            }
        }else{
            throw new GenericException(Defs.USER_NOT_FOUND);
        }
        return employeeDto;
    }
}
