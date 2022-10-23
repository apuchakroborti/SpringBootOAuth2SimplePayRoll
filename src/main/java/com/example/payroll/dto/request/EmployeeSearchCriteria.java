package com.example.payroll.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeSearchCriteria {

    private Long employeeId;
    private String email;
    private String username;
    private String userId;
    private String firstName;
    private String lastName;
    private String designation;
    private LocalDate joinedDate;
}