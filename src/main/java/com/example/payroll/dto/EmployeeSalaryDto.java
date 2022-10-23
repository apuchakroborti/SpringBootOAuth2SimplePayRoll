package com.example.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSalaryDto extends CommonDto {
    private Long id;

    @NotNull(message = "employee id can't be null!")
    private Long employeeId;

    @NotNull(message = "Gross salary can't be null!")
    private Double grossSalary;
    private Double basicSalary;

    private String comments;

    private LocalDate fromDate;
    private LocalDate toDate;

    private String status;
}
