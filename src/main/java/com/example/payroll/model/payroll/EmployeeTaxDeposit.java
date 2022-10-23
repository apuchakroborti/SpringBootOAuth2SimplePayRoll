package com.example.payroll.model.payroll;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "EMPLOYEE_TAX_DEPOSIT")
public class EmployeeTaxDeposit extends Common{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMPLOYEE_ID", nullable = false)
    private Long employeeId;

    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "CHALAN_NO")
    private String chalanNo;

    private String comments;

    @Column(name = "FROM_DATE", nullable = false)
    private LocalDate fromDate;
    @Column(name = "TO_DATE", nullable = false)
    private LocalDate toDate;

}
