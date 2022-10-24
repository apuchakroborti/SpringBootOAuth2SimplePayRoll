package com.example.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto extends CommonDto implements Serializable {
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String tin;
    private String nid;
    private String passport;
    private LocalDate dateOfJoining;
    private Integer designationId;
    private Integer addressId;
    private Boolean status;
    private String password;
    @NotNull(message = "Gross salary must not be null!")
    private Double grossSalary;
}
