package com.leavemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthResponseDTO - Data Transfer Object for Authentication Response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String employeeId;
    private String name;
    private String email;
    private String role;
    private Integer remainingLeaves;
}
