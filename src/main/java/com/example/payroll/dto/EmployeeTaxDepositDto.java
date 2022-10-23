package com.example.payroll.dto;

import lombok.Data;

@Data
public class EmployeeTaxDepositDto extends CommonDto {
    private Long id;

    private Long employeeId;

    private Double amount;

    private String chalanNo;

    private String comments;
}
