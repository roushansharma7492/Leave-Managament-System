package com.leavemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmployeeDTO - Data Transfer Object for Employee
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String employeeId;
    private String name;
    private String email;
    private String department;
    private String role;
    private Integer totalLeaves;
    private Integer remainingLeaves;
    private Integer usedLeaves;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}
