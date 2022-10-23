package com.example.payroll.dto;

import lombok.Data;

@Data
public class RoleModel extends CommonModel {
    private Long id;

    private String accessLevel;

    private String activeStatus;
}
