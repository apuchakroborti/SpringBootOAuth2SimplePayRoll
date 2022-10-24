package com.example.payroll.services.payroll;


import com.example.payroll.dto.MonthlyPaySlipDto;
import com.example.payroll.dto.request.MonthlyPaySlipRequestDto;
import com.example.payroll.dto.request.PayslipSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.MonthlyPaySlip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface EmployeeMonthlyPaySlipService {
   MonthlyPaySlipDto createPaySlip(MonthlyPaySlipRequestDto monthlyPaySlipRequestDto) throws GenericException;
   Page<MonthlyPaySlip> getPaySlipWithInDateRangeAndEmployeeId(PayslipSearchCriteria criteria, Pageable pageable) throws GenericException;

}
