package com.example.payroll.services.payroll;

import com.example.payroll.dto.request.ProvidentFundSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.EmployeeSalary;
import com.example.payroll.models.payroll.ProvidentFund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface ProvidentFundService {
    ProvidentFund insertPfData(EmployeeSalary employeeSalary, LocalDate month) throws GenericException;
    Page<ProvidentFund> getPFInfoWithSearchCriteria(ProvidentFundSearchCriteria criteria, Pageable pageable) throws GenericException;
}
