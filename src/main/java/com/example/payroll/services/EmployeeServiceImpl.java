package com.example.payroll.services;

import com.example.payroll.dto.request.EmployeeSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.models.payroll.EmployeeSalary;
import com.example.payroll.models.payroll.MonthlyPaySlip;
import com.example.payroll.repository.payroll.MonthlyPaySlipRepository;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.repository.payroll.EmployeeSalaryRepository;
import com.example.payroll.security_oauth2.models.security.Authority;
import com.example.payroll.security_oauth2.models.security.User;
import com.example.payroll.security_oauth2.repository.AuthorityRepository;
import com.example.payroll.security_oauth2.repository.UserRepository;
import com.example.payroll.services.payroll.MonthlyPaySlipService;
import com.example.payroll.services.payroll.impls.MonthlyPaySlipServiceImpl;
import com.example.payroll.specifications.EmployeeSearchSpecifications;
import com.example.payroll.utils.Defs;
import com.example.payroll.utils.Role;
import com.example.payroll.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    MonthlyPaySlipService monthlyPaySlipService;

    @Autowired
    @Qualifier("userPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    private User addOauthUser(Employee employee, String password) throws GenericException {
        try {
            Optional<User> optionalUser = userRepository.findByUsername(employee.getEmail());
            if (optionalUser.isPresent()) {
                logger.error("User already exists, email: {}", employee.getEmail());
                throw new GenericException(Defs.USER_ALREADY_EXISTS);
            }

            User user = new User();
            user.setUsername(employee.getEmail());
            user.setEnabled(true);

            Authority authority = authorityRepository.findByName(Role.EMPLOYEE.getValue());
            user.setAuthorities(Arrays.asList(authority));
            user.setPassword(passwordEncoder.encode(password));

            userRepository.save(user);
            return user;
        }catch (GenericException e){
            throw e;
        }catch (Exception e){
            logger.error("Error occurred while creating oauth user!");
            throw new GenericException("Error occurred while creating oauth user!", e);
        }
    }
    @Override
    public ServiceResponse<EmployeeDto> enrollEmployee(EmployeeDto employeeDto) throws GenericException {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findByUserId(employeeDto.getUserId());
            if (optionalEmployee.isPresent()) throw new GenericException(Defs.USER_ALREADY_EXISTS);

            Employee employee = new Employee();
            Utils.copyProperty(employeeDto, employee);

            User user = addOauthUser(employee, employeeDto.getPassword());
            employee.setOauthUser(user);
            employee.setStatus(true);

            employee.setCreatedBy(1l);
            employee.setCreateTime(LocalDateTime.now());

            employee = employeeRepository.save(employee);

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

            try {
                //generate payslip for the current financial year
                monthlyPaySlipService.generatePayslipForCurrentFinancialYear(employee, employeeSalary, employee.getDateOfJoining());
            } catch (GenericException e) {
                logger.error("Exception occurred while generating payslip for the current financial year, message: {}", e.getMessage());
                throw e;
            }

            Utils.copyProperty(employee, employeeDto);
            return new ServiceResponse(Utils.getSuccessResponse(), employeeDto);
        }catch (GenericException e){
            throw e;
        }catch (Exception e){
            logger.error("Exception occurred while enrolling employee, message: {}", e.getMessage());
            throw new GenericException(e.getMessage(), e);
        }
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
    public ServiceResponse<EmployeeDto> findEmployeeById(Long id) throws GenericException{
        try {
            Optional<Employee> optionalUser = employeeRepository.findById(id);

            if (!optionalUser.isPresent() || optionalUser.get().getStatus().equals(false)) {
                return new ServiceResponse<>(Utils.getSingleErrorBadRequest(
                        new ArrayList<>(),
                        "employeeId", Defs.EMPLOYEE_NOT_FOUND,
                        "Please check employee id is correct"), null);
            } else {
                EmployeeDto employeeDto = new EmployeeDto();
                Utils.copyProperty(optionalUser.get(), employeeDto);
                return new ServiceResponse(Utils.getSuccessResponse(), employeeDto);
            }
        }catch (Exception e){
            logger.error("Error occurred while fetching employee by id: {}", id);
            throw new GenericException("Error occurred while fetching employee by id", e);
        }
    }

    @Override
    public ServiceResponse<EmployeeDto> updateEmployeeById(Long id, EmployeeDto employeeDto) throws GenericException{
        try {
            Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();
            if (loggedInEmployee.isPresent() && !loggedInEmployee.get().getId().equals(id)) {
                return new ServiceResponse<>(Utils.getSingleErrorBadRequest(
                        new ArrayList<>(),
                        null, Defs.NO_PERMISSION_TO_DELETE,
                        "Please check you have permission to do this operation!"), null);
            }

            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            if (!optionalEmployee.isPresent() || optionalEmployee.get().getStatus().equals(false)){
                return new ServiceResponse<>(Utils.getSingleErrorBadRequest(
                        new ArrayList<>(),
                        "employeeId", Defs.EMPLOYEE_NOT_FOUND,
                        "Please check employee id is correct"), null);
            }


            Employee employee = optionalEmployee.get();
            if (!Utils.isNullOrEmpty(employeeDto.getFirstName())) {
                employee.setFirstName(employeeDto.getFirstName());
            }
            if (!Utils.isNullOrEmpty(employeeDto.getLastName())) {
                employee.setLastName(employeeDto.getLastName());
            }
            employee = employeeRepository.save(employee);

            Utils.copyProperty(employee, employeeDto);

            return new ServiceResponse(Utils.getSuccessResponse(), employeeDto);
        }catch (Exception e){
         logger.error("Exception occurred while updating employee, id: {}", id);
         throw new GenericException(e.getMessage(), e);
        }
    }

    @Override
    public Page<Employee> getEmployeeList(EmployeeSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException{
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
    public ServiceResponse<Boolean> deleteEmployeeById(Long id) throws GenericException{
        try {
            Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            if (loggedInEmployee.isPresent() && optionalEmployee.isPresent() &&
                    !loggedInEmployee.get().getId().equals(optionalEmployee.get().getId())) {

                return new ServiceResponse<>(Utils.getSingleErrorBadRequest(
                        new ArrayList<>(),
                        null, Defs.NO_PERMISSION_TO_DELETE,
                        "Please check you have permission to do this operation!"), null);
            }
            if (!optionalEmployee.isPresent()) {
                return new ServiceResponse<>(Utils.getSingleErrorBadRequest(
                        new ArrayList<>(),
                        "employeeId", Defs.EMPLOYEE_NOT_FOUND,
                        "Please check employee id is correct"), null);
            }

            Employee employee = optionalEmployee.get();
            employee.setStatus(false);
            try {
                employee = employeeRepository.save(employee);
                User user = employee.getOauthUser();
                user.setEnabled(false);
                userRepository.save(user);
            } catch (Exception e) {
                logger.error(Defs.EXCEPTION_OCCURRED_WHILE_SAVING_USER_INFO+", message: {}", e.getMessage());
                throw new GenericException(Defs.EXCEPTION_OCCURRED_WHILE_SAVING_USER_INFO, e);
            }
            return new ServiceResponse(Utils.getSuccessResponse(), true);
        } catch (GenericException e){
            throw e;
        }catch (Exception e){
            logger.error("Exception occurred while deleting user, user id: {}", id);
            throw new GenericException(e.getMessage(), e);
        }
    }
}
