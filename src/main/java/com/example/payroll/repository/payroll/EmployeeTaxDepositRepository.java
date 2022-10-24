package com.example.payroll.repository.payroll;

import com.example.payroll.models.payroll.EmployeeTaxDeposit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeTaxDepositRepository extends CrudRepository<EmployeeTaxDeposit, Long> {
    Page<EmployeeTaxDeposit> findAllByEmployeeId(Long employeeId, Pageable pageable);

    @Query(value="  select tax " +
            "   from EmployeeTaxDeposit tax" +
            "   where (:employeeId is null or tax.employee.id = :employeeId ) " +
            "   and ( :fromDate is null or tax.fromDate >= :fromDate ) " +
            "   and ( :toDate is null or tax.toDate <= :toDate ) ")
    Page<EmployeeTaxDeposit> getAllByEmployeeIdAndFromDateAndToDate(
            @Param("employeeId") Long employeeId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
            );
}
