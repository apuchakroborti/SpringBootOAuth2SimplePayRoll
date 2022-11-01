package com.example.payroll.controllers;

import com.example.payroll.dto.request.PasswordChangeRequestDto;
import com.example.payroll.dto.request.PasswordResetRequestDto;
import com.example.payroll.dto.request.EmployeeSearchCriteria;
import com.example.payroll.dto.response.Pagination;
import com.example.payroll.dto.response.PasswordChangeResponseDto;
import com.example.payroll.dto.response.ServiceResponse;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.security_oauth2.services.UserService;
import com.example.payroll.services.EmployeeService;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final UserService userService;
    private final EmployeeService employeeService;

    //TODO need to add description about all of these endpoints
    @Autowired
    EmployeeController(UserService userService, EmployeeService employeeService){
        this.userService = userService;
        this.employeeService = employeeService;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ServiceResponse<EmployeeDto> enrollEmployee(@Valid @RequestBody EmployeeDto customUserDto) throws GenericException {
        return employeeService.enrollEmployee(customUserDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public ServiceResponse<Page<EmployeeDto>> searchEmployee(EmployeeSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException {
        Page<Employee>  employeePage = employeeService.getEmployeeList(criteria, pageable);

        return new ServiceResponse(Utils.getSuccessResponse(),
                Utils.toDtoList(employeePage, EmployeeDto.class),
                new Pagination(employeePage.getTotalElements(), employeePage.getNumberOfElements(), employeePage.getNumber(), employeePage.getSize()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(path = "/{id}")
    public ServiceResponse<EmployeeDto> getEmployeeById(@PathVariable(name = "id") Long id ) throws GenericException {
        return employeeService.findEmployeeById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ServiceResponse<EmployeeDto> updateEmployeeById(@PathVariable(name = "id") Long id, @RequestBody EmployeeDto employeeBean) throws GenericException {
        return employeeService.updateEmployeeById(id, employeeBean);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ServiceResponse<Boolean> deleteEmployeeById(@PathVariable(name = "id") Long id) throws GenericException {
        return employeeService.deleteEmployeeById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/update-password")
    public ServiceResponse<PasswordChangeResponseDto> updatePassword(@RequestBody PasswordChangeRequestDto passwordChangeRequestDto) throws GenericException {
        return userService.changeUserPassword(passwordChangeRequestDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/reset-password")
    public ServiceResponse<PasswordChangeResponseDto> resetPassword(@RequestBody PasswordResetRequestDto passwordResetRequestDto) throws GenericException {
        return userService.resetPassword(passwordResetRequestDto);
    }
}
