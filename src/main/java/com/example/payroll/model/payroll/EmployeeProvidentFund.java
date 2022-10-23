package com.example.payroll.model.payroll;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "EMPLOYEE_PROVIDENT_FUND")
public class EmployeeProvidentFund extends Common{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMPLOYEE_ID", nullable = false)
    private Long employeeId;

    @Column(name = "EMPLOYEE_CONTRIBUTION")
    private Double employeeContribution;
    @Column(name = "COMPANY_CONTRIBUTION")
    private Double companyContribution;

    private String comments;
    @Column(name = "FROM_DATE", nullable = false)
    private LocalDate fromDate;
    @Column(name = "TO_DATE", nullable = false)
    private LocalDate toDate;
}
