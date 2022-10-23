package com.example.payroll.dto;

import lombok.Data;

@Data
public class EmployeeTaxDepositModel extends CommonModel {
    private Long id;

    private Long employeeId;

    private Double amount;

    private String chalanNo;

    private String comments;
}
