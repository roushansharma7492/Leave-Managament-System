package com.leavemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

/**
 * LeaveApprovalDTO - Data Transfer Object for Leave Approval/Rejection
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApprovalDTO {

    @NotNull(message = "Leave request ID cannot be null")
    private Long leaveRequestId;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "APPROVED|REJECTED", message = "Status must be APPROVED or REJECTED")
    private String status;

    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String managerComment;
}
