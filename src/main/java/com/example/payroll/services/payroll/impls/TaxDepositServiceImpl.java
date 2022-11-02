package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.dto.request.TaxSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.models.payroll.EmployeeTaxDeposit;
import com.example.payroll.models.payroll.MonthlyPaySlip;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.repository.payroll.TaxDepositRepository;
import com.example.payroll.services.payroll.TaxDepositService;
import com.example.payroll.utils.Defs;
import com.example.payroll.utils.TaxType;
import com.example.payroll.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class TaxDepositServiceImpl implements TaxDepositService {
    Logger logger = LoggerFactory.getLogger(TaxDepositServiceImpl.class);

    @Autowired
    TaxDepositRepository taxDepositRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    //An employee can save his/her tax info only
    @Override
    public ServiceResponse<EmployeeTaxDepositDto> insertIndividualTaxInfo(EmployeeTaxDepositDto employeeTaxDepositDto) throws GenericException{
        try {
            Optional<Employee> employee = employeeRepository.getLoggedInEmployee();

            Optional<Employee> optionalEmployee = employeeRepository.findById(
                    employee.isPresent() ? employee.get().getId(): employeeTaxDepositDto.getEmployee().getId()
            );
            if (!optionalEmployee.isPresent()){
                return new ServiceResponse<>(Utils.getSingleErrorBadRequest(
                        new ArrayList<>(),
                        "employee", Defs.EMPLOYEE_NOT_FOUND,
                        "Please check employee id is correct"), null);
            }

            EmployeeTaxDeposit employeeTaxDeposit = new EmployeeTaxDeposit();
            Utils.copyProperty(employeeTaxDepositDto, employeeTaxDeposit);

            employeeTaxDeposit.setEmployee(optionalEmployee.get());
            employeeTaxDeposit.setCreatedBy(1L);
            employeeTaxDeposit.setCreateTime(LocalDateTime.now());

            employeeTaxDeposit = taxDepositRepository.save(employeeTaxDeposit);

            logger.info("Individual tax info saved employeeId: {}", employeeTaxDepositDto.getEmployee().getId());

            Utils.copyProperty(employeeTaxDeposit, employeeTaxDepositDto);

            return new ServiceResponse(Utils.getSuccessResponse(), employeeTaxDepositDto);
        }catch (Exception e){
            logger.error("Error occurred while inserting individual tax info, employeeId: {}", employeeTaxDepositDto.getEmployee().getId());
            throw new GenericException("Internal server error", e);
        }
    }

    //An employee can save his/her tax info only
    @Override
    public ServiceResponse<EmployeeTaxDeposit> insertPayslipTaxInfo(MonthlyPaySlip monthlyPaySlip, Employee employee,
                                                   Double taxToDepositForTheRequestMonth, TaxType taxType,
                                                   LocalDate fromDate, LocalDate toDate)throws GenericException{
        try {
            Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();

            Optional<EmployeeTaxDeposit> optionalTaxDeposit = taxDepositRepository.findByEmployeeIdAndFromDateAndToDateAndTaxType(
                    loggedInEmployee.isPresent() ? loggedInEmployee.get().getId() : employee.getId(),
                    fromDate, toDate, taxType);

            if (optionalTaxDeposit.isPresent()){
                return new ServiceResponse(Utils.getSuccessResponse(), optionalTaxDeposit.get());
            }

            EmployeeTaxDeposit employeeTaxDeposit = new EmployeeTaxDeposit();

            employeeTaxDeposit.setMonthlyPaySlip(monthlyPaySlip);
            employeeTaxDeposit.setEmployee(employee);
            employeeTaxDeposit.setTaxType(taxType);
            employeeTaxDeposit.setAmount(taxToDepositForTheRequestMonth);

            //TODO need to take input chalaNo from input or need to update later
            employeeTaxDeposit.setChalanNo("ABCD123");
            employeeTaxDeposit.setComments("Auto deduction of monthly tax by company");
            employeeTaxDeposit.setFromDate(fromDate);
            employeeTaxDeposit.setToDate(toDate);

            employeeTaxDeposit.setCreatedBy(1l);
            employeeTaxDeposit.setCreateTime(LocalDateTime.now());

            employeeTaxDeposit = taxDepositRepository.save(employeeTaxDeposit);

            return new ServiceResponse(Utils.getSuccessResponse(), employeeTaxDeposit);
        }catch (Exception e){
            throw new GenericException("Exception occurred while saving tax deposit info!");
        }
    }

    //An employee can see his/her tax info only
    @Override
    public Page<EmployeeTaxDeposit> getAllTaxInfoByEmployeeId(Long employeeId, Pageable pageable){
        Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();

        Page<EmployeeTaxDeposit> employeeTaxDepositPage = taxDepositRepository.findAllByEmployeeId(
                loggedInEmployee.isPresent()?loggedInEmployee.get().getId():employeeId, pageable);
        return employeeTaxDepositPage;
    }
    //An employee can see his/her tax info only
    @Override
    public Page<EmployeeTaxDeposit> getTaxInfoWithInDateRangeAndEmployeeId(TaxSearchCriteria criteria, Pageable pageable){
        Optional<Employee> loggedInEmployee = employeeRepository.getLoggedInEmployee();
        Page<EmployeeTaxDeposit> taxInfoPage = taxDepositRepository.getAllByEmployeeIdAndFromDateAndToDate(
                criteria.getFromDate(), criteria.getToDate(), loggedInEmployee.isPresent()?loggedInEmployee.get().getId():criteria.getEmployeeId(), pageable);

        return taxInfoPage;
    }
}
