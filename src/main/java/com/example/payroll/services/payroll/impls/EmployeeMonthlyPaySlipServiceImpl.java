package com.example.payroll.services.payroll.impls;


import com.example.payroll.dto.MonthlyPaySlipDto;
import com.example.payroll.dto.request.MonthlyPaySlipRequestDto;
import com.example.payroll.dto.request.PayslipSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.models.payroll.EmployeeSalary;
import com.example.payroll.models.payroll.MonthlyPaySlip;
import com.example.payroll.models.payroll.ProvidentFund;
import com.example.payroll.repository.payroll.MonthlyPaySlipRepository;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.repository.payroll.EmployeeSalaryRepository;
import com.example.payroll.services.payroll.EmployeeMonthlyPaySlipService;
import com.example.payroll.services.payroll.ProvidentFundService;
import com.example.payroll.utils.Defs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeMonthlyPaySlipServiceImpl implements EmployeeMonthlyPaySlipService {
    @Autowired
    MonthlyPaySlipRepository monthlyPaySlipRepository;

    @Autowired
    ProvidentFundService providentFundService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;

    //TODO need to implement
    @Override
    public MonthlyPaySlipDto createPaySlip(MonthlyPaySlipRequestDto monthlyPaySlipRequestDto) throws GenericException {
        Optional<Employee> optionalEmployee = employeeRepository.findById(monthlyPaySlipRequestDto.getEmployee().getId());
        if(!optionalEmployee.isPresent())throw new GenericException(Defs.USER_NOT_FOUND);
        EmployeeSalary employeeSalary = employeeSalaryRepository.getEmployeeCurrentSalaryByEmployeeId(optionalEmployee.get().getId());

        ProvidentFund providentFund = providentFundService.insertPfData(employeeSalary, monthlyPaySlipRequestDto.getFromDate());

        MonthlyPaySlip monthlyPaySlip = new MonthlyPaySlip();

        monthlyPaySlip.setProvidentFund(providentFund);


        monthlyPaySlip = monthlyPaySlipRepository.save(monthlyPaySlip);

        MonthlyPaySlipDto monthlyPaySlipDto = new MonthlyPaySlipDto();
        return monthlyPaySlipDto;

    }
    @Override
    public Page<MonthlyPaySlip> getPaySlipWithInDateRangeAndEmployeeId(PayslipSearchCriteria criteria, Pageable pageable) throws GenericException{
        Page<MonthlyPaySlip> employeeMonthlyPaySlipPage = monthlyPaySlipRepository.getEmployeeMonthlyPaySlipByDateRangeAndEmployeeId(
                criteria.getFromDate(), criteria.getToDate(), criteria.getEmployeeId(), pageable);
        return employeeMonthlyPaySlipPage;
    }
    //TODO need to implement
    private Integer getMonthlyTaxDepositAmount(Employee employee, LocalDate date){
        Integer monthlyTxFundAmount = 0;

        int year = date.getYear();
        int month = date.getMonthValue();

        LocalDate fromDate = null;
        LocalDate toDate = null;
        if(month <=6){
            fromDate = LocalDate.of(year-1, 7, 1 );
            toDate = LocalDate.of(year, 6, 30 );
        }else{
            fromDate = LocalDate.of(year, 7, 1 );
            toDate = LocalDate.of(year+1, 6, 30 );
        }
        return monthlyTxFundAmount;
    }
    //TODO need to implement
    private Integer getTotalEstimatedTaxableIncomeBasedOnSalary(Employee employee, LocalDate fromDate, LocalDate toDate){
        Integer totalTaxableIncome = 0;

        List<EmployeeSalary> employeeSalaryList = employee.getEmployeeSalaryList();
        employeeSalaryList = employeeSalaryList
                .stream()
                .filter(employeeSalary ->
                        employeeSalary.getFromDate().isAfter(fromDate.minusDays(1)) &&
                        employeeSalary.getToDate().isBefore(toDate.plusDays(1)))
                .collect(Collectors.toList());

        HashMap<Integer, Integer> monthlySalaryMap = new HashMap<>();

        for(EmployeeSalary employeeSalary: employeeSalaryList){

        }

        return totalTaxableIncome;
    }
}
