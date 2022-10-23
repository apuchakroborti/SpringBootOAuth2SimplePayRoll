package com.example.payroll.repository.payroll;

import com.example.payroll.model.payroll.Roles;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Roles, Long> {
}
