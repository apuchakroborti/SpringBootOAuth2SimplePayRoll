package com.example.payroll.services.payroll.impls;


import com.example.payroll.dto.EmployeeModel;
import com.example.payroll.dto.request.EmployeeSearchCriteria;
import com.example.payroll.model.payroll.Employee;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.services.payroll.EmployeeService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeModel addEmployee(EmployeeModel employeeModel) throws Exception{
        Employee employee = new Employee();
        Util.copyProperty(employeeModel, employee);
        employee.setStatus("ACTIVE");
        employee = employeeRepository.save(employee);
        Util.copyProperty(employee, employeeModel);
        return employeeModel;
    }

    public Page<Employee> searchEmployee(EmployeeSearchCriteria criteria, Pageable pageable){

        Page<Employee> employeeModelPage = employeeRepository.searchEmployee(criteria.getEmployeeId()!=null?criteria.getEmployeeId():null,
                                                                                criteria.getFirstName()!=null?criteria.getFirstName():null,
                                                                                criteria.getUsername()!=null?criteria.getUsername():null,
                                                                                criteria.getJoinedDate()!=null?criteria.getJoinedDate():null,
                                                                                criteria.getEmail()!=null?criteria.getEmail():null,
                                                                                pageable);
        return employeeModelPage;
    }
//    @Override
//    public EmployeeLoginInfoDto getEmployeeLoginInfoByEmail(String email) {
//        return employeeRepository.getEmployeeLoginInfoByEmail(email);
//    }
}
