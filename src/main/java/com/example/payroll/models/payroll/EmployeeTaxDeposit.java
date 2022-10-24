package com.example.payroll.models.payroll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "EMPLOYEE_TAX_DEPOSIT")
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTaxDeposit extends EntityCommon {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MONTHLY_PAYSLIP_ID", nullable = false)
    private MonthlyPaySlip monthlyPaySlip;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "CHALAN_NO", nullable = false)
    private String chalanNo;

    private String comments;

    @Column(name = "FROM_DATE", nullable = false)
    private LocalDate fromDate;

    @Column(name = "TO_DATE", nullable = false)
    private LocalDate toDate;

}
