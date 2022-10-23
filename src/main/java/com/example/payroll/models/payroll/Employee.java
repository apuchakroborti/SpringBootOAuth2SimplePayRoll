package com.example.payroll.models.payroll;

import com.example.payroll.security_oauth2.models.security.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "EMPLOYEE")
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends EntityCommon {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "TIN")
    private String tin;

    @Column(name = "NID")
    private String nid;

    @Column(name = "PASSPORT")
    private String passport;

    @Column(name = "DATE_OF_JOINING", nullable = false)
    private LocalDate dateOfJoining;

    @Column(name = "DESIGNATION_ID")
    private Integer designationId;

    @Column(name = "ADDRESS_ID")
    private Integer addressId;

    @Column(name = "STATUS", nullable = false)
    private Boolean status;

    @OneToOne(fetch = FetchType.LAZY)
    private User oauthUser;
}
