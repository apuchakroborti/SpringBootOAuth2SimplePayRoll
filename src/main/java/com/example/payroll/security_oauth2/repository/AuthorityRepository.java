package com.example.payroll.security_oauth2.repository;


import com.example.payroll.security_oauth2.models.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(String name);
}