package com.example.payroll.repository.payroll;

import com.example.payroll.model.payroll.EmployeeProvidentFund;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeProvidentFundRepository extends CrudRepository<EmployeeProvidentFund, Long> {
    @Query(value="  select pf " +
            "   from EmployeeProvidentFund pf" +
            "   where pf.employeeId = :employeeId " +
            "   and pf.fromDate >= :fromDate " +
            "   and pf.toDate <= :toDate")
    List<EmployeeProvidentFund> getEmployeeMonthlyPFByDateRangeAndEmployeeId(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("employeeId") Long employeeId);
}
