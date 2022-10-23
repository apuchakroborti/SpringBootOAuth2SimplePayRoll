package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.EmployeeSalaryModel;
import com.example.payroll.exceptions.PayrollException;
import com.example.payroll.model.payroll.Employee;
import com.example.payroll.model.payroll.EmployeeSalary;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.repository.payroll.EmployeeSalaryRepository;
import com.example.payroll.services.payroll.EmployeeSalaryService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeSalaryServiceImpl implements EmployeeSalaryService {
    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public EmployeeSalaryModel insertSalaryData(EmployeeSalaryModel employeeSalaryModel) throws PayrollException {
        EmployeeSalary employeeSalary = employeeSalaryRepository.getEmployeeCurrentSalaryByEmployeeId(employeeSalaryModel.getEmployeeId());
        if(employeeSalary!=null){
            if(employeeSalaryModel.getFromDate()==null){
                throw new PayrollException("Please provide the starting date of the salary!");
            }
            employeeSalary.setToDate(employeeSalaryModel.getFromDate().minusDays(1));
            employeeSalaryRepository.save(employeeSalary);
        }else{
            Optional<Employee> optional = employeeRepository.findById(employeeSalaryModel.getEmployeeId());
            if(!optional.isPresent()){
                throw new PayrollException("employee not found!");
            }
            employeeSalaryModel.setFromDate(optional.get().getDateOfJoining());
        }
        EmployeeSalary employeeSalaryNew = new EmployeeSalary();
        Util.copyProperty(employeeSalaryModel, employeeSalaryNew);
        Double basic = employeeSalaryModel.getGrossSalary()*60/100.0;
        employeeSalaryNew.setBasicSalary(basic);
        employeeSalaryNew.setStatus("ACTIVE");
        employeeSalaryNew = employeeSalaryRepository.save(employeeSalaryNew);
        Util.copyProperty(employeeSalaryNew, employeeSalaryModel);
        return employeeSalaryModel;
    }
    @Override
    public List<EmployeeSalaryModel> getSalaryDataWithInDateRangeAndEmployeeId(LocalDate fromDate, LocalDate toDate, Long employeeId) throws PayrollException{
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.getEmployeeSalaryByDateRangeAndEmployeeId(fromDate, toDate, employeeId);
        return Util.toDtoList(employeeSalaryList, EmployeeSalaryModel.class);
    }
    @Override
    public EmployeeSalaryModel getCurrentSalaryByEmployeeId(Long employeeId) throws PayrollException{
        EmployeeSalary employeeCurrentSalary = employeeSalaryRepository.getEmployeeCurrentSalaryByEmployeeId(employeeId);
        EmployeeSalaryModel employeeSalaryModel = new EmployeeSalaryModel();
        Util.copyProperty(employeeCurrentSalary, employeeSalaryModel);
        return employeeSalaryModel;
    }
}
