package com.example.payroll.models.payroll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "EMPLOYEE_PROVIDENT_FUND")
@NoArgsConstructor
@AllArgsConstructor
public class ProvidentFund extends EntityCommon {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @Column(name = "EMPLOYEE_CONTRIBUTION")
    private Double employeeContribution;

    @Column(name = "COMPANY_CONTRIBUTION")
    private Double companyContribution;

    private String comments;

    @Column(name = "FROM_DATE", nullable = false)
    private LocalDate fromDate;

    @Column(name = "TO_DATE", nullable = false)
    private LocalDate toDate;

    @OneToOne
    private MonthlyPaySlip monthlyPaySlip;
}
