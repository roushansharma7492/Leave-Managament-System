package com.leavemgmt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LeaveRequest Entity - Represents a leave request submitted by an employee
 */
@Entity
@Table(name = "leave_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "number_of_days", nullable = false)
    private Integer numberOfDays;

    @Column(name = "reason", length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "manager_comment", length = 500)
    private String managerComment;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "approved_rejected_date")
    private LocalDateTime approvedRejectedDate;

    @Column(name = "applied_date", nullable = false, updatable = false)
    private LocalDateTime appliedDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Lifecycle callbacks for audit timestamps
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        appliedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
