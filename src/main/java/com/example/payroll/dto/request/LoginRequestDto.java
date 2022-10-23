package com.example.payroll.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotEmpty(message = "username mandatory")
    private String username;

    @NotEmpty(message = "password mandatory")
    private String password;
}
