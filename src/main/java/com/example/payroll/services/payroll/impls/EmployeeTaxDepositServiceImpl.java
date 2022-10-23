package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeTaxDepositDto;
import com.example.payroll.models.payroll.EmployeeTaxDeposit;
import com.example.payroll.repository.payroll.EmployeeTaxDepositRepository;
import com.example.payroll.services.payroll.EmployeeTaxDepositService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeTaxDepositServiceImpl implements EmployeeTaxDepositService {

    @Autowired
    EmployeeTaxDepositRepository employeeTaxDepositRepository;

    @Override
    public EmployeeTaxDepositDto insertTaxInfo(EmployeeTaxDepositDto employeeTaxDepositDto){
        EmployeeTaxDeposit employeeTaxDeposit = new EmployeeTaxDeposit();
        Util.copyProperty(employeeTaxDepositDto, employeeTaxDeposit);

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
    public Page<EmployeeTaxDeposit> getTaxInfoWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId, Pageable pageable){
        Page<EmployeeTaxDeposit> taxInfoPage = employeeTaxDepositRepository.getAllByEmployeeIdAndFromDateAndToDate(employeeId, fromDate, toDate, pageable);

        return taxInfoPage;
    }
}
