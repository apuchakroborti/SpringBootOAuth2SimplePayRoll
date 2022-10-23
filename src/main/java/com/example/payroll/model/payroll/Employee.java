package com.example.payroll.model.payroll;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "EMPLOYEE")
public class Employee extends Common{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "TIN")
    private String tin;

    @Column(name = "NID")
    private String nid;

    @Column(name = "PASSPORT")
    private String passport;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "DATE_OF_JOINING", nullable = false)
    private LocalDate dateOfJoining;

    @Column(name = "DESIGNATION")
    private String designation;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ROLE_ID", nullable = false)
    private Integer rollId;

    @Column(name = "STATUS", nullable = false)
    private String status;
}
