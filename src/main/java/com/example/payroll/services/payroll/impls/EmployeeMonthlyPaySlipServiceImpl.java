package com.example.payroll.services.payroll.impls;


import com.example.payroll.dto.EmployeeMonthlyPaySlipModel;
import com.example.payroll.model.payroll.EmployeeMonthlyPaySlip;
import com.example.payroll.repository.payroll.EmployeeMonthlyPaySlipRepository;
import com.example.payroll.services.payroll.EmployeeMonthlyPaySlipService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeMonthlyPaySlipServiceImpl implements EmployeeMonthlyPaySlipService {
    @Autowired
    EmployeeMonthlyPaySlipRepository employeeMonthlyPaySlipRepository;

    public EmployeeMonthlyPaySlipModel createPaySlip(EmployeeMonthlyPaySlipModel employeeMonthlyPaySlipModel){
        EmployeeMonthlyPaySlip employeeMonthlyPaySlip = new EmployeeMonthlyPaySlip();
        Util.copyProperty(employeeMonthlyPaySlipModel, employeeMonthlyPaySlip);
        employeeMonthlyPaySlip = employeeMonthlyPaySlipRepository.save(employeeMonthlyPaySlip);
        Util.copyProperty(employeeMonthlyPaySlip, employeeMonthlyPaySlipModel);
        return employeeMonthlyPaySlipModel;

    }
    public List<EmployeeMonthlyPaySlipModel> getPaySlipWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId){
        List<EmployeeMonthlyPaySlip> employeeMonthlyPaySlipList = employeeMonthlyPaySlipRepository.getEmployeeMonthlyPaySlipByDateRangeAndEmployeeId(fromDate, toDate, employeeId);
        return Util.toDtoList(employeeMonthlyPaySlipList, EmployeeMonthlyPaySlipModel.class);
    }
}
