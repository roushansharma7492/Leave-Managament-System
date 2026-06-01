package com.leavemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * LeaveRequestDTO - Data Transfer Object for Leave Request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
