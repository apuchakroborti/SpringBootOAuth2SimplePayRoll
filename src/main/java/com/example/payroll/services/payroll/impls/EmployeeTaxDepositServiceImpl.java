package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.dto.request.TaxSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.models.payroll.EmployeeTaxDeposit;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.repository.payroll.EmployeeTaxDepositRepository;
import com.example.payroll.services.payroll.EmployeeTaxDepositService;
import com.example.payroll.utils.Defs;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmployeeTaxDepositServiceImpl implements EmployeeTaxDepositService {

    @Autowired
    EmployeeTaxDepositRepository employeeTaxDepositRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public EmployeeTaxDepositDto insertTaxInfo(EmployeeTaxDepositDto employeeTaxDepositDto) throws GenericException{
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeTaxDepositDto.getEmployee().getId());
        if(!optionalEmployee.isPresent())throw new GenericException(Defs.USER_NOT_FOUND);

        EmployeeTaxDeposit employeeTaxDeposit = new EmployeeTaxDeposit();
        Util.copyProperty(employeeTaxDepositDto, employeeTaxDeposit);

        employeeTaxDeposit.setEmployee(optionalEmployee.get());
        employeeTaxDeposit.setCreatedBy(1L);
        employeeTaxDeposit.setCreateTime(LocalDateTime.now());

        employeeTaxDeposit = employeeTaxDepositRepository.save(employeeTaxDeposit);
        Util.copyProperty(employeeTaxDeposit, employeeTaxDepositDto);
        return employeeTaxDepositDto;
    }
    @Override
    public Page<EmployeeTaxDeposit> getAllTaxInfoByEmployeeId(Long employeeId, Pageable pageable){
        Page<EmployeeTaxDeposit> employeeTaxDepositPage = employeeTaxDepositRepository.findAllByEmployeeId(employeeId, pageable);
        return employeeTaxDepositPage;
    }
    @Override
    public Page<EmployeeTaxDeposit> getTaxInfoWithInDateRangeAndEmployeeId(TaxSearchCriteria criteria, Pageable pageable){
        Page<EmployeeTaxDeposit> taxInfoPage = employeeTaxDepositRepository.getAllByEmployeeIdAndFromDateAndToDate(criteria.getEmployeeId(), criteria.getFromDate(), criteria.getToDate(), pageable);

        return taxInfoPage;
    }
}
