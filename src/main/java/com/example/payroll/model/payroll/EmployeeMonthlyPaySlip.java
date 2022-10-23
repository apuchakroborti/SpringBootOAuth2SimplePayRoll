package com.example.payroll.model.payroll;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "EMPLOYEE_MONTHLY_PAY_SLIP")
public class EmployeeMonthlyPaySlip extends Common {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMPLOYEE_ID", nullable = false)
    private Long employeeId;

    @Column(name = "GROSS_SALARY", nullable = false)
    private Double grossSalary;

    @Column(name = "BASIC_SALARY", nullable = false)
    private Double basicSalary;

    @Column(name = "HOUSE_RENT")
    private Double houseRent;

    @Column(name = "CONVEYANCE")
    private Integer conveyenceAllowance;

    @Column(name = "MEDICAL")
    private Double medicalAllowance;

    private Double due;

    @Column(name = "PF_DEDUCTION")
    private Double pfDeductionAmount;

    @Column(name = "TAX_DEDUCTION")
    private Double taxDeductionAmount;

    private Integer arrear;

    @Column(name = "FESTIVAL_BONUS")
    private Double festivalBonus;

    @Column(name = "INCENTIVE_BONUS")
    private Double incentiveBonus;

    @Column(name = "OTHER_PAY")
    private Double otherPay;

    @Column(name = "NET_PAYMENT")
    private Double netPayment;
    private String status;

    @Column(name = "FROM_DATE", nullable = false)
    private LocalDate fromDate;
    @Column(name = "TO_DATE", nullable = false)
    private LocalDate toDate;

    private String comments;
}
