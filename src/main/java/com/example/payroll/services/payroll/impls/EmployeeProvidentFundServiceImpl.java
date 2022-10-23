package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeProvidentFundDto;
import com.example.payroll.models.payroll.EmployeeProvidentFund;
import com.example.payroll.repository.payroll.EmployeeProvidentFundRepository;
import com.example.payroll.services.payroll.EmployeeProvidentFundService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeProvidentFundServiceImpl implements EmployeeProvidentFundService {
    @Autowired
    EmployeeProvidentFundRepository employeeProvidentFundRepository;

    @Override
    public EmployeeProvidentFundDto insertPfData(EmployeeProvidentFundDto employeeProvidentFundDto){

        EmployeeProvidentFund employeeProvidentFund = new EmployeeProvidentFund();
        Util.copyProperty(employeeProvidentFundDto, employeeProvidentFund);

        employeeProvidentFund.setCreatedBy(1L);
        employeeProvidentFund.setCreateTime(LocalDateTime.now());

        employeeProvidentFund = employeeProvidentFundRepository.save(employeeProvidentFund);
        Util.copyProperty(employeeProvidentFund, employeeProvidentFundDto);
        return employeeProvidentFundDto;
    }
    @Override
    public Page<EmployeeProvidentFund> getPFInfoWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId, Pageable pageable){
        Page<EmployeeProvidentFund> employeeMonthlyPaySlipPage = employeeProvidentFundRepository.getEmployeeMonthlyPFByDateRangeAndEmployeeId(fromDate, toDate, employeeId, pageable);

        return employeeMonthlyPaySlipPage;
    }
}
