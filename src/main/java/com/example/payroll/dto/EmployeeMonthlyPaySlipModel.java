package com.example.payroll.dto;

import com.example.payroll.model.payroll.Common;
import lombok.Data;

import javax.persistence.Id;

@Data
public class EmployeeMonthlyPaySlipModel extends Common {
    @Id
    private Long id;


    private Long employeeId;
    private Double grossSalary;


    private Double basicSalary;


    private Double houseRent;

    private Double conveyanceAllowance;

    private Double medicalAllowance;

    private Double due;

    private Double pfDeductionAmount;

    private Double taxDeductionAmount;

    private Double arrears;

    private Double festivalBonus;

    private Double incentiveBonus;

    private Double otherPay;

    private Double totalPayment;
    private String status;

    private String comments;
}
