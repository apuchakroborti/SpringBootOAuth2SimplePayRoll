package com.example.payroll.controllers;

import com.example.payroll.dto.request.PasswordChangeRequestDto;
import com.example.payroll.dto.request.PasswordResetRequestDto;
import com.example.payroll.dto.request.UserSearchCriteria;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.security_oauth2.services.UserService;
import com.example.payroll.services.EmployeeService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final UserService userService;
    private final EmployeeService employeeService;

    @Autowired
    EmployeeController(UserService userService, EmployeeService employeeService){
        this.userService = userService;
        this.employeeService = employeeService;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ServiceResponse enrollEmployee(@RequestBody EmployeeDto customUserDto) throws GenericException {
        return new ServiceResponse(null, employeeService.enrollEmployee(customUserDto));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping()
    public ServiceResponse searchEmployee(UserSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException {
        return Utils.pageToServiceResponse(employeeService.getEmployeeList(criteria, pageable), EmployeeDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(path = "/{id}")
    public ServiceResponse getEmployeeById(@PathVariable(name = "id") Long id ) throws GenericException {
        return new ServiceResponse<>(null, employeeService.findEmployeeById(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ServiceResponse updateEmployeeById(@PathVariable(name = "id") Long id, @RequestBody EmployeeDto employeeBean) throws GenericException {
        return new ServiceResponse(null, employeeService.updateEmployeeById(id, employeeBean));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ServiceResponse deleteEmployeeById(@PathVariable(name = "id") Long id) throws GenericException {
        return new ServiceResponse(null, employeeService.deleteEmployeeById(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/update-password")
    public ServiceResponse updatePassword(@RequestBody PasswordChangeRequestDto passwordChangeRequestDto) throws GenericException {
        return new ServiceResponse(null, userService.changeUserPassword(passwordChangeRequestDto));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/reset-password")
    public ServiceResponse resetPassword(@RequestBody PasswordResetRequestDto passwordResetRequestDto) throws GenericException {
        return new ServiceResponse(null, userService.resetPassword(passwordResetRequestDto));
    }
}
