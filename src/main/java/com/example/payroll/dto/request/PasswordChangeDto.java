package com.example.payroll.dto.request;

import lombok.Data;

@Data
public class PasswordChangeDto {

    private String currentPassword;

    private String newPassword;
}