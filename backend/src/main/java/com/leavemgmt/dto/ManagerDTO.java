package com.leavemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ManagerDTO - Data Transfer Object for Manager
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDTO {
    private Long id;
    private String employeeId;
    private String name;
    private String email;
    private String department;
    private String role;
    private Boolean isActive;
    private String createdAt;
}
