package com.example.payroll.repository.payroll;

import com.example.payroll.model.payroll.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Employee findByUsername(String username);
    @Query("select e "+
            "   from " +
            "   Employee e " +
            "   where " +
            "   e.status='ACTIVE' and "+
            "   (:employeeId is null or e.id = :employeeId) and" +
            "   (:firstName is null or e.firstName like :firstName) and  " +
            "   (:username is null or e.username = :username) and" +
            "   (:dateOfJoining is null or e.dateOfJoining= :dateOfJoining) and  " +
            "   (:email is null or e.email= :email) ")
    Page<Employee> searchEmployee(@Param("employeeId") Long employeeId,
                                                   @Param("firstName") String firstName,
                                                    @Param("username") String username,
                                                   @Param("dateOfJoining") LocalDate dateOfJoining,
                                                   @Param("email") String email,
                                                   Pageable pageable);

}
