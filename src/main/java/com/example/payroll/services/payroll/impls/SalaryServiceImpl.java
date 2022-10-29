package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeSalaryDto;
import com.example.payroll.dto.request.SalarySearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.models.payroll.EmployeeSalary;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.repository.payroll.EmployeeSalaryRepository;
import com.example.payroll.services.payroll.SalaryService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class SalaryServiceImpl implements SalaryService {
    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public EmployeeSalaryDto updateSalaryData(EmployeeSalaryDto employeeSalaryDto) throws GenericException {
        //TODO check fromDate must be after the month of joining date
        EmployeeSalary employeeSalary = employeeSalaryRepository.getEmployeeCurrentSalaryByEmployeeId(employeeSalaryDto.getEmployee().getId());
        Employee employee = null;
        if(employeeSalary!=null){
            employeeSalary.setToDate(employeeSalaryDto.getFromDate().minusDays(1));
            employeeSalary.setStatus(false);
            employee = employeeSalary.getEmployee();
            employeeSalaryRepository.save(employeeSalary);
        }else{
            Optional<Employee> optionalEmployee = employeeRepository.findById(employeeSalaryDto.getEmployee().getId());
            if(!optionalEmployee.isPresent()){
                throw new GenericException("employee not found!");
            }
            employeeSalaryDto.setFromDate(optionalEmployee.get().getDateOfJoining());
            if(employee==null)employee=optionalEmployee.get();
        }
        EmployeeSalary employeeSalaryNew = new EmployeeSalary();
        Utils.copyProperty(employeeSalaryDto, employeeSalaryNew);

        Double basic = employeeSalaryDto.getGrossSalary()*60/100.0;
        employeeSalaryNew.setBasicSalary(basic);
        employeeSalaryNew.setStatus(true);

        employeeSalaryNew.setCreatedBy(1L);
        employeeSalaryNew.setCreateTime(LocalDateTime.now());
        employeeSalaryNew.setEmployee(employee);
        employeeSalaryNew = employeeSalaryRepository.save(employeeSalaryNew);
        Utils.copyProperty(employeeSalaryNew, employeeSalaryDto);

        //TODO need to update payslip's info from this month to last month for this financial year
        return employeeSalaryDto;
    }
    @Override
    public Page<EmployeeSalary> getSalaryDataWithInDateRangeAndEmployeeId(SalarySearchCriteria searchCriteria, Pageable pageable)throws GenericException{
        Page<EmployeeSalary> employeeSalaryPage = employeeSalaryRepository.getEmployeeSalaryByDateRangeAndEmployeeId(
                searchCriteria.getFromDate(),
                searchCriteria.getToDate(),
                searchCriteria.getEmployeeId(),
                pageable);
        return employeeSalaryPage;
    }
    @Override
    public EmployeeSalaryDto getCurrentSalaryByEmployeeId(Long employeeId) throws GenericException {
        if(employeeId==null)throw new GenericException("EmployeeId should not be null!");
        EmployeeSalary employeeCurrentSalary = employeeSalaryRepository.getEmployeeCurrentSalaryByEmployeeId(employeeId);
        EmployeeSalaryDto employeeSalaryDto = new EmployeeSalaryDto();
        Utils.copyProperty(employeeCurrentSalary, employeeSalaryDto);
        return employeeSalaryDto;
    }
}
