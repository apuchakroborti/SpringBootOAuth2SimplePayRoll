package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.dto.request.LoginRequestDto;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.entity.payroll.Employee;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.security_oauth2.models.security.User;
import com.example.payroll.security_oauth2.repository.UserRepository;
import com.example.payroll.services.payroll.LoginService;
import com.example.payroll.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @Override
    public ServiceResponse<EmployeeDto> checkLoginUser(LoginRequestDto loginRequestDto) throws GenericException {
        try {
            EmployeeDto employeeDto = new EmployeeDto();

            Optional<User> optionalUser = userRepository.findByUsername(loginRequestDto.getUsername());
            if (optionalUser.isPresent()) {
                if (passwordEncoder.matches(loginRequestDto.getPassword(), optionalUser.get().getPassword())) {
                    Optional<Employee> optionalEmployee = employeeRepository.findByEmail(loginRequestDto.getUsername());
                    if (optionalEmployee.isPresent()) {
                        Utils.copyProperty(optionalEmployee.get(), employeeDto);
                    } else {
                        employeeDto.setFirstName("Admin");
                        employeeDto.setLastName("Admin");
                    }
                } else {
                    return new ServiceResponse<>(Utils.getSingleErrorBadRequest(
                            new ArrayList<>(),
                            "username, password", null,
                            "Please check username or password is incorrect"), null);
                }
            } else {
                return new ServiceResponse<>(Utils.getSingleErrorBadRequest(
                        new ArrayList<>(),
                        "username, password", null,
                        "Please check username or password is incorrect"), null);
            }
            return new ServiceResponse(Utils.getSuccessResponse(), employeeDto);
        }catch (Exception e){
            logger.error("Exception occurred while checking login user, message: {}", e.getMessage());
            throw new GenericException(e.getMessage(), e);
        }
    }
}
