package com.example.payroll.model.payroll;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ROLES")
public class Roles extends Common{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LEVEL")
    private String accessLevel;

    @Column(name = "STATUS")
    private String activeStatus;
}
