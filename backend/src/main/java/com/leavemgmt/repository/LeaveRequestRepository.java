package com.leavemgmt.repository;

import com.leavemgmt.entity.LeaveRequest;
import com.leavemgmt.entity.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * LeaveRequestRepository - JPA Repository for LeaveRequest Entity
 */
@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    Page<LeaveRequest> findByEmployeeId(Long employeeId, Pageable pageable);

    List<LeaveRequest> findByEmployeeId(Long employeeId);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);

    @Query("SELECT lr FROM LeaveRequest lr WHERE " +
           "(:employeeId IS NULL OR lr.employee.id = :employeeId) AND " +
           "(:status IS NULL OR lr.status = :status) AND " +
           "(:startDate IS NULL OR lr.startDate >= :startDate) AND " +
           "(:endDate IS NULL OR lr.endDate <= :endDate)")
    Page<LeaveRequest> findWithFilters(
            @Param("employeeId") Long employeeId,
            @Param("status") LeaveStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query("SELECT lr FROM LeaveRequest lr WHERE " +
           "lr.employee.department = :department AND " +
           "(:status IS NULL OR lr.status = :status)")
    Page<LeaveRequest> findByDepartmentAndStatus(
            @Param("department") String department,
            @Param("status") LeaveStatus status,
            Pageable pageable);

    long countByStatus(LeaveStatus status);

    @Query("SELECT COUNT(lr) FROM LeaveRequest lr WHERE " +
           "(:department IS NULL OR lr.employee.department = :department) AND " +
           "lr.status = :status")
    long countByDepartmentAndStatus(
            @Param("department") String department,
            @Param("status") LeaveStatus status);
}
