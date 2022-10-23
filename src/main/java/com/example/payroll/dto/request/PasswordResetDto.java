package com.example.payroll.dto.request;

import lombok.Data;

@Data
public class PasswordResetDto {
    private String userName;

    private String currentPassword;

    private String newPassword;
}
