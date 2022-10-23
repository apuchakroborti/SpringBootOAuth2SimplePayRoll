package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeProvidentFundModel;
import com.example.payroll.model.payroll.EmployeeProvidentFund;
import com.example.payroll.repository.payroll.EmployeeProvidentFundRepository;
import com.example.payroll.services.payroll.EmployeeProvidentFundService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeProvidentFundServiceImpl implements EmployeeProvidentFundService {
    @Autowired
    EmployeeProvidentFundRepository employeeProvidentFundRepository;

    @Override
    public EmployeeProvidentFundModel insertPfData(EmployeeProvidentFundModel employeeProvidentFundModel){

        EmployeeProvidentFund employeeProvidentFund = new EmployeeProvidentFund();
        Util.copyProperty(employeeProvidentFundModel, employeeProvidentFund);
        employeeProvidentFund = employeeProvidentFundRepository.save(employeeProvidentFund);
        Util.copyProperty(employeeProvidentFund, employeeProvidentFundModel);
        return employeeProvidentFundModel;
    }
    @Override
    public List<EmployeeProvidentFundModel> getPFInfoWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId){
        List<EmployeeProvidentFund> employeeMonthlyPaySlipList = employeeProvidentFundRepository.getEmployeeMonthlyPFByDateRangeAndEmployeeId(fromDate, toDate, employeeId);

        return Util.toDtoList(employeeMonthlyPaySlipList, EmployeeProvidentFundModel.class);
    }
}
