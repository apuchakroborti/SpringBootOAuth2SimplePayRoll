package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.dto.request.TaxSearchCriteria;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TaxDepositServiceImpl implements TaxDepositService {

    @Autowired
    TaxDepositRepository taxDepositRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public EmployeeTaxDepositDto insertIndividualTaxInfo(EmployeeTaxDepositDto employeeTaxDepositDto) throws GenericException{
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeTaxDepositDto.getEmployee().getId());
        if(!optionalEmployee.isPresent())throw new GenericException(Defs.USER_NOT_FOUND);

        EmployeeTaxDeposit employeeTaxDeposit = new EmployeeTaxDeposit();
        Utils.copyProperty(employeeTaxDepositDto, employeeTaxDeposit);

        employeeTaxDeposit.setEmployee(optionalEmployee.get());
        employeeTaxDeposit.setCreatedBy(1L);
        employeeTaxDeposit.setCreateTime(LocalDateTime.now());

        employeeTaxDeposit = taxDepositRepository.save(employeeTaxDeposit);
        Utils.copyProperty(employeeTaxDeposit, employeeTaxDepositDto);
        return employeeTaxDepositDto;
    }
    @Override
    public EmployeeTaxDeposit insertPayslipTaxInfo(MonthlyPaySlip monthlyPaySlip, Employee employee,
                                                   Double taxToDepositForTheRequestMonth, TaxType taxType,
                                                   LocalDate fromDate, LocalDate toDate)throws GenericException{
        try {


            Optional<EmployeeTaxDeposit> optionalTaxDeposit = taxDepositRepository.findByEmployeeIdAndFromDateAndToDateAndTaxType(
                    employee.getId(), fromDate, toDate, taxType
            );
            if (optionalTaxDeposit.isPresent()) return optionalTaxDeposit.get();

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
            return employeeTaxDeposit;
        }catch (Exception e){
            throw new GenericException("Exception occurred while saving tax deposit info!");
        }
    }
    @Override
    public Page<EmployeeTaxDeposit> getAllTaxInfoByEmployeeId(Long employeeId, Pageable pageable){
        Page<EmployeeTaxDeposit> employeeTaxDepositPage = taxDepositRepository.findAllByEmployeeId(employeeId, pageable);
        return employeeTaxDepositPage;
    }
    @Override
    public Page<EmployeeTaxDeposit> getTaxInfoWithInDateRangeAndEmployeeId(TaxSearchCriteria criteria, Pageable pageable){
        Page<EmployeeTaxDeposit> taxInfoPage = taxDepositRepository.getAllByEmployeeIdAndFromDateAndToDate(
                criteria.getFromDate(), criteria.getToDate(), criteria.getEmployeeId(), pageable);

        return taxInfoPage;
    }
}
