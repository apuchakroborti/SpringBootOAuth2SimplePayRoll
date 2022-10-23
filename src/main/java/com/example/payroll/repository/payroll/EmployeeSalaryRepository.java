package com.example.payroll.repository.payroll;

import com.example.payroll.model.payroll.EmployeeSalary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeSalaryRepository extends CrudRepository<EmployeeSalary, Long> {

    @Query(value="  select salary " +
            "   from EmployeeSalary salary " +
            "   where salary.employeeId = :employeeId " +
            "   and salary.fromDate >= :fromDate " +
            "   and salary.toDate <= :toDate")
    List<EmployeeSalary> getEmployeeSalaryByDateRangeAndEmployeeId(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("employeeId") Long employeeId);

    @Query(value="  select salary " +
            "   from EmployeeSalary salary " +
            "   where salary.employeeId = :employeeId " +
            "   and salary.toDate is null")
    EmployeeSalary getEmployeeCurrentSalaryByEmployeeId(
            @Param("employeeId") Long employeeId);

}
