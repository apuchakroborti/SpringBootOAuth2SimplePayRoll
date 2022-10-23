package com.example.payroll.services.payroll.impls;


import com.example.payroll.dto.EmployeeMonthlyPaySlipDto;
import com.example.payroll.models.payroll.EmployeeMonthlyPaySlip;
import com.example.payroll.repository.payroll.EmployeeMonthlyPaySlipRepository;
import com.example.payroll.services.payroll.EmployeeMonthlyPaySlipService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeMonthlyPaySlipServiceImpl implements EmployeeMonthlyPaySlipService {
    @Autowired
    EmployeeMonthlyPaySlipRepository employeeMonthlyPaySlipRepository;

    public EmployeeMonthlyPaySlipDto createPaySlip(EmployeeMonthlyPaySlipDto employeeMonthlyPaySlipDto){
        EmployeeMonthlyPaySlip employeeMonthlyPaySlip = new EmployeeMonthlyPaySlip();
        Util.copyProperty(employeeMonthlyPaySlipDto, employeeMonthlyPaySlip);
        employeeMonthlyPaySlip = employeeMonthlyPaySlipRepository.save(employeeMonthlyPaySlip);
        Util.copyProperty(employeeMonthlyPaySlip, employeeMonthlyPaySlipDto);
        return employeeMonthlyPaySlipDto;

    }
    public Page<EmployeeMonthlyPaySlip> getPaySlipWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId, Pageable pageable){
        Page<EmployeeMonthlyPaySlip> employeeMonthlyPaySlipPage = employeeMonthlyPaySlipRepository.getEmployeeMonthlyPaySlipByDateRangeAndEmployeeId(
                fromDate, toDate, employeeId, pageable);
        return employeeMonthlyPaySlipPage;
    }
}
