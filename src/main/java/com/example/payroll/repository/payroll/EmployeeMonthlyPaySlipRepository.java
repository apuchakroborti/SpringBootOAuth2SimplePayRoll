package com.example.payroll.repository.payroll;

import com.example.payroll.models.payroll.EmployeeMonthlyPaySlip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeMonthlyPaySlipRepository extends CrudRepository<EmployeeMonthlyPaySlip, Long> {

    @Query(value="  select payslip " +
            "   from EmployeeMonthlyPaySlip payslip" +
            "   where payslip.employeeId = :employeeId " +
            "   and payslip.fromDate >= :fromDate " +
            "   and payslip.toDate <= :toDate")
    Page<EmployeeMonthlyPaySlip> getEmployeeMonthlyPaySlipByDateRangeAndEmployeeId(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("employeeId") Long employeeId,
            Pageable pageable);
}
