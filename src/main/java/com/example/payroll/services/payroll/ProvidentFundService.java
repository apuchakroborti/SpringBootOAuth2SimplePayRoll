package com.example.payroll.services.payroll;

import com.example.payroll.dto.request.ProvidentFundSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.entity.payroll.EmployeeSalary;
import com.example.payroll.entity.payroll.ProvidentFund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;


public interface ProvidentFundService {
    ProvidentFund insertPfData(EmployeeSalary employeeSalary, LocalDate month) throws GenericException;
    Page<ProvidentFund> getPFInfoWithSearchCriteria(ProvidentFundSearchCriteria criteria, Pageable pageable) throws GenericException;
    Optional<ProvidentFund> getByEmployeeIdAndFromDateAndToDate(Long employeeId, LocalDate fromDate, LocalDate toDate);
}
