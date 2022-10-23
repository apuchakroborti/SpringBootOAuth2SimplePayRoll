package com.example.payroll.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EmployeeSalaryModel extends CommonModel {
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
