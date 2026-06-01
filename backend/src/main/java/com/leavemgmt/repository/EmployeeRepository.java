package com.leavemgmt.repository;

import com.leavemgmt.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * EmployeeRepository - JPA Repository for Employee Entity
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeId(String employeeId);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmployeeIdOrEmail(String employeeId, String email);

    List<Employee> findByDepartment(String department);

    List<Employee> findByIsActive(Boolean isActive);

    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employee> searchEmployees(@Param("searchTerm") String searchTerm);

    long countByIsActive(Boolean isActive);
}
