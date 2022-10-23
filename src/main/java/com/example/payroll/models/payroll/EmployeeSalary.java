package com.example.payroll.models.payroll;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "EMPLOYEE_SALARY")
public class EmployeeSalary extends EntityCommon {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMPLOYEE_ID", nullable = false)
    private Long employeeId;

    @Column(name = "BASIC_SALARY")
    private Double basicSalary;

    @Column(name = "GROSS_SALARY")
    private Double grossSalary;

    private String comments;

    @Column(name = "FROM_DATE", nullable = false)
    private LocalDate fromDate;

    @Column(name = "TO_DATE")
    private LocalDate toDate;

    private Boolean status;
}
