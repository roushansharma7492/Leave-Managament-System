package com.leavemgmt.repository;

import com.leavemgmt.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * ManagerRepository - JPA Repository for Manager Entity
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByEmployeeId(String employeeId);

    Optional<Manager> findByEmail(String email);

    Optional<Manager> findByEmployeeIdOrEmail(String employeeId, String email);

    long countByIsActive(Boolean isActive);
}
