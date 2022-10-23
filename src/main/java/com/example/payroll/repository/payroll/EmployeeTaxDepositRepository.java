package com.example.payroll.repository.payroll;

import com.example.payroll.model.payroll.EmployeeTaxDeposit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeTaxDepositRepository extends CrudRepository<EmployeeTaxDeposit, Long> {
    List<EmployeeTaxDeposit> findAllByEmployeeId(Long employeeId);

    @Query(value="  select tax " +
            "   from EmployeeTaxDeposit tax" +
            "   where tax.employeeId = :employeeId " +
            "   and tax.fromDate >= :fromDate " +
            "   and tax.toDate <= :toDate ")
    List<EmployeeTaxDeposit> getAllByEmployeeIdAndFromDateAndToDate(
            @Param("employeeId") Long employeeId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
            );
}
