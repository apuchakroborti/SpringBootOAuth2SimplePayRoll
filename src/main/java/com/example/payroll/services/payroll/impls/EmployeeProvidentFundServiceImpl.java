package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.request.ProvidentFundSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.EmployeeSalary;
import com.example.payroll.models.payroll.ProvidentFund;
import com.example.payroll.repository.payroll.ProvidentFundRepository;
import com.example.payroll.services.payroll.ProvidentFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
public class EmployeeProvidentFundServiceImpl implements ProvidentFundService {
    @Autowired
    ProvidentFundRepository employeeProvidentFundRepository;

    @Override
    public ProvidentFund insertPfData(EmployeeSalary employeeSalary, LocalDate month) throws GenericException {
        ProvidentFund providentFund = new ProvidentFund();

        LocalDate start = month.with(firstDayOfMonth());
        LocalDate end = month.with(lastDayOfMonth());

        providentFund.setFromDate(start);
        providentFund.setToDate(end);

        providentFund.setEmployeeContribution(employeeSalary.getBasicSalary()*7.5/100);
        providentFund.setCompanyContribution(employeeSalary.getBasicSalary()*7.5/100);

        providentFund.setComments("Monthly deposit to provident fund");

        providentFund.setEmployee(employeeSalary.getEmployee());

        providentFund.setCreatedBy(1L);
        providentFund.setCreateTime(LocalDateTime.now());

        providentFund = employeeProvidentFundRepository.save(providentFund);

        return providentFund;
    }
    @Override
    public Page<ProvidentFund> getPFInfoWithSearchCriteria(ProvidentFundSearchCriteria criteria, Pageable pageable) throws GenericException{
        Page<ProvidentFund> employeePFPage = employeeProvidentFundRepository.getEmployeeMonthlyPFByDateRangeAndEmployeeId(
                criteria.getFromDate(), criteria.getToDate(), criteria.getEmployeeId(), pageable);

        return employeePFPage;
    }
}
