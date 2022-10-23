package com.example.payroll.services;

import com.example.payroll.dto.request.UserSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.security_oauth2.models.security.Authority;
import com.example.payroll.security_oauth2.models.security.User;
import com.example.payroll.security_oauth2.repository.AuthorityRepository;
import com.example.payroll.security_oauth2.repository.UserRepository;
import com.example.payroll.specifications.EmployeeSearchSpecifications;
import com.example.payroll.utils.Defs;
import com.example.payroll.utils.Role;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    @Qualifier("userPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    private User addOauthUser(Employee employee, String password) throws GenericException {
        Optional<User> optionalUser = userRepository.findByUsername(employee.getEmail());
        if(optionalUser.isPresent())throw new GenericException(Defs.USER_ALREADY_EXISTS);

        User user = new User();
        user.setUsername(employee.getEmail());
        user.setEnabled(true);

        Authority authority = authorityRepository.findByName(Role.USER.getValue());
        user.setAuthorities(Arrays.asList(authority));
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
        return user;
    }
    @Override
    public EmployeeDto enrollEmployee(EmployeeDto employeeDto) throws GenericException {
        Optional<Employee> optionalCustomUser = employeeRepository.findByUserId(employeeDto.getUserId());
        if(optionalCustomUser.isPresent())throw new GenericException(Defs.USER_ALREADY_EXISTS);

        Employee employee = new Employee();
        Utils.copyProperty(employeeDto, employee);

        User user = addOauthUser(employee, employeeDto.getPassword());
        employee.setOauthUser(user);
        employee.setStatus(true);

        employee.setCreatedBy(1l);
        employee.setCreateTime(LocalDateTime.now());

        employee = employeeRepository.save(employee);

        Utils.copyProperty(employee, employeeDto);

        return employeeDto;
    }

    @Override
    public EmployeeDto findByUsername(String username) throws GenericException{

        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(username);
        if(!optionalEmployee.isPresent() || optionalEmployee.get().getStatus().equals(false)){
            return null;
        }
        EmployeeDto employeeDto = new EmployeeDto();
        Utils.copyProperty(optionalEmployee, employeeDto);
        return employeeDto;
    }
    @Override
    public EmployeeDto findEmployeeById(Long id) throws GenericException{
        Optional<Employee> optionalUser = employeeRepository.findById(id);

        if(!optionalUser.isPresent() || optionalUser.get().getStatus().equals(false)){
            throw new GenericException(Defs.USER_NOT_FOUND);
        }else{
            EmployeeDto employeeDto = new EmployeeDto();
            Utils.copyProperty(optionalUser.get(), employeeDto);
            return employeeDto;
        }
    }

    @Override
    public EmployeeDto updateEmployeeById(Long id, EmployeeDto employeeDto) throws GenericException{
        Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();
        if(loggedInEmployee.isPresent() && !loggedInEmployee.get().getId().equals(id)){
            throw new GenericException(Defs.NO_PERMISSION_TO_UPDATE);
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(!optionalEmployee.isPresent() || optionalEmployee.get().getStatus().equals(false)) throw new GenericException(Defs.USER_NOT_FOUND);

        Employee employee = optionalEmployee.get();
        if(!Utils.isNullOrEmpty(employeeDto.getFirstName())){
            employee.setFirstName(employeeDto.getFirstName());
        }
        if(!Utils.isNullOrEmpty(employeeDto.getLastName())){
            employee.setLastName(employeeDto.getLastName());
        }
        employee = employeeRepository.save(employee);

        Utils.copyProperty(employee, employeeDto);
        return employeeDto;
    }

    @Override
    public Page<Employee> getEmployeeList(UserSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException{
        Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();
        Long id = null;
        if(loggedInEmployee.isPresent()){
            id = loggedInEmployee.get().getId();
        }

        Page<Employee> userPage = employeeRepository.findAll(
                EmployeeSearchSpecifications.withId(id==null ? criteria.getId() : id)
                        .and(EmployeeSearchSpecifications.withFirstName(criteria.getFirstName()))
                        .and(EmployeeSearchSpecifications.withLastName(criteria.getLastName()))
                        .and(EmployeeSearchSpecifications.withEmail(criteria.getEmail()))
                        .and(EmployeeSearchSpecifications.withPhone(criteria.getPhone()))
                        .and(EmployeeSearchSpecifications.withStatus(true))
                ,pageable
        );
        return userPage;
    }

    @Override
    public Boolean deleteEmployeeById(Long id) throws GenericException{
        Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(loggedInEmployee.isPresent() && optionalEmployee.isPresent() && !loggedInEmployee.get().getId().equals(optionalEmployee.get().getId())){
            throw new GenericException(Defs.NO_PERMISSION_TO_DELETE);
        }
        if(!optionalEmployee.isPresent()) throw new GenericException(Defs.USER_NOT_FOUND);

        Employee employee = optionalEmployee.get();
        employee.setStatus(false);
        try {
            employee = employeeRepository.save(employee);
            User user = employee.getOauthUser();
            user.setEnabled(false);
            userRepository.save(user);
        }catch (Exception e){
            throw new GenericException(Defs.EXCEPTION_OCCURRED_WHILE_SAVING_USER_INFO);
        }
        return true;
    }
}
