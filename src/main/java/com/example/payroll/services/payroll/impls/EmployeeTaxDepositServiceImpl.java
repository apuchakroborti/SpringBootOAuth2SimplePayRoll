package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeTaxDepositModel;
import com.example.payroll.model.payroll.EmployeeTaxDeposit;
import com.example.payroll.repository.payroll.EmployeeTaxDepositRepository;
import com.example.payroll.services.payroll.EmployeeTaxDepositService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeTaxDepositServiceImpl implements EmployeeTaxDepositService {

    @Autowired
    EmployeeTaxDepositRepository employeeTaxDepositRepository;

    @Override
    public EmployeeTaxDepositModel insertTaxInfo(EmployeeTaxDepositModel employeeTaxDepositModel){
        EmployeeTaxDeposit employeeTaxDeposit = new EmployeeTaxDeposit();
        Util.copyProperty(employeeTaxDepositModel, employeeTaxDeposit);
        employeeTaxDeposit = employeeTaxDepositRepository.save(employeeTaxDeposit);
        Util.copyProperty(employeeTaxDeposit, employeeTaxDepositModel);
        return employeeTaxDepositModel;
    }
    @Override
    public List<EmployeeTaxDepositModel> getAllTaxInfoByEmployeeId(Long employeeId){
        List<EmployeeTaxDeposit> employeeTaxDepositList = employeeTaxDepositRepository.findAllByEmployeeId(employeeId);
        return Util.toDtoList(employeeTaxDepositList, EmployeeTaxDepositModel.class);
    }
    @Override
    public List<EmployeeTaxDepositModel> getPFInfoWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId){
        List<EmployeeTaxDeposit> taxInfoList = employeeTaxDepositRepository.getAllByEmployeeIdAndFromDateAndToDate(employeeId, fromDate, toDate);

        return Util.toDtoList(taxInfoList, EmployeeTaxDepositModel.class);
    }
}
