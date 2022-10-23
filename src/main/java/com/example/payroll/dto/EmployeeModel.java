package com.example.payroll.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeModel extends CommonModel {
    private Long id;
    private String firstName;

    private String lastName;


    private String email;


    private String tin;


    private String nid;

    private String passport;


    private String username;


    private String password;


    private LocalDate dateOfJoining;

    private String designation;

    private String phone;

    private Integer rollId;
}
