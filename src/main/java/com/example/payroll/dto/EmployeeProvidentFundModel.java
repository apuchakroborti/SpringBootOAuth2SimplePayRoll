package com.example.payroll.dto;

import lombok.Data;

@Data
public class EmployeeProvidentFundModel extends CommonModel {
    private Long id;

    private Long employeeId;

    private Double employeeContribution;
    private Double companyContribution;

    private String comments;
}
