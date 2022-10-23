package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeSalaryDto;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.models.payroll.EmployeeSalary;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.repository.payroll.EmployeeSalaryRepository;
import com.example.payroll.services.payroll.EmployeeSalaryService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class EmployeeSalaryServiceImpl implements EmployeeSalaryService {
    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public EmployeeSalaryDto insertSalaryData(EmployeeSalaryDto employeeSalaryDto) throws GenericException {
        EmployeeSalary employeeSalary = employeeSalaryRepository.getEmployeeCurrentSalaryByEmployeeId(employeeSalaryDto.getEmployeeId());
        if(employeeSalary!=null){
            if(employeeSalaryDto.getFromDate()==null){
                throw new GenericException("Please provide the starting date of the salary!");
            }
            employeeSalary.setToDate(employeeSalaryDto.getFromDate().minusDays(1));
            employeeSalaryRepository.save(employeeSalary);
        }else{
            Optional<Employee> optional = employeeRepository.findById(employeeSalaryDto.getEmployeeId());
            if(!optional.isPresent()){
                throw new GenericException("employee not found!");
            }
            employeeSalaryDto.setFromDate(optional.get().getDateOfJoining());
        }
        EmployeeSalary employeeSalaryNew = new EmployeeSalary();
        Util.copyProperty(employeeSalaryDto, employeeSalaryNew);

        Double basic = employeeSalaryDto.getGrossSalary()*60/100.0;
        employeeSalaryNew.setBasicSalary(basic);
        employeeSalaryNew.setStatus(true);

        employeeSalaryNew.setCreatedBy(1L);
        employeeSalaryNew.setCreateTime(LocalDateTime.now());

        employeeSalaryNew = employeeSalaryRepository.save(employeeSalaryNew);
        Util.copyProperty(employeeSalaryNew, employeeSalaryDto);
        return employeeSalaryDto;
    }
    @Override
    public Page<EmployeeSalary> getSalaryDataWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId, Pageable pageable)throws GenericException{
        Page<EmployeeSalary> employeeSalaryPage = employeeSalaryRepository.getEmployeeSalaryByDateRangeAndEmployeeId(fromDate, toDate, employeeId, pageable);
        return employeeSalaryPage;
    }
    @Override
    public EmployeeSalaryDto getCurrentSalaryByEmployeeId(Long employeeId) throws GenericException {
        EmployeeSalary employeeCurrentSalary = employeeSalaryRepository.getEmployeeCurrentSalaryByEmployeeId(employeeId);
        EmployeeSalaryDto employeeSalaryDto = new EmployeeSalaryDto();
        Util.copyProperty(employeeCurrentSalary, employeeSalaryDto);
        return employeeSalaryDto;
    }
}
