package com.example.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProvidentFundDto extends CommonDto {
    private Long id;

    private Long employeeId;

    private Double employeeContribution;
    private Double companyContribution;

    private String comments;
}
