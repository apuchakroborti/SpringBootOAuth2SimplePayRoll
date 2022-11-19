package com.example.payroll.repository.payroll;

import com.example.payroll.entity.payroll.Employee;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

//    Employee findByEmail(@Param("email") String email);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByUserId(String userId);

    @Query("select  cu from Employee cu where cu.oauthUser.id = ?#{principal.id}")
    Optional<Employee> getLoggedInEmployee();

}
