package com.example.payroll.services;

import com.example.payroll.dto.request.EmployeeSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.EmployeeNotFoundException;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.entity.payroll.Employee;
import com.example.payroll.entity.payroll.EmployeeSalary;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.repository.payroll.EmployeeSalaryRepository;
import com.example.payroll.security_oauth2.models.security.Authority;
import com.example.payroll.security_oauth2.models.security.User;
import com.example.payroll.security_oauth2.repository.AuthorityRepository;
import com.example.payroll.security_oauth2.repository.UserRepository;
import com.example.payroll.services.payroll.MonthlyPaySlipService;
import com.example.payroll.specifications.EmployeeSearchSpecifications;
import com.example.payroll.utils.Defs;
import com.example.payroll.utils.Role;
import com.example.payroll.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final EmployeeSalaryRepository employeeSalaryRepository;
    private final MonthlyPaySlipService monthlyPaySlipService;

    @Qualifier("userPasswordEncoder")
    private final PasswordEncoder passwordEncoder;

    @Autowired
    EmployeeServiceImpl(EmployeeRepository employeeRepository,
            UserRepository userRepository,
            AuthorityRepository authorityRepository,
            EmployeeSalaryRepository employeeSalaryRepository,
            MonthlyPaySlipService monthlyPaySlipService,
            @Qualifier("userPasswordEncoder")PasswordEncoder passwordEncoder){
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.employeeSalaryRepository = employeeSalaryRepository;
        this.monthlyPaySlipService = monthlyPaySlipService;
        this.passwordEncoder = passwordEncoder;
    }


    private User addOauthUser(Employee employee, String password) throws GenericException {
        try {
            log.info("EmployeeServiceImpl::addOauthUser start: email: {}", employee.getEmail());
            Optional<User> optionalUser = userRepository.findByUsername(employee.getEmail());
            if (optionalUser.isPresent()) {
                log.error("EmployeeServiceImpl::addOauthUser user already exists: email: {}", employee.getEmail());
                throw new GenericException(Defs.USER_ALREADY_EXISTS);
            }

            User user = new User();
            user.setUsername(employee.getEmail());
            user.setEnabled(true);

            Authority authority = authorityRepository.findByName(Role.EMPLOYEE.getValue());
            user.setAuthorities(Arrays.asList(authority));
            user.setPassword(passwordEncoder.encode(password));

            user = userRepository.save(user);
            log.debug("EmployeeServiceImpl::addOauthUser user: {}", user.toString());
            log.info("EmployeeServiceImpl::addOauthUser end: email: {}", employee.getEmail());
            return user;
        }catch (GenericException e){
            throw e;
        }catch (Exception e){
            log.error("EmployeeServiceImpl::addOauthUser Exception occurred while adding oauth user email:{} message: {}",employee.getEmail(), e.getMessage());
            throw new GenericException("Error occurred while creating oauth user!");
        }
    }
    @Override
    public EmployeeDto enrollEmployee(EmployeeDto employeeDto) throws GenericException {
        try {
            log.info("EmployeeServiceImpl::enrollEmployee service start: userId: {} and email: {}", employeeDto.getUserId(), employeeDto.getEmail());
            Optional<Employee> optionalEmployee = employeeRepository.findByUserId(employeeDto.getUserId());
            if (optionalEmployee.isPresent()){
                log.error("EmployeeServiceImpl::enrollEmployee service:  userId: {} already exists", employeeDto.getUserId());
                throw new GenericException(Defs.USER_ALREADY_EXISTS);
            }

            Employee employee = new Employee();
            Utils.copyProperty(employeeDto, employee);

            User user = addOauthUser(employee, employeeDto.getPassword());
            log.debug("EmployeeServiceImpl::enrollEmployee service:  user: {} ", user.toString());

            employee.setOauthUser(user);
            employee.setStatus(true);

            employee.setCreatedBy(1l);
            employee.setCreateTime(LocalDateTime.now());

            employee = employeeRepository.save(employee);
            log.debug("EmployeeServiceImpl::enrollEmployee service:  employee: {} ", employee.toString());

            EmployeeSalary employeeSalary = new EmployeeSalary();
            employeeSalary.setEmployee(employee);
            employeeSalary.setGrossSalary(employeeDto.getGrossSalary());
            employeeSalary.setBasicSalary(employeeSalary.getGrossSalary() * 0.6);
            employeeSalary.setComments("Joining Salary");
            employeeSalary.setFromDate(employeeDto.getDateOfJoining());
            employeeSalary.setStatus(true);

            employeeSalary.setCreatedBy(1L);
            employeeSalary.setCreateTime(LocalDateTime.now());

            employeeSalary = employeeSalaryRepository.save(employeeSalary);
            log.debug("EmployeeServiceImpl::enrollEmployee service:  employeeSalary: {} ", employeeSalary.toString());


            //generate payslip for the current financial year
            monthlyPaySlipService.generatePayslipForCurrentFinancialYear(employee, employeeSalary, employee.getDateOfJoining());
            log.debug("EmployeeServiceImpl::enrollEmployee service:  monthly payslip generation successful employee id: {}, email: {} ", employee.getId(), employee.getEmail());

            Utils.copyProperty(employee, employeeDto);
            log.info("EmployeeServiceImpl::enrollEmployee service end: userId: {} and email: {}", employeeDto.getUserId(), employeeDto.getEmail());
            return employeeDto;
        }catch (GenericException e){
            throw e;
        }catch (Exception e){
            log.error("Exception occurred while enrolling employee, message: {}", e.getMessage());
            throw new GenericException(e.getMessage(), e);
        }
    }



    @Override
    public EmployeeDto findByUsername(String username) throws GenericException{
        try {
            log.debug("EmployeeServiceImpl::findByUsername start:  username: {} ", username);
            Optional<Employee> optionalEmployee = employeeRepository.findByEmail(username);
            if (!optionalEmployee.isPresent() || optionalEmployee.get().getStatus().equals(false)) {
                log.debug("EmployeeServiceImpl::findByUsername user not found by  username: {} ", username);
                return null;
            }
            EmployeeDto employeeDto = new EmployeeDto();
            Utils.copyProperty(optionalEmployee.get(), employeeDto);
            log.debug("EmployeeServiceImpl::findByUsername end:  username: {} ", username);
            return employeeDto;
        }catch (Exception e){
            log.error("Error while finding employee by username: {}", username);
            throw new GenericException(e.getMessage());
        }
    }
    @Override
    public EmployeeDto findEmployeeById(Long id) throws GenericException{
        try {
            log.info("EmployeeServiceImpl::findEmployeeById start:  id: {} ", id);
            Optional<Employee> optionalUser = employeeRepository.findById(id);

            if (!optionalUser.isPresent() || optionalUser.get().getStatus().equals(false)) {
                log.debug("EmployeeServiceImpl::findEmployeeById employee not found by id: {} ", id);
                throw new EmployeeNotFoundException(Defs.EMPLOYEE_NOT_FOUND);
            }
            EmployeeDto employeeDto = new EmployeeDto();
            Utils.copyProperty(optionalUser.get(), employeeDto);
            log.info("EmployeeServiceImpl::findEmployeeById end: id: {} ", id);
            return employeeDto;

        }catch (Exception e){
            log.error("Error occurred while fetching employee by id: {}", id);
            throw new GenericException("Error occurred while fetching employee by id", e);
        }
    }

    @Override
    public EmployeeDto updateEmployeeById(Long id, EmployeeDto employeeDto) throws GenericException{
        try {
            log.debug("EmployeeServiceImpl::updateEmployeeById start:  id: {} ", id);

            Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();
            if (loggedInEmployee.isPresent() && !loggedInEmployee.get().getId().equals(id)) {
                throw new GenericException("No permission to up update!");
            }

            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            if (!optionalEmployee.isPresent() || optionalEmployee.get().getStatus().equals(false)){
                log.debug("EmployeeServiceImpl::updateEmployeeById employee not found by id: {} ", id);
                throw new EmployeeNotFoundException(Defs.EMPLOYEE_NOT_FOUND);
            }


            Employee employee = optionalEmployee.get();
            if (!Utils.isNullOrEmpty(employeeDto.getFirstName())) {
                employee.setFirstName(employeeDto.getFirstName());
            }
            if (!Utils.isNullOrEmpty(employeeDto.getLastName())) {
                employee.setLastName(employeeDto.getLastName());
            }
            employee = employeeRepository.save(employee);
            log.debug("EmployeeServiceImpl::updateEmployeeById employee update successful:  employee: {} ", employee.toString());

            Utils.copyProperty(employee, employeeDto);
            log.debug("EmployeeServiceImpl::updateEmployeeById end:  id: {} ", id);

            return employeeDto;
        }catch (Exception e){
         log.error("Exception occurred while updating employee, id: {}", id);
         throw new GenericException(e.getMessage(), e);
        }
    }

    @Override
    public Page<Employee> getEmployeeList(EmployeeSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException{
        try {
            log.info("EmployeeServiceImpl::getEmployeeList start:  criteria: {} ", Utils.jsonAsString(criteria));

            Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();
            Long id = null;
            if (loggedInEmployee.isPresent()) {
                id = loggedInEmployee.get().getId();
            }

            Page<Employee> userPage = employeeRepository.findAll(
                    EmployeeSearchSpecifications.withId(id == null ? criteria.getId() : id)
                            .and(EmployeeSearchSpecifications.withFirstName(criteria.getFirstName()))
                            .and(EmployeeSearchSpecifications.withLastName(criteria.getLastName()))
                            .and(EmployeeSearchSpecifications.withEmail(criteria.getEmail()))
                            .and(EmployeeSearchSpecifications.withPhone(criteria.getPhone()))
                            .and(EmployeeSearchSpecifications.withStatus(true))
                    , pageable
            );
            log.debug("EmployeeServiceImpl::getEmployeeList number of elements: {} ", userPage.getTotalElements());

            log.info("EmployeeServiceImpl::getEmployeeList end");
            return userPage;
        }catch (Exception e){
            log.error("EmployeeServiceImpl::getEmployeeList exception occurred while fetching user list!");
            throw new GenericException("exception occurred while fetching user list!");
        }
    }

    @Override
    public Boolean deleteEmployeeById(Long id) throws GenericException{
        try {
            log.info("EmployeeServiceImpl::deleteEmployeeById start:  id: {} ", id);

            Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            if (loggedInEmployee.isPresent() && optionalEmployee.isPresent() &&
                    !loggedInEmployee.get().getId().equals(optionalEmployee.get().getId())) {
                throw new GenericException(Defs.NO_PERMISSION_TO_DELETE);
            }
            if (!optionalEmployee.isPresent()) {
                throw new EmployeeNotFoundException(Defs.EMPLOYEE_NOT_FOUND);
            }

            Employee employee = optionalEmployee.get();
            employee.setStatus(false);

            //soft deletion of employee
            employee = employeeRepository.save(employee);
            User user = employee.getOauthUser();
            user.setEnabled(false);
            userRepository.save(user);

            log.info("EmployeeServiceImpl::deleteEmployeeById end:  id: {} ", id);
            return  true;
        } catch (GenericException e){
            throw e;
        }catch (Exception e){
            log.error("Exception occurred while deleting user, user id: {}", id);
            throw new GenericException(e.getMessage(), e);
        }
    }
}
