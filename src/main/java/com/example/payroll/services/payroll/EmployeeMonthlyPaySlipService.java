package com.example.payroll.services.payroll;


import com.example.payroll.dto.EmployeeMonthlyPaySlipDto;
import com.example.payroll.models.payroll.EmployeeMonthlyPaySlip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface EmployeeMonthlyPaySlipService {
   EmployeeMonthlyPaySlipDto createPaySlip(EmployeeMonthlyPaySlipDto employeeMonthlyPaySlipDto);
   Page<EmployeeMonthlyPaySlip> getPaySlipWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId, Pageable pageable);

}
