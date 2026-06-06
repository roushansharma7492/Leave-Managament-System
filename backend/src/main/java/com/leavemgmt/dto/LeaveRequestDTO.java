package com.leavemgmt.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * LeaveRequestDTO - Data Transfer Object for Leave Request
 */
public class LeaveRequestDTO {

    private Long id;

    @NotBlank(message = "Leave type cannot be blank")
    private String leaveType;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be in present or future")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @FutureOrPresent(message = "End date must be in present or future")
    private LocalDate endDate;

    private Integer numberOfDays;

    @NotBlank(message = "Reason cannot be blank")
    @Size(min = 10, max = 1000, message = "Reason must be between 10 and 1000 characters")
    private String reason;

    private String status;
    private String managerComment;
    private Long employeeId;
    private String employeeName;
    private String department;
    private String appliedDate;
    private String approvedRejectedDate;

    public LeaveRequestDTO() {
    }

    public LeaveRequestDTO(Long id, String leaveType, LocalDate startDate, LocalDate endDate,
                           Integer numberOfDays, String reason, String status, String managerComment,
                           Long employeeId, String employeeName, String department, String appliedDate,
                           String approvedRejectedDate) {
        this.id = id;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
        this.status = status;
        this.managerComment = managerComment;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.department = department;
        this.appliedDate = appliedDate;
        this.approvedRejectedDate = approvedRejectedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getApprovedRejectedDate() {
        return approvedRejectedDate;
    }

    public void setApprovedRejectedDate(String approvedRejectedDate) {
        this.approvedRejectedDate = approvedRejectedDate;
    }
}
