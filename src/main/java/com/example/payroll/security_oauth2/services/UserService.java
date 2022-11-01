package com.example.payroll.security_oauth2.services;


import com.example.payroll.dto.request.PasswordChangeRequestDto;
import com.example.payroll.dto.request.PasswordResetRequestDto;
import com.example.payroll.dto.response.PasswordChangeResponseDto;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;

public interface UserService {
    ServiceResponse<PasswordChangeResponseDto> changeUserPassword(PasswordChangeRequestDto passwordChangeRequestDto) throws GenericException;
    ServiceResponse<PasswordChangeResponseDto> resetPassword(PasswordResetRequestDto passwordResetRequestDto) throws GenericException;
}